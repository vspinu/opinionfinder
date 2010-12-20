/*
 * opin/pre/TextExtract.java
 * Alexander Conrad
 * conrada@cs.pitt.edu
 * 11 August 2010
 */

package opin.preprocessor;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;

/**
 * This class removes all markup from HTML and XML files, replacing this markup
 * with whitespace. NOTE: this class is not bytespan-safe!
 *
 * @author conrada
 */
public class TextExtract {

    public static final int BLOCK_SIZE = 80;
    public static String posix_patterns = "\\p{Lower}\\p{Upper}\\p{ASCII}" +
            "\\p{Alpha}\\p{Digit}\\p{Alnum}\\p{Punct}\\p{Graph}\\p{Print}" +
            "\\p{Blank}\\p{Cntrl}\\p{XDigit}\\p{Space}";
    public static String markup_pattern = "(?i)(<[^<>]*>)";
    ///*
    public static String markup_script_pattern =
            "(?i)((<script)([^\"</script>\"])*(</script>))";
    public static String markup_style_pattern =
            "(?i)((<style)([^\"</style>\"])*(</style>))";

    //(<(?:script|style)[^>]+>(?:[^<]+<\/(?:script|style)[^>]+>)?)
    //public static String markup_style_pattern = "(<(?:script|style)[^>]+>(?:[^<]+<(?:script|style)[^>]*>)?)";

    //*/
    /*
    public static String markup_script_pattern =
            "(?i)((<script)([^<>])*(</script>))";
    public static String markup_style_pattern =
            "(?i)((<style)([^<>])*(</style>))";
    //*/
    public static String replacement = " ";

