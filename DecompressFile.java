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

    // Nested private class for bit-level input
    private static class BitInputStream implements Closeable {
        private InputStream in;
        private int currentByte;
        private int numBitsFilled;

        public BitInputStream(InputStream in) {
            this.in = in;
            this.currentByte = 0;
            this.numBitsFilled = 0;
        }

        public int readBit() throws IOException {
            if (numBitsFilled == 0) {
                currentByte = in.read();
                if (currentByte == -1) {
                    return -1;  // End of stream
                }
                numBitsFilled = 8;
            }
            numBitsFilled--;
            return (currentByte >> numBitsFilled) & 1;
        }

        public int readInt() throws IOException {
            int result = 0;
            for (int i = 0; i < 4; i++) {
                int value = in.read();
                if (value == -1) {
                    throw new IOException("Unexpected end of stream");
                }
                result |= value << (8 * (3 - i));
            }
            return result;
        }

        @Override
        public void close() throws IOException {
            in.close();
        }
    }
}
