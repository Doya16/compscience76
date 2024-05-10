///**
// * BitOutputStream will write either one bit at a time or a string of bits into a file.
// *
// * @author Arshmeet Kaur
// */
//
///**
// * Neccessary Imports:
// */
//
//import java.lang.*;
//import java.io.*;
//import java.util.*;
//
//public class BitOutputStream implements Serializable{
//
//    /**
//     * Variables:
//     * streamOut is the FileOutputStream used for all operations in this class's objects.
//     * we define a Stringbuilder byteString for later use. We will build an 8 character long Stringbuilder, then write it as a byte into the file.
//     */
//
//    private FileOutputStream streamOut;
//    private static StringBuilder byteString = new StringBuilder(8);
//    int bitHolderByteArray = 0;
//
//    int byteLength = 0;
//
//    /**
//     * <p> This is a constructor, it verifies the existence of the file and creates the stream </p>
//     * @param f the File object a person has used to creat the BitOutputStream Object
//     */
//    public BitOutputStream(File f) throws IOException{
//            streamOut = new FileOutputStream(f);
//    }
//
//    /**
//     * <p> writeBit() simply writes one character that the user wishes to write.
//     * It must be a 0 or 1 and this is checked with a conditional.
//     * Each byte holds 8 of the characters a user can enter before it gets written to the file.
//     * </p>
//     * @param bit one character which the user wishes to write.
//     */
//    public void writeBit(char bit) throws IOException{
//        //check that the bit is either 0 or 1
//        if (bit == '0' | bit == '1'){
//            byteLength++;
//            int incomingByte =  (bit -'0');
//            System.out.println("incomingByte.."+incomingByte);
//            //move the existing byte by one place
//            bitHolderByteArray = (bitHolderByteArray << 1)| incomingByte;
//            //append to the right most bit value in the
//            String binaryString = Integer.toBinaryString(bitHolderByteArray);
//            System.out.println("length "+ byteLength + " bit =>" + binaryString);
//
//            if(byteLength == 8) {
//                 binaryString = Integer.toBinaryString(bitHolderByteArray);
//                System.out.println("byte =>" + binaryString);
//                //write the 8 bits to file
//                streamOut.write(bitHolderByteArray);
//                //reset the counter
//                byteLength = 0;
//                //reset the byte
//                bitHolderByteArray = 0;
//            }
//        } else {
//            throw new InputMismatchException("Input can only be '0' or '1' whether characters or a whole String");
//        }
//    }
//
//    /**
//     * <p> This method allows the user to write a whole string of characters to the file.
//     * Every string is tested to see if it only consists of 1 and 0.
//     * Every string is split into characters.
//     * The characters are then sent one by one to the writeBit() method. </p>
//     * @param bitString is the string to be written.
//     */
//    public void writeBits(String bitString) throws IOException{
//        char[] binChars = bitString.toCharArray();
//        boolean correctFormat = true;
//        for (char b: binChars){
//            if (b != '1' && b != '0'){
//                correctFormat = false;
//            }
//        }
//
//        if (correctFormat){
//            for (char b: binChars){
//                writeBit(b);
//            }
//        } else {
//            throw new InputMismatchException("Input can only be '0' or '1' whether characters or a whole String");
//        }
//    }
//
//
//    /**
//     * <p> The close method:
//     * At the end of all user's input the close method will turn those "unfinished" (<8 bit) strings into 8 bit strings and write them:
//     * zeros are concatenated to the front of byteString.toString() according to how many zeros are needed (calculated in the forloop)
//     * the byteString is then parsed into a base2 integer
//     * The file is closed at the end. </p>
//     */
//    public void close() throws IOException {
//        if (byteLength > 0) {
//            // add 0s to the remaining places
//            bitHolderByteArray <<= (8 - byteLength);
//            streamOut.write(bitHolderByteArray);
//        }
//        streamOut.close();
//    }
//
//    public static void main(String[] args) throws Exception {
//        int i = 8;
//        String binS = Integer.toBinaryString(i);
//        System.out.println(binS);
//        int j = i<<1;
//        System.out.println(Integer.toBinaryString(j));
//        System.out.println(j);
//        File f = new File("/Users/arshmeetkaur/Java II/fileio-handout_old copy/test.dat");
//        BitOutputStream bos = new BitOutputStream(f);
//
//        //method 1 and 2:
//        //1 char
//        //bos.writeBit('0');
//        //15 chr
//        bos.writeBits("10101010010100001");
//        //1 char
//       // bos.writeBit('1');
//
//        //00101010 10111111 00000001
//        //00101010 10111111 00000001
//
//        //close method
//        bos.close();
//    }
//}
//
