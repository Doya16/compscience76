import java.io.*;
import java.util.Scanner;

/**
 * Module 15: Programming Project
 * @author Arshmeet Kaur
 * @author Vincent Tran
 */

public class CompressFile {

    public static void compressFile(File source, File target) throws IOException {

        // vincent
        /** pass to a method that reads the file in (line by line) using the Scanner class
         * returns a String containing the full file's text. */
        String messageInput = readFile(source);
        System.out.println(messageInput); // print string test, should output entire text from any inputted .txt file (Remove when done)

        // vincent
        /** pass that String containing the full file's text to calculateFrequencies.
         * That should return an array of counts. */
        int[] counts = calculateFrequencies(messageInput); // similar to my getCharacterFrequency method

        /** Prints ASCII code, character, and frequency to check
         * CAN DELETE WHEN WE'RE CLOSE TO FINISHING */
        System.out.printf("%-15s%-15s%-15s\n", "ASCII Code", "Character", "Frequency");
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] != 0) {// (char)i is not in text if counts[i] is 0
                if (i == 10) { // ASCII for 10 is new line
                    System.out.printf("%-15d%-15s%-15d\n", i, "New Line", counts[i]);
                }
                else if (i == 13) { // ASCII for 13 is Carriage Return (Ignore when decompressing)
                    System.out.printf("%-15d%-15s%-15d\n", i, "CR", counts[i]);
                }
                else {
                    System.out.printf("%-15d%-15s%-15d\n", i, (char)i + "", counts[i]);
                }
            }
        }


        /** Build the Huffman Tree.
         * Pass the array of counts into the function to get a huffman tree.
         * Returns a tree object. */
        HuffmanTree hf = getHuffmanTree(counts)

        /** Get the Huffman Codes from the tree for each ASCII character. */
        String[] charKey = getCode() // calls assignCode

        // vincent
        /** Get the actual code that we want to put into the output file. */
        String outputMessage = writeMessage(charKey, messageInput);

        ObjectOutputStream oos = new ObjectOutputStream(new FileInputStream(target));
        output.writeObject(outputMessage); // write huffman codes to target file

        /** Very confused wtf BitOutputStream doing here? */

        output.flush();
        output.close();
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
        int[] counts = new int[256];

        for (int i = 0; i < text.length(); i++) {
            counts[(int)text.charAt(i)]++; // count the character in text
        }
        return counts;
    }

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
}

/** Needed for the Huffman Tree. */
class Heap<E extends Comparable<E>> {
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

/** Arshmeet Kaur */
/** Needed for the Huffman Tree. */
public static class HuffmanTree implements Comparable<Tree> {

    /** Need Inner Class for the HuffmanNode */
    public class Node {}
}
