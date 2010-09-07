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

/**
 * This class removes all markup from HTML and XML files, replacing this markup
 * with whitespace. NOTE: this class is not bytespan-safe!
 *
 * @author conrada
 */
public class TextExtract {

    public static final int BLOCK_SIZE = 80;
    public static String markup_pattern = "(?i)(<[^<>]*>)";
    public static String markup_script_style_pattern =
            "(?i)([\"<script[^<]*<\\\\script>\"]|[\"<style[^<]*<\\\\style>\"])";
    public static String replacement = " ";

    // for testing purposes only
    public static void main(String[] args) {

        //removeMarkup(args[0], args[1]);

        // extract all ideological debates for easier reading
        String debPath = "E:\\Users\\conrada\\Documents\\" +
                "subjectivity_research\\swapna_debates\\";
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

        // existence of god debates
        removeMarkup(debPath+"god\\Do_you_believe_in_God_2",
                debPath+"god\\Do_you_believe_in_God_2_clean");
        removeMarkup(debPath+"god\\Does_God_really_Exist",
                debPath+"god\\Does_God_really_Exist_clean");
        removeMarkup(debPath+"god\\Is_belief_in_God_for_the_GREATER_GOOD",
                debPath+"god\\Is_belief_in_God_for_the_GREATER_GOOD_clean");
        removeMarkup(debPath+"god\\Is_life_believing_in_God_worth_it",
                debPath+"god\\Is_life_believing_in_God_worth_it_clean");
        
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

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error: could not read file: "+inFilename);
            System.exit(-1);
        }

        // remove markup
        //  (note: single whitespace for tag; this will mess up bytespans!
        //   either don't do bytespans until after tag removal or
        //   FIX THIS LATER!)
        //fileContents = fileContents.replaceAll(markup_script_style_pattern,
        //        replacement);
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