    // for testing purposes only
    public static void main(String[] args) {

        //removeMarkup(args[0], args[1]);

        // extract all ideological debates for easier reading
        String debPath = "E:\\Users\\conrada\\Documents\\" +
                "subjectivity_research\\swapna_debates\\";
        String debPathEdit = "E:\\Users\\conrada\\Documents\\subjectivity_research\\healthcare_editorial_docs\\";
        String debPathBlog = "E:\\Users\\conrada\\Documents\\subjectivity_research\\healthcare_blog_docs\\";
        String debPathDebate = "E:\\Users\\conrada\\Documents\\subjectivity_research\\healthcare_debate_docs\\";
        String debPathDebateLong = "E:\\Users\\conrada\\Documents\\subjectivity_research\\healthcare_long_debate_docs\\";

        /*
        // healthcare debates
        removeMarkup(debPath+"healthcare\\healthcare",
                debPath+"healthcare\\healthcare_clean");
        removeMarkup(debPath+"healthcare\\Healthcare_Reform_Good_Bad_or_Ugly",
                debPath+"healthcare\\Healthcare_Reform_Good_Bad_or_Ugly_clean");
        removeMarkup(debPath+"healthcare\\Is_the_National_Health_Care_Reform_the_answer_to_our_medical_problems",
                debPath+"healthcare\\Is_the_National_Health_Care_Reform_the_answer_to_our_medical_problems_clean");
        removeMarkup(debPath+"healthcare\\Should_America_have_Universal_Healthcare",
                debPath+"healthcare\\Should_America_have_Universal_Healthcare_clean");
        removeMarkup(debPath+"healthcare\\So_you_still_want_Nationalized_Health_Care",
                debPath+"healthcare\\So_you_still_want_Nationalized_Health_Care_clean");
        removeMarkup(debPath+"healthcare\\Universal_Healthcare",
                debPath+"healthcare\\Universal_Healthcare_clean");
        */

        /*
        // existence of god debates
        removeMarkup(debPath+"god\\Do_you_believe_in_God_2",
                debPath+"god\\Do_you_believe_in_God_2_clean");
        removeMarkup(debPath+"god\\Does_God_really_Exist",
                debPath+"god\\Does_God_really_Exist_clean");
        removeMarkup(debPath+"god\\Is_belief_in_God_for_the_GREATER_GOOD",
                debPath+"god\\Is_belief_in_God_for_the_GREATER_GOOD_clean");
        removeMarkup(debPath+"god\\Is_life_believing_in_God_worth_it",
                debPath+"god\\Is_life_believing_in_God_worth_it_clean");
        */

        // first round of new healthcare corpus
        //removeMarkup(debPathEdit+"html\\www.cleveland.com opinion index.ssf 2010 03 latest_health_care_reform_bill.html", debPathEdit+"clean\\www.cleveland.com opinion index.ssf 2010 03 latest_health_care_reform_bill.html");
        //removeMarkup(debPathEdit+"html\\Barack Obama and Tom Daschle should fix health care step by step.htm", debPathEdit+"clean\\Barack Obama and Tom Daschle should fix health care step by step.htm");

        File debPathEditFile = new File(debPathEdit+"html");
        File[] debPathEditList = debPathEditFile.listFiles();
        /*
        for (File file : debPathEditList) {
            try {
                System.out.println("filename: "+file.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        */

        ///*
        for (File file : debPathEditList) {
            try {
                if (file.exists() && file.getName().contains(".htm")) {
                    removeMarkup(file.getCanonicalPath(), debPathEdit+"clean\\"+file.getName());
                }
            } catch (FileNotFoundException e) {
                System.out.println("can't find file: "+file);
            } catch (IOException e) {
                e.printStackTrace();
                //System.exit(-1);
            }
        }

        File debPathBlogFile = new File(debPathBlog+"html");
        File[] debPathBlogList = debPathBlogFile.listFiles();
        for (File file : debPathBlogList) {
            try {
                if (file.exists() && file.getName().contains(".htm")) {
                    removeMarkup(file.getCanonicalPath(), debPathBlog+"clean\\"+file.getName());
                }
            } catch (FileNotFoundException e) {
                System.out.println("can't find file: "+file);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        File debPathDebateFile = new File(debPathDebate+"html");
        File[] debPathDebateList = debPathDebateFile.listFiles();
        for (File file : debPathDebateList) {
            try {
                if (file.exists() && file.getName().contains(".htm")) {
                    removeMarkup(file.getCanonicalPath(), debPathDebate+"clean\\"+file.getName());
                }
            } catch (FileNotFoundException e) {
                System.out.println("can't find file: "+file);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        File debPathDebateLongFile = new File(debPathDebateLong+"html");
        File[] debPathDebateLongList = debPathDebateLongFile.listFiles();
        for (File file : debPathDebateLongList) {
            try {
                if (file.exists() && file.getName().contains(".htm")) {
                    removeMarkup(file.getCanonicalPath(), debPathDebateLong+"clean\\"+file.getName());
                }
            } catch (FileNotFoundException e) {
                System.out.println("can't find file: "+file);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        //*/

        //removeMarkup(debPathEdit+"html\\0001.htm", debPathEdit+"clean\\0001.txt");

    }

    public static String removeMarkup(String inFilename, String outFilename) {

        // read file
        String fileContents = "";
        try {
            BufferedReader inFile = new BufferedReader(
                    new FileReader(inFilename));

            StringBuilder inFileContents = new StringBuilder();
            char[] charBlock = new char[BLOCK_SIZE];
            int numCharsRead;
            while((numCharsRead = inFile.read(charBlock)) > 0) {
                inFileContents.append(charBlock, 0, numCharsRead);
            }
            fileContents = inFileContents.toString();
            inFile.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error: could not read file: "+inFilename);
            System.exit(-1);
        }

        // remove markup
        //  (note: single whitespace for tag; this will mess up bytespans!
        //   either don't do bytespans until after tag removal or
        //   FIX THIS LATER!)
        ///*
        fileContents = fileContents.replaceAll(markup_script_pattern,
                replacement);
        fileContents = fileContents.replaceAll(markup_style_pattern,
                replacement);
        //*/
        fileContents = fileContents.replaceAll(markup_pattern, replacement);

        // write file
        try {
            BufferedWriter outFile = new BufferedWriter(
                    new FileWriter(outFilename));

            outFile.write(fileContents);
            outFile.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error: could not write to file: "+outFilename);
            System.exit(-1);
        }

        return fileContents;

    }



}
