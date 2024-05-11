package compscience76;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.lang.instrument.Instrumentation;

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

    public static void compressFile(File source, File target) throws IOException, ClassNotFoundException {

        // vincent
        /** pass to a method that reads the file in (line by line) using the Scanner class
         * returns a String containing the full file's text. */
        String messageInput = readFile(source);
        // System.out.println(messageInput); // print string test, should output entire text from any inputted .txt file (Remove when done)

        // vincent
        /** pass that String containing the full file's text to calculateFrequencies.
         * That should return an array of counts. */
        int[] frequencies = calculateFrequencies(messageInput); // similar to my getCharacterFrequency method

        // arshmeet
        /** Build the Huffman Tree.
         * Pass the array of counts into the function to get a huffman tree.
         * Returns a tree object. */
        HuffmanTree hf = getHuffmanTree(frequencies);

        // arshmeet
        /** Get the Huffman Codes from the tree for each ASCII character. */
        String[] charKey = hf.getCode(hf.root); // calls assignCode
        //  System.out.println(Arrays.toString(charKey));

        System.out.printf("%-15s%-15s%-15s%-15s\n", "ASCII Code", "Character", "Frequency", "Encoding");
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] != 0) {
                if (i == 10) { // ASCII for 10 is new line
                    System.out.printf("%-15d%-15s%-15d%-15s\n", i, "New Line", frequencies[i], charKey[i]);
                } else if (i == 13) { // ASCII for 13 is Carriage Return (Ignore when decompressing)
                    System.out.printf("%-15d%-15s%-15d%-15s\n", i, "CR", frequencies[i], charKey[i]);
                } else {
                    System.out.printf("%-15d%-15s%-15d%-15s\n", i, (char) i + "", frequencies[i], charKey[i]);
                }
            }
        }

        // vincent
        /** Get the actual code that we want to put into the output file. */
        String outputMessage = writeMessage(charKey, messageInput);
        System.out.println("Message to be printed to file "+outputMessage);

        // arshmeet
        BitOutputStream bos = new BitOutputStream(target, true);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(hf); // writing huffman
        bos.writeInt(outputMessage.length());
        bos.writeBits(outputMessage); // write huffman codes to target file

        oos.flush();
        bos.flush();
        oos.close();
    }

    public static String decodeBinaryString(String binaryString, HuffmanTree huffmanTree) {
        StringBuilder decodedString = new StringBuilder();
        HuffmanTree.HuffmanNode currentNode = huffmanTree.root;


        for (int i = 0; i < binaryString.length(); i++) {
            char bit = binaryString.charAt(i);
            if (bit == '0') {
                currentNode = currentNode.left;
            } else if (bit == '1') {
                currentNode = currentNode.right;
            }


            if (currentNode.left == null && currentNode.right == null) {
                // Reached a leaf node, append the character and reset to root
                decodedString.append(currentNode.data);
                currentNode = huffmanTree.root;
            }
        }


        return decodedString.toString();
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
        System.out.println("Entered frequencies");
        int[] frequencies = new int[256];

        for (int i = 0; i < text.length(); i++) {

            char c = text.charAt(i);
            int index = (int) c;

            if (index > 256){
                continue;
            } else {
                frequencies[index]++; // count the character in text
            }
        }
        System.out.println("exiting");
        return frequencies;
    }

    /** Construct the Huffman Tree. */
    public static HuffmanTree getHuffmanTree(int[] counts) {
        Heap<HuffmanTree> heap = new Heap<>();
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > 0)
                heap.add(new HuffmanTree(counts[i], (char)i)); // A leaf node tree
        }

        if (heap.getSize() == 1){
            // construct a new tree with the actual Tree in the heap a new character with no weight
            // this way there will be a node to traverse through so we can get a code
            heap.add(new HuffmanTree(heap.remove(), new HuffmanTree(0, ' ')));
        }

        while (heap.getSize() > 1) {
            HuffmanTree t1 = heap.remove(); // Remove the smallest weight trees
            HuffmanTree t2 = heap.remove(); // Remove the next smallest weight
            heap.add(new HuffmanTree(t1, t2)); // Combine two trees
        }

        return heap.remove(); // The final tree
    }

    public static String writeMessage(String[] charKey, String messageInput) {

        StringBuilder message = new StringBuilder(); // empty string builder

        for(int i = 0; i < messageInput.length(); i++) {
            char currentCharacter = messageInput.charAt(i);
            int asciiCode = currentCharacter;

            if (asciiCode > 256) {
                continue;
            } else {
                message.append(charKey[asciiCode]);
            }
        }

        return message.toString();
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
    String[] encodings;

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

    /** get the codes */
    public String[] getCode(HuffmanTree.HuffmanNode root) {
        // if the tree is empty...
        if (root == null)
            return null;
        // the string array of codes is 256, one for each ASCII character
        String[] encoding = new String[256];
        // fill the array with the code for each character
        assignCode(root, encoding);
        this.encodings = encoding;
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
            // in the recursive calls
            encoding[(int) root.data] = root.code;
        }
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

    private int currentByte = 0;
    private int numBits = 0;
    boolean append;

    public BitOutputStream(File f, boolean append) throws IOException {
        super(f, append);
        this.append = append;
    }

    public void writeInt(int count) throws IOException {
        super.write(count);
    }

    public void writeBits(String bitString) throws IOException {
        for (char b : bitString.toCharArray()) {

            if (b == '0' || b == '1') {
                numBits++;
                int currentBit =  ( ((int)b)- 48);
                currentByte = (currentByte << 1) | currentBit;
                if (numBits == 8) {
                    super.write(currentByte);
                    numBits = 0;
                    currentByte = 0;
                }
            } else {
                throw new InputMismatchException("Input can only be '0' or '1'");
            }

        }
    }

    public void close() throws IOException {
        if (numBits > 0) {
            currentByte <<= (8 - numBits);
            super.write(currentByte);
        }
        super.close();
    }
}
