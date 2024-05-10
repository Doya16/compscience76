import java.io.*;

/**
 * BitInputStream will read Bytes in from binary files.
 *
 * @author Arshmeet Kaur
 */

public class BitInputStream {

    /**
     *  Variables:
     *  We define an input stream and an empty stringbuilder.
     *  The stringbuilder will be used to read one character at a time.
     */
    private FileInputStream streamIn;
    private StringBuilder fullbinarySb = new StringBuilder("");

    /**
     * <p>This constructor creates a FileInStream, reads all bytes from the file and stores them in the stringbuilder,
     * and then closes and reopens a new, fresh stream for the user </p>
     * @param f File f which the user wishes to read in.
     */
    public BitInputStream (File f) throws IOException{

            streamIn = new FileInputStream(f);

            int bytesAva = streamIn.available();
            for (int i = 0; i<bytesAva; i++){
                String binaryString = Integer.toBinaryString(streamIn.read());
                int leftover = 8 - binaryString.length();
                //System.out.println("before padding " +binaryString);
                for (int j = 0; j<leftover; j++){
                    binaryString = "0" + binaryString;
//                    System.out.println(binaryString);
                }
                System.out.println("after padding " +binaryString);
                fullbinarySb.append(binaryString);
                System.out.println("SB after padding 2 " +fullbinarySb.toString());
            }
            streamIn.close();
            streamIn = new FileInputStream(f);
    }

    /**
     * <p> First method: available bites</p>
     * We simply count the length of the stringBuilder fullbinarySb to see bits available.
     * Shortly, in other methods, we see that length decreases when a character is read.
     * @return bits available
     */

    //first method: availaible bits
    public int available(){
        return fullbinarySb.length();
    }

    //second method: prints next character
    public char readBit() throws IOException{
        char c = 0;
            //the "next" character
            c = fullbinarySb.charAt(0);
            //also deletes 1 from bitsAva
            fullbinarySb.deleteCharAt(0);
        return c;
    }

    /**
     * <p>third method: readBits() returns the entire unread string</p>
     * We simply return the fullbinarySb. Any characters that were already read were deleted from here in other methods.
     * @return all portion of the byte file that was not yet read.
     */
    public String readBits() throws IOException{
        String fullBinaryStr = "There are no bits left to read.";
            fullBinaryStr = fullbinarySb.toString();
            //setting the length to 0 again.
            fullbinarySb = new StringBuilder("");
        return fullBinaryStr;
    }

    /**
     * fourth method:
     * <p> This method closes the streamIn used in thos method.</p>
     */
    public void close() throws IOException{
            streamIn.close();
    }

    public static void main(String[] args) throws Exception{

        //full bytes from file opened on bit viewer
        BitInputStream bis = new BitInputStream(new File("/Users/arshmeetkaur/Java II/fileio-handout_old copy/test.dat"));
        System.out.println("File" + bis.readBits());
        System.out.print(bis.available());
        System.out.println(bis.readBit());
        System.out.print(bis.available());
        System.out.println(bis.readBit());
        System.out.print(bis.available());
        System.out.println(bis.readBit());
        System.out.println("Rest of the strint" + bis.readBits());
        System.out.println(bis.available());
        bis.close();
//Expected 10101010-01010000,
//     Got 11111010-11010000"
//

        //11111000 11111001 10000000
        //11111000 11111001 10000000

//        System.out.println(bis.available());
//
//        System.out.println(bis.readBit());
//
//        System.out.println(bis.available());
//
//        System.out.println(bis.readBit());
//
//        System.out.println(bis.readBits());
//
//        System.out.println(bis.available());
//
//        //handles the case where someone tries to read bit after there are none left
//        System.out.println(bis.readBit());
//        System.out.println(bis.readBits());
//
//        bis.close();
    }

}