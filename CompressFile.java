package compscience76;

import java.io.*;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Module 15: Programming Project
 * @author Arshmeet Kaur
 * @author Vincent Tran
 */

public class CompressFile {

    /** Run "java CompressFile sourceFile.txt compressedFile.txt" in terminal to test. */
    public static void main(String[] args) {
        if(args.length == 2) {
            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);

            try {
                System.out.println("Compressing file.");
                compressFile(inputFile, outputFile);
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
        else {
            System.out.println("Usage: java CompressFile (InputFile) (OutputFile)");
        }
    }

    public static void compressFile(File source, File target) throws IOException {

        // vincent
        /** pass to a method that reads the file in (line by line) using the Scanner class
         * returns a String containing the full file's text. */
        String messageInput = readFile(source);
        System.out.println(messageInput); // print string test, should output entire text from any inputted .txt file (Remove when done)

        // vincent
        /** pass that String containing the full file's text to calculateFrequencies.
         * That should return an array of counts. */
        int[] frequencies = calculateFrequencies(messageInput); // similar to my getCharacterFrequency method

        /** Prints ASCII code, character, and frequency to check
         * CAN DELETE WHEN WE'RE CLOSE TO FINISHING */
        System.out.printf("%-15s%-15s%-15s\n", "ASCII Code", "Character", "Frequency");
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] != 0) {// (char)i is not in text if counts[i] is 0
                if (i == 10) { // ASCII for 10 is new line
                    System.out.printf("%-15d%-15s%-15d\n", i, "New Line", frequencies[i]);
                }
                else if (i == 13) { // ASCII for 13 is Carriage Return (Ignore when decompressing)
                    System.out.printf("%-15d%-15s%-15d\n", i, "CR", frequencies[i]);
                }
                else {
                    System.out.printf("%-15d%-15s%-15d\n", i, (char)i + "", frequencies[i]);
                }
            }
        }

        // arshmeet
        /** Build the Huffman Tree.
         * Pass the array of counts into the function to get a huffman tree.
         * Returns a tree object. */
        HuffmanTree hf = getHuffmanTree(frequencies);

        // arshmeet
        /** Get the Huffman Codes from the tree for each ASCII character. */
        String[] charKey = getCode(hf.root); // calls assignCode

        // vincent
        /** Get the actual code that we want to put into the output file. */
        String outputMessage = writeMessage(charKey, messageInput);

        // arshmeet
        ObjectOutputStream oos = new ObjectOutputStream(new BitOutputStream(target));
        oos.writeObject(outputMessage); // write huffman codes to target file

        oos.flush();
        oos.close();
    }

    /**
     * Takes input file and returns a string containing entire text from the file
     * @param source File to read text from
     * @return String containing text from file
     * @throws FileNotFoundException
     * @author Vincent Tran
     */
    public static String readFile(File source) throws FileNotFoundException {
        Scanner input = new Scanner(source);
        String content = input.useDelimiter("\\Z").next(); // delimiter allows entire text to be read instead of single word using .next()
        input.close();
        return content;
    }

    /**
     * Takes a string and casts each character as an integer to convert it into ASCII code, increasing
     * the count by 1 using the ASCII code of the character as the index
     * @param text String to count frequency of characters for
     * @return Integer array containing counts of each character
     * @author Vincent Tran
     */
    public static int[] calculateFrequencies(String text) {
        int[] frequencies = new int[256];

        for (int i = 0; i < text.length(); i++) {
            frequencies[(int)text.charAt(i)]++; // count the character in text
        }
        return frequencies;
    }

    /** Construct the Huffman Tree. */
    public static HuffmanTree getHuffmanTree(int[] frequencies) {
        // get a min heap
        Heap<HuffmanTree> minheap = new Heap<>(); //heap of type Type HuffmanTree
        for(int i = 0; i < frequencies.length; i++) {
            minheap.add(new HuffmanTree(frequencies[i], (char)i));
        }

        while (minheap.getSize() > 1) {
            // remove the two smallest weight trees
            HuffmanTree h1 = minheap.remove();
            HuffmanTree h2 = minheap.remove();
            // merge those trees into one tree with there combined weights
            minheap.add(new HuffmanTree(h1, h2));
        }

        return minheap.remove(); // the last tree (with all of the lower ones under it) is our tree
    }

    /** get the codes */
    public static String[] getCode(HuffmanTree.HuffmanNode root) {
        // if the tree is empty...
        if (root == null)
            return null;
        // the string array of codes is 256, one for each ASCII character
        String[] encoding = new String[256];
        // fill the array with the code for each character
        assignCode(root, encoding);
        return encoding;
    }

    public static void assignCode(HuffmanTree.HuffmanNode root, String[] encoding) {
        if (root.left != null) {
            // add zeros going leftwards
            root.left.code = root.code + "0";
            assignCode(root.left, encoding);

            // add ones going rightwards
            root.right.code = root.code + "1";
            assignCode(root.right, encoding);
        } else {
            // when you hit the leaf (containing characters), save the code that has been built
            // in the recrusive calls
            encoding[(int) root.data] = root.code;
        }
    }

    public static String writeMessage(String[] charKey, String messageInput) {
        String message = "";
        //System.out.println(Arrays.toString(charKey));
        for (String key: charKey) {
            message += key;
        }
        return message;
    }
}

