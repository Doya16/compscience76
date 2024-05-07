/**
 * Module 15: Programming Project
 * @author Arshmeet Kaur
 * @author Vincent Tran
 */

public class CompressFile {

    public static void compressFile(File source, File target) throws IOException {

        /** pass to a method that reads the file in (line by line) using the Scanner class
         * returns a String containing the full file's text. */

        // vincent
        String messageInput = readFile(source);

        // vincent
        /** pass that String containing the full file's text to calculateFrequencies.
         * That should return an array of counts. */
        int[] counts = calculateFrequencies(messageInput); // similar to my getCharacterFrequency method

        /** Build the Huffman Tree.
         * Pass the array of counts into the function to get a huffman tree.
         * Returns a tree object. */
        HuffmanTree hf = getHuffmanTree(counts)

        /** Get the Huffman Codes from the tree for each ASCII character. */
        String[] charKey = getCode() // calls assignCode

        // vincent
        /** Get the actual code that we want to put int the output file. */
        String outputMessage = writeMessage(charKey, messageInput);

        ObjectOutputStream oos = new ObjectOutputStream(new FileInputStream(target));
        output.writeObject(outputMessage); // write huffman codes to target file

        /** Very confused wtf BitOutputStream doing here? */

        output.flush();
        output.close();
    }

}

/** Needed for the Huffman Tree. */
class Heap<E extends Comparable<E>> {}

/** Arshmeet Kaur */
/** Needed for the Huffman Tree. */
public static class HuffmanTree implements Comparable<Tree> {

    /** Need Inner Class for the HuffmanNode */
    public class Node {}
}
