package compscience76;
import java.io.*;

public class DecompressFile {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java DecompressFile <compressed file> <decompressed file>");
            return;
        }

        String compressedFile = args[0];
        String decompressedFile = args[1];

        try (FileInputStream fileIn = new FileInputStream(compressedFile);
             ObjectInputStream ois = new ObjectInputStream(fileIn)) {

            // Read Huffman tree
            HuffmanTree hf = (HuffmanTree) ois.readObject();
            System.out.println("Huffman Tree read successfully.");

             // Create a new BitInputStream to start reading bit data
            try (BitInputStream bis = new BitInputStream(fileIn)) {
                // Read the length of the encoded message
                int messageLength = bis.readInt();
                System.out.println("Message length: " + messageLength);

                // Read and decode the bit sequence
                StringBuilder binaryString = new StringBuilder();
                for (int i = 0; i < messageLength; i++) {
                    binaryString.append(bis.readBit() == 1 ? "1" : "0");
                }
                System.out.println("Encoded message read successfully.");

                // Decode the binary string using the Huffman Tree
                String decodedString = decodeBinaryString(binaryString.toString(), hf);
                try (FileOutputStream fos = new FileOutputStream(decompressedFile)) {
                    fos.write(decodedString.getBytes());
                }
                System.out.println("Decoding completed.");
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        }
    }

    public static String decodeBinaryString(String binaryString, HuffmanTree huffmanTree) {
        StringBuilder decodedString = new StringBuilder();
        HuffmanTree.HuffmanNode currentNode = huffmanTree.root;

        for (int i = 0; i < binaryString.length(); i++) {
            char bit = binaryString.charAt(i);
            currentNode = (bit == '0') ? currentNode.left : currentNode.right;

            if (currentNode.left == null && currentNode.right == null) {
                decodedString.append(currentNode.data);
                currentNode = huffmanTree.root; // Reset to root for next character
            }
        }

        return decodedString.toString();
    }

public class BitInputStream extends FileInputStream {
    private int currentByte; // the byte we're reading in
    private int numBits; // the number of bits that have been read in

    public BitInputStream(File file) throws FileNotFoundException {
        super(file);
        currentByte = 0;
        numBits = 0;
    }

    public int readBit() throws IOException {
        if (numBits == 0) { // no bits have been read yet
            // read one byte from FileInputStream
            int newByte = super.read();
            if (newByte == -1) return -1; // end of file
            // set the values of the current byte and the number of bits (8 when a byte is read in)
            currentByte = newByte;
            numBits = 8;
        }
        // shifts the digits to the right by numBits-1 so that the bit of interest is the rightmost
        // & 1 will turn every preceding bit to 0
        int bit = (currentByte >>> (numBits - 1)) & 1;
        numBits--;
        return bit;
    }

    public String readBits(int quantity) throws IOException {
        StringBuilder bitString = new StringBuilder();
        for (int i = 0; i < quantity; i++) {
            int bit = readBit();
            if (bit == -1) break; // end of file
            bitString.append(bit);
        }
        return bitString.toString();
    }

    public int readInt() throws IOException {
        int intValue = 0;
        for (int i = 0; i < 4; i++) {
            int oneByte = super.read();
            if (oneByte == -1) throw new EOFException("End of input reached");
            intValue = (intValue << 8) | (oneByte & 0xFF);
        }
        return intValue;
    }

    public int readByte() throws IOException {
        return Integer.parseInt(readBits(8), 2);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}