/** Needed for the Huffman Tree. */
class Heap<E extends Comparable<E>> implements Serializable {
    private java.util.ArrayList<E> list = new java.util.ArrayList<E>();

    /** Create a default heap */
    public Heap() {
    }

    /** Create a heap from an array of objects */
    public Heap(E[] objects) {
        for (int i = 0; i < objects.length; i++)
            add(objects[i]);
    }

    /** Add a new object into the heap */
    public void add(E newObject) {
        list.add(newObject); // Append to the heap
        int currentIndex = list.size() - 1; // The index of the last node

        while (currentIndex > 0) {
            int parentIndex = (currentIndex - 1) / 2;
            // Swap if the current object is greater than its parent
            if (list.get(currentIndex).compareTo(
                    list.get(parentIndex)) > 0) {
                E temp = list.get(currentIndex);
                list.set(currentIndex, list.get(parentIndex));
                list.set(parentIndex, temp);
            }
            else
                break; // the tree is a heap now

            currentIndex = parentIndex;
        }
    }

    /** Remove the root from the heap */
    public E remove() {
        if (list.size() == 0) return null;

        E removedObject = list.get(0);
        list.set(0, list.get(list.size() - 1));
        list.remove(list.size() - 1);

        int currentIndex = 0;
        while (currentIndex < list.size()) {
            int leftChildIndex = 2 * currentIndex + 1;
            int rightChildIndex = 2 * currentIndex + 2;

            // Find the maximum between two children
            if (leftChildIndex >= list.size()) break; // The tree is a heap
            int maxIndex = leftChildIndex;
            if (rightChildIndex < list.size()) {
                if (list.get(maxIndex).compareTo(
                        list.get(rightChildIndex)) < 0) {
                    maxIndex = rightChildIndex;
                }
            }

            // Swap if the current node is less than the maximum
            if (list.get(currentIndex).compareTo(list.get(maxIndex)) < 0) {
                E temp = list.get(maxIndex);
                list.set(maxIndex, list.get(currentIndex));
                list.set(currentIndex, temp);
                currentIndex = maxIndex;
            }
            else
                break; // The tree is a heap
        }

        return removedObject;
    }

    /** Get the number of nodes in the tree */
    public int getSize() {
        return list.size();
    }
}

/** Inner class needed for the Huffman Tree Object. */
class HuffmanTree implements Comparable<HuffmanTree>, Serializable {

    HuffmanNode root;

    /** constructor for a huffman tree with children */
    public HuffmanTree(HuffmanTree h1, HuffmanTree h2) {
        root = new HuffmanNode();
        root.left = h1.root;
        root.right = h2.root;
        root.weight = h1.root.weight + h2.root.weight;
    }

    /** constructor for leaf */
    public HuffmanTree(int weight, char data) {
        root = new HuffmanNode(weight, data);
    }

