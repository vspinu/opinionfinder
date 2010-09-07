/*
 * opin/pre/EscapeCodeReplacer.java
 * Alexander Conrad
 * conrada@cs.pitt.edu
 * 25 August 2010
 */

package opin.preprocessor;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * This class replaces HTML / XML escape code with the actual unicode character
 * that it represents. NOTE: this class is not bytespan-safe!
 *
 * This class essentially (though not exactly) replicates the functionality of
 * bin/remove_escapes.py in the opinionfinder1 release.
 *
 * NOTE: this class should be run AFTER html/xml markup is extracted, because
 * of risk that escaped reserved characters (ie, <,>) will be unescaped and
 * then interpreted as being parts of additional markup
 *
 * @author conrada
 */
public class EscapeCodeReplacer {

    // key is code (ie, between & and ;), value is char to replace that code
    public static Hashtable<String, String> escRepl;

    public static void makeEscapeLists() {

        escRepl = new Hashtable<String, String>();
        escRepl.put("euro", " "); // euro
        escRepl.put("nbsp", " ");
        escRepl.put("#32", " ");
        escRepl.put("quot", "\"");
        escRepl.put("#34", "\"");
        escRepl.put("amp", "&");
        escRepl.put("#38", "&");
        escRepl.put("lt", "<");
        escRepl.put("#60", "<");
        escRepl.put("gt", ">");
        escRepl.put("#62", ">");
        escRepl.put("#127", " "); // not used
        escRepl.put("#128", " "); // euro
        escRepl.put("#129", " "); // not used
        escRepl.put("#130", "\""); // single low-9 quotation mark
        escRepl.put("#131", "f"); // latin small letter f with hook
        escRepl.put("#132", "\""); // double low-9 quotation mark
        escRepl.put("#133", " "); // elipses
        escRepl.put("#134", " "); // dagger
        escRepl.put("#135", " "); // double dagger
        escRepl.put("#136", " "); // modified letter circumflex accent
        escRepl.put("#137", " "); // per mille sign
        escRepl.put("#138", "S"); // Latin capital letter S with caron
        escRepl.put("#139", "\""); // single left-pointing angel quotation mark
        escRepl.put("#140", " "); // Latin capital ligature OE
        escRepl.put("#141", " "); // not used
        escRepl.put("#142", "Z"); // Latin capital letter Z with caron
        escRepl.put("#143", " "); // not used
        escRepl.put("#144", " "); // not used
        escRepl.put("#145", "'"); // left single quotation mark
        escRepl.put("#146", "'"); // right single quotation mark
        escRepl.put("#147", "\""); // left double quotation mark
        escRepl.put("#148", "\""); // right double quotation mark
        escRepl.put("#149", " "); // bullet
        escRepl.put("#150", "-"); // en dash
        escRepl.put("#151", "-"); // em dash
        escRepl.put("#152", "~"); // small tilde
        escRepl.put("#153", " "); // trade mark sign
        escRepl.put("#154", "s"); // Latin small letter s with caron
        escRepl.put("#155", "'"); // single right-pointing angle quotation mark
        escRepl.put("#156", " "); // Latin small ligature oe
        escRepl.put("#157", " "); // not used
        escRepl.put("#158", "z"); // Latin small letter z with caron
        escRepl.put("#159", "Y"); // Latin capital letter Y with diaeresis
        escRepl.put("nbsp", " ");
        escRepl.put("#160", " "); // no-break space
        escRepl.put("iexcl", " ");
        escRepl.put("#161", " "); // inverted exclamation mark
        escRepl.put("cent", " ");
        escRepl.put("#162", " "); // cent sign
        escRepl.put("pound", " ");
        escRepl.put("#163", " "); // pound (currency) sign
        escRepl.put("curren", " ");
        escRepl.put("#164", " "); // currency sign
        escRepl.put("yen", " ");
        escRepl.put("#165", " "); // yen sign
        escRepl.put("brvbar", " ");
        escRepl.put("#166", " "); // broken bar
        escRepl.put("sect", " ");
        escRepl.put("#167", " "); // section sign
        escRepl.put("uml", " ");
        escRepl.put("#168", " "); // diaeresis
        escRepl.put("copy", " ");
        escRepl.put("#169", " "); // copyright sign
        escRepl.put("ordf", " ");
        escRepl.put("#170", " "); // feminine ordinal indicator
        escRepl.put("#171", "\""); // left-pointing double angle quotation mark
        escRepl.put("not", " ");
        escRepl.put("#172", " "); // not sign
        escRepl.put("shy", " ");
        escRepl.put("#173", " "); // soft hyphen
        escRepl.put("reg", " ");
        escRepl.put("#174", " "); // registered sign
        escRepl.put("macr", " ");
        escRepl.put("#175", " "); // macron
        escRepl.put("deg", " ");
        escRepl.put("#176", " "); // degree sign
        escRepl.put("plusmn", " ");
        escRepl.put("#177", " "); // plus-minus sign
        escRepl.put("sup2", " ");
        escRepl.put("#178", " "); // superscript 2
        escRepl.put("sup3", " ");
        escRepl.put("#179", " "); // superscript 3
        escRepl.put("acute", " ");
        escRepl.put("#180", " "); // acute accent
        escRepl.put("micro", " ");
        escRepl.put("#181", " "); // micro sign
        escRepl.put("para", " ");
        escRepl.put("#182", " "); // pilcrow sign
        escRepl.put("middot", " ");
        escRepl.put("#183", " "); // middle dot
        escRepl.put("cedil", " ");
        escRepl.put("#184", " "); // cedilla
        escRepl.put("sup1", " ");
        escRepl.put("#185", " "); // superscript 1
        escRepl.put("ordm", " ");
        escRepl.put("#186", " "); // masculine ordinal indicator
        escRepl.put("raquo", "\"");
        escRepl.put("#187", "\""); // right-pointing double angle quotation mark
        escRepl.put("frac14", " ");
        escRepl.put("#188", " "); // vulgar fraction one quarter
        escRepl.put("frac12", " ");
        escRepl.put("#189", " "); // vulgar fraction one half
        escRepl.put("frac34", " ");
        escRepl.put("#190", " "); // vulgar fraction three quarters
        escRepl.put("iquest", " ");
        escRepl.put("#191", " "); // inverted question mark
        escRepl.put("Agrave", "A");
        escRepl.put("#192", "A"); // A with grave
        escRepl.put("Aacute", "A");
        escRepl.put("#193", "A"); // A with acute
        escRepl.put("Acirc", "A");
        escRepl.put("#194", "A"); // A with circumflex
        escRepl.put("Atilde", "A");
        escRepl.put("#195", "A"); // A with tilde
        escRepl.put("Auml", "A");
        escRepl.put("#196", "A"); // A with diaeresis
        escRepl.put("Aring", "A");
        escRepl.put("#197", "A"); // A with ring above
        escRepl.put("AElig", " ");
        escRepl.put("#198", " "); // latin capital letter AE
        escRepl.put("Ccedil", "C");
        escRepl.put("#199", "C"); // C with cedilla
        escRepl.put("Egrave", "E");
        escRepl.put("#200", "E"); // E with grave
        escRepl.put("Eacute", "E");
        escRepl.put("#201", "E"); // E with acute
        escRepl.put("Ecirc", "E");
        escRepl.put("#202", "E"); // E with circumflex
        escRepl.put("Euml", "E");
        escRepl.put("#203", "E"); // E with diaeresis
        escRepl.put("Igrave", "I");
        escRepl.put("#204", "I"); // I with grave
        escRepl.put("Iacute", "I");
        escRepl.put("#205", "I"); // I with acute
        escRepl.put("Icirc", "I");
        escRepl.put("#206", "I"); // I with circumflex
        escRepl.put("Iuml", "I");
        escRepl.put("#207", "I"); // I with diaeresis
        escRepl.put("ETH", " ");
        escRepl.put("#208", " "); // Latin capital letter Eth
        escRepl.put("Ntilde", "N");
        escRepl.put("#209", "N"); // N with tilde
        escRepl.put("Ograve", "O");
        escRepl.put("#210", "O"); // O with grave
        escRepl.put("Oacute", "O");
        escRepl.put("#211", "O"); // O with acute
        escRepl.put("Ocirc", "O");
        escRepl.put("#212", "O"); // O with circumflex
        escRepl.put("Otilde", "O");
        escRepl.put("#213", "O"); // O with tilde
        escRepl.put("Ouml", "O");
        escRepl.put("#214", "O"); // O with diaeresis
        escRepl.put("times", " ");
        escRepl.put("#215", " "); // mult sign (x)
        escRepl.put("Oslash", "O");
        escRepl.put("#216", "O"); // O with stroke
        escRepl.put("Ugrave", "U");
        escRepl.put("#217", "U"); // U with grave
        escRepl.put("Uacute", "U");
        escRepl.put("#218", "U"); // U with acute
        escRepl.put("Ucirc", "U");
        escRepl.put("#219", "U"); // U with circumflex
        escRepl.put("Uuml", "U");
        escRepl.put("#220", "U"); // U with diaeresis
        escRepl.put("Yacute", "Y");
        escRepl.put("#221", "Y"); // Y with acute
        escRepl.put("THORN", " ");
        escRepl.put("#222", " "); // Latin capital letter Thorn
        escRepl.put("szlig", " ");
        escRepl.put("#223", " "); // Latin small letter sharp s
        escRepl.put("agrave", "a");
        escRepl.put("#224", "a"); // a with grave
        escRepl.put("aacute", "a");
        escRepl.put("#225", "a"); // a with acute
        escRepl.put("acirc", "a");
        escRepl.put("#226", "a"); // a with circumflex
        escRepl.put("atilde", "a");
        escRepl.put("#227", "a"); // a with tilde
        escRepl.put("auml", "a");
        escRepl.put("#228", "a"); // a with diaeresis
        escRepl.put("aring", "a");
        escRepl.put("#229", "a"); // a with ring above
        escRepl.put("aelig", " ");
        escRepl.put("#230", " "); // Latin small lettter ae
        escRepl.put("ccedil", "c");
        escRepl.put("#231", "c"); // c with cedilla
        escRepl.put("egrave", "e");
        escRepl.put("#232", "e"); // e with grave
        escRepl.put("eacute", "e");
        escRepl.put("#233", "e"); // e with acute
        escRepl.put("ecirc", "e");
        escRepl.put("#234", "e"); // e with circumflex
        escRepl.put("euml", "e");
        escRepl.put("#235", "e"); // e with diaeresis
        escRepl.put("igrave", "i");
        escRepl.put("#236", "i"); // i with grave
        escRepl.put("iacute", "i");
        escRepl.put("#237", "i"); // i with acute
        escRepl.put("icirc", "i");
        escRepl.put("#238", "i"); // i with circumflex
        escRepl.put("iuml", "i");
        escRepl.put("#239", "i"); // i with diaeresis
        escRepl.put("eth", " ");
        escRepl.put("#240", " "); // Latin small letter eth
        escRepl.put("ntilde", "n");
        escRepl.put("#241", "n"); // n with tilde
        escRepl.put("ograve", "o");
        escRepl.put("#242", "o"); // o with grave
        escRepl.put("oacute", "o");
        escRepl.put("#243", "o"); // o with acute
        escRepl.put("ocirc", "o");
        escRepl.put("#244", "o"); // o with circumflex
        escRepl.put("otilde", "o");
        escRepl.put("#245", "o"); // o with tilde
        escRepl.put("ouml", "o");
        escRepl.put("#246", "o"); // o with diaeresis
        escRepl.put("divide", " ");
        escRepl.put("#247", " "); // division sign
        escRepl.put("oslash", "o");
        escRepl.put("#248", "o"); // o with stroke
        escRepl.put("ugrave", "u");
        escRepl.put("#249", "u"); // u with grave
        escRepl.put("uacute", "u");
        escRepl.put("#250", "u"); // u with acute
        escRepl.put("ucirc", "u");
        escRepl.put("#251", "u"); // u with circumflex
        escRepl.put("uuml", "u");
        escRepl.put("#252", "u"); // u with diaeresis
        escRepl.put("yacute", "y");
        escRepl.put("#253", "y"); // y with acute
        escRepl.put("thorn", " ");
        escRepl.put("#254", " "); // Latin small letter thorn
        escRepl.put("#255", "y"); // y with diaeresis
        escRepl.put("#256", "A");
        escRepl.put("#257", "a");
        escRepl.put("#258", "A");
        escRepl.put("#259", "a");
        escRepl.put("#260", "A");
        escRepl.put("#261", "a");
        escRepl.put("#262", "C");
        escRepl.put("#263", "c");
        escRepl.put("#264", "C");
        escRepl.put("#265", "c");
        escRepl.put("#266", "C");
        escRepl.put("#267", "c");
        escRepl.put("#268", "C");
        escRepl.put("#269", "c");
        escRepl.put("#270", "D");
        escRepl.put("#271", "d");
        escRepl.put("#272", "D");
        escRepl.put("#273", "d");
        escRepl.put("#274", "E");
        escRepl.put("#275", "e");
        escRepl.put("#276", "E");
        escRepl.put("#277", "e");
        escRepl.put("#278", "E");
        escRepl.put("#279", "e");
        escRepl.put("#280", "E");
        escRepl.put("#281", "e");
        escRepl.put("#282", "E");
        escRepl.put("#283", "e");
        escRepl.put("#284", "G");
        escRepl.put("#285", "g");
        escRepl.put("#286", "G");
        escRepl.put("#287", "g");
        escRepl.put("#288", "G");
        escRepl.put("#289", "g");
        escRepl.put("#290", "G");
        escRepl.put("#291", "g");
        escRepl.put("#292", "H");
        escRepl.put("#293", "h");
        escRepl.put("#294", "H");
        escRepl.put("#295", "h");
        escRepl.put("#296", "I");
        escRepl.put("#297", "i");
        escRepl.put("#298", "I");
        escRepl.put("#299", "i");
        escRepl.put("#300", "I");
        escRepl.put("#301", "i");
        escRepl.put("#302", "I");
        escRepl.put("#303", "i");
        escRepl.put("#304", "I");
        escRepl.put("#305", "i");
        escRepl.put("#306", " "); // IJ
        escRepl.put("#307", " "); // ij
        escRepl.put("#308", "J");
        escRepl.put("#309", "j");
        escRepl.put("#310", "K");
        escRepl.put("#311", "k");
        escRepl.put("#312", "k");
        escRepl.put("#313", "L");
        escRepl.put("#314", "l");
        escRepl.put("#315", "L");
        escRepl.put("#316", "l");
        escRepl.put("#317", "L");
        escRepl.put("#318", "l");
        escRepl.put("#319", "L");
        escRepl.put("#320", "l");
        escRepl.put("#321", "L");
        escRepl.put("#322", "l");
        escRepl.put("#323", "N");
        escRepl.put("#324", "n");
        escRepl.put("#325", "N");
        escRepl.put("#326", "n");
        escRepl.put("#327", "N");
        escRepl.put("#328", "n");
        escRepl.put("#329", "n");
        escRepl.put("#330", "N");
        escRepl.put("#331", "n");
        escRepl.put("#332", "O");
        escRepl.put("#333", "o");
        escRepl.put("#334", "O");
        escRepl.put("#335", "o");
        escRepl.put("#336", "O");
        escRepl.put("#337", "o");
        escRepl.put("#338", " "); // OE
        escRepl.put("#339", " "); // oe
        escRepl.put("#340", "R");
        escRepl.put("#341", "r");
        escRepl.put("#342", "R");
        escRepl.put("#343", "r");
        escRepl.put("#344", "R");
        escRepl.put("#345", "r");
        escRepl.put("#346", "S");
        escRepl.put("#347", "s");
        escRepl.put("#348", "S");
        escRepl.put("#349", "s");
        escRepl.put("#350", "S");
        escRepl.put("#351", "s");
        escRepl.put("#352", "S");
        escRepl.put("#353", "s");
        escRepl.put("#354", "T");
        escRepl.put("#355", "t");
        escRepl.put("#356", "T");
        escRepl.put("#357", "t");
        escRepl.put("#358", "T");
        escRepl.put("#359", "t");
        escRepl.put("#360", "U");
        escRepl.put("#361", "u");
        escRepl.put("#362", "U");
        escRepl.put("#363", "u");
        escRepl.put("#364", "U");
        escRepl.put("#365", "u");
        escRepl.put("#366", "U");
        escRepl.put("#367", "u");
        escRepl.put("#368", "U");
        escRepl.put("#369", "u");
        escRepl.put("#370", "U");
        escRepl.put("#371", "u");
        escRepl.put("#372", "W");
        escRepl.put("#373", "w");
        escRepl.put("#374", "Y");
        escRepl.put("#375", "y");
        escRepl.put("#376", "Y");
        escRepl.put("#377", "Z");
        escRepl.put("#378", "z");
        escRepl.put("#379", "Z");
        escRepl.put("#380", "z");
        escRepl.put("#381", "Z");
        escRepl.put("#382", "z");
        escRepl.put("#383", " "); // r?
        escRepl.put("#8482", " "); // "tm" symbol

    }

