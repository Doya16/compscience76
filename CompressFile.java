/**
 * Module 15: Programming Project
 * @author Arshmeet Kaur
 * @author @roomy add ur name here.
 */

public class CompressFile {

    public static void compressFile(File source, File target) throws IOException {

        /** pass to a method that reads the file in (line by line) using the Scanner class
         * returns a Stringbuilder containing the full file's text.*/
        readFile(source);

        /** pass that Stringbuilder containing the full file's text to calculateFrequencies.
         * That should return an array of counts. */
        int[] counts = calculateFrequencies(sourceFilePath); // similar to my getCharacterFrequency method

        /** Build the Huffman Tree.
         * Pas the array of c*/
        getHuffmanTree(counts)

        output.writeObject(huffmanCodes.toString()); // write huffman codes to target file

        reader.close();
        output.close();
    }

    public static void main(String[] args) {

    }
}