    /** should implement compareTo() in reverse order:
     * Because heap we have created is a max heap.
     * But we want the minimum frequency at the top so that when we're merging trees, we're picking off the smallest
     * elements each time.
     * */
    @Override
    public int compareTo(HuffmanTree h) {
        boolean hDifference = this.root.weight > h.root.weight;
        if (hDifference)
            return -1;
        else if (this.root.weight == h.root.weight)
            return 0;
        else
            return 1;
    }

    /** Need Inner Class for the HuffmanNode */
    public class HuffmanNode implements Serializable {
        /**
         * data holds the character at each node.
         * weight holds the weight of the node.
         * left and right and the children of the node
         * code is path of 0s or 1s that lead to the node.
         */
        char data;
        int weight;
        HuffmanNode left; HuffmanNode right;
        String code = "";

        /** default constructor. */
        public HuffmanNode(){}
        /** value constructor gives each node character and weight */
        public HuffmanNode(int weight, char data) {
            this.weight = weight;
            this.data = data;
        }

    }
}

class BitOutputStream extends FileOutputStream implements Serializable {

    /**
     * Variables:
     * streamOut is the FileOutputStream used for all operations in this class's objects.
     * we define a Stringbuilder byteString for later use. We will build an 8 character long Stringbuilder, then write it as a byte into the file.
     */


    private FileOutputStream streamOut;
    private static StringBuilder byteString = new StringBuilder(8);
    int bitHolderByteArray = 0;


    int byteLength = 0;


    /**
     * <p> This is a constructor, it verifies the existence of the file and creates the stream </p>
     * @param f the File object a person has used to creat the BitOutputStream Object
     */
    public BitOutputStream(File f) throws IOException{
        super(f);
    }

    /**
     * <p> writeBit() simply writes one character that the user wishes to write.
     * It must be a 0 or 1 and this is checked with a conditional.
     * Each byte holds 8 of the characters a user can enter before it gets written to the file.
     * </p>
     * @param bit one character which the user wishes to write.
     */
    public void writeBit(char bit) throws IOException{
        //check that the bit is either 0 or 1
        if (bit == '0' | bit == '1'){
            byteLength++;
            int incomingByte =  (bit -'0');
            System.out.println("incomingByte.."+incomingByte);
            //move the existing byte by one place
            bitHolderByteArray = (bitHolderByteArray << 1)| incomingByte;
            //append to the right most bit value in the
            String binaryString = Integer.toBinaryString(bitHolderByteArray);
            System.out.println("length "+ byteLength + " bit =>" + binaryString);


            if(byteLength == 8) {
                binaryString = Integer.toBinaryString(bitHolderByteArray);
                System.out.println("byte =>" + binaryString);
                //write the 8 bits to file
                streamOut.write(bitHolderByteArray);
                //reset the counter
                byteLength = 0;
                //reset the byte
                bitHolderByteArray = 0;
            }
        } else {
            throw new InputMismatchException("Input can only be '0' or '1' whether characters or a whole String");
        }
    }


    /**
     * <p> This method allows the user to write a whole string of characters to the file.
     * Every string is tested to see if it only consists of 1 and 0.
     * Every string is split into characters.
     * The characters are then sent one by one to the writeBit() method. </p>
     * @param bitString is the string to be written.
     */
    public void writeBits(String bitString) throws IOException{
        char[] binChars = bitString.toCharArray();
        boolean correctFormat = true;
        for (char b: binChars){
            if (b != '1' && b != '0'){
                correctFormat = false;
            }
        }


        if (correctFormat){
            for (char b: binChars){
                writeBit(b);
            }
        } else {
            throw new InputMismatchException("Input can only be '0' or '1' whether characters or a whole String");
        }
    }

    /**
     * <p> The close method:
     * At the end of all user's input the close method will turn those "unfinished" (<8 bit) strings into 8 bit strings and write them:
     * zeros are concatenated to the front of byteString.toString() according to how many zeros are needed (calculated in the forloop)
     * the byteString is then parsed into a base2 integer
     * The file is closed at the end. </p>
     */
    public void close() throws IOException {
        if (byteLength > 0) {
            // add 0s to the remaining places
            bitHolderByteArray <<= (8 - byteLength);
            streamOut.write(bitHolderByteArray);
        }
        streamOut.close();
    }
}