    /**
     * Replaces escape code with ascii-safe characters.
     *
     * Note: must call makeEscapeLists() before using this method
     *
     * @param data String containing escapes to be replaced
     * @return data String with escapes replaced
     */
    public static String replaceEscapes(String data) {

        Enumeration<String> escCodeList = escRepl.keys();

        // replace & first, since it could be part of another escape code
        data = data.replaceAll("&amp;", "&");
        System.out.println("replacing &amp; with &");

        while (escCodeList.hasMoreElements()) {

            String nextCode = escCodeList.nextElement();
            String nextCodeFull = "&"+nextCode+";";
            data = data.replaceAll(nextCodeFull, escRepl.get(nextCode));
            System.out.println("replacing "+nextCodeFull+" with "+escRepl.get(nextCode));

        }

        return data;

    }

    /**
     * for testing only
     *
     * @param args
     */
    public static void main(String[] args) {
        
        try {
            BufferedReader inFile = new BufferedReader(
                    new FileReader(args[0]));

            StringBuilder inFileContents = new StringBuilder();
            char[] charBlock = new char[80];
            int numCharsRead;
            while((numCharsRead = inFile.read(charBlock)) > 0) {
                inFileContents.append(charBlock, 0, numCharsRead);
            }
            String fileContents = inFileContents.toString();
            inFile.close();

            makeEscapeLists();
            fileContents = replaceEscapes(fileContents);

            BufferedWriter outFile = new BufferedWriter(
                    new FileWriter(args[1]));

            outFile.write(fileContents);
            outFile.close();

            System.out.println(fileContents);


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error: IO problem");
            System.exit(-1);
        }

    }


    

}
