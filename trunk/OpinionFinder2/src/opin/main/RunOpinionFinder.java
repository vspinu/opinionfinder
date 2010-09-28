package opin.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import opin.util.*;

/**
 * This class executes the main OpinionFinder pipeline. Note: lib/ must be on
 * the classpath!
 *
 * @author conrada
 */
public class RunOpinionFinder {

    public static String sep;
    
    public static String osName;
    public static boolean windows;
    public static boolean win95;

    public static void main(String[] args) {

        osName = System.getProperty("os.name");
        windows = false;
        sep = "/";
        if (osName.split(" ")[0].toLowerCase().equals("windows")) {
            windows = true;
            sep = "\\";
        }
        if (windows && osName.equals("Windows 95")) {
            win95 = true;
            sep = "\\";
        }

        // get command line args; use commons cli?

        String encoding = "UTF-8";
        String featfileList = sep+"config"+sep+"featurefilelist";
        String name = "default";
        String databasePath = "";

        Options options = new Options();

        Option helpOption = OptionBuilder.withDescription("displays this help message")
                .create("help");
        Option encodingOption = OptionBuilder.withArgName("encoding")
                .hasArg()
                .withDescription("document encoding (default is UTF-8)")
                .create("e");
        Option nameOption = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("name to identify this OpinionFinder dataset")
                .create("n");
        Option doclistOption = OptionBuilder.withArgName("doclist")
                .hasArg()
                .withDescription("file containing list of documents to be processed")
                .create("d");
        Option featfileOption = OptionBuilder.withArgName("featfilelist")
                .hasArg()
                .withDescription("file containing list of feature files")
                .create("f");
        Option databaseOption = OptionBuilder.withArgName("databasepath")
                .hasArg()
                .withDescription("path to root database directory")
                .create("db");

        options.addOption(helpOption);
        options.addOption(encodingOption);
        options.addOption(doclistOption);
        options.addOption(featfileOption);
        options.addOption(databaseOption);

        if (args.length == 0) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java RunOpinionFinder", options);
            System.exit(1);
        }

        // by default, assume that the first item is doclist
        String doclistPath = args[0];


        CommandLineParser parser = new PosixParser();
        
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java RunOpinionFinder", options);
                System.exit(0);
            }
            if (cmd.hasOption("e")) {
                encoding = cmd.getOptionValue("e");
            }
            if (cmd.hasOption("d")) {
                doclistPath = cmd.getOptionValue("d");
            }
            if (cmd.hasOption("f")) {
                featfileList = cmd.getOptionValue("f");
            }
            if (cmd.hasOption("name")) {
                name = cmd.getOptionValue("name");
            }
            if (cmd.hasOption("db")) {
                databasePath = cmd.getOptionValue("db");
            }


        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("error while parsing command line args, exiting...");
            System.exit(1);
        }

        // read configuration files

        // copy doclist files to database, if they're not there already
        try {
            Scanner doclistContents = new Scanner(new BufferedReader(new FileReader(doclistPath)));

            while (doclistContents.hasNextLine()) {
                File source = new File(doclistContents.nextLine());
                File destDir = new File(databasePath+sep+"docs"+sep+name);
                if (!source.getCanonicalPath().equals(databasePath+sep+"docs"+sep+name+sep+source.getName())) {
                    FileUtils.copyFileToDirectory(source, destDir);
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("error while copying documents to database dir, exiting...");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while copying documents to database dir, exiting...");
            System.exit(1);
        }

        // build corpus from copied docs

        // *** modules 1 and 2: preprocessing and stemming ***

        // remove html / xml markup (opin.preprocessor.TextExtract.java)
        

        // replace non-ascii chars and escape code with ascii approximations
        //  (opin.preprocessor.EscapeCodeReplacer.java)

        // sentence splitting, tokenization,l pos tagging, and lemmatization
        //  (opin.preprocessor.PreProcess.java)


        // *** module 3: unique feature finder ***
        // this component is no longer with us; was only needed for
        //  intensity classification

        // *** module 4: feature finder ***

        // use ngrams to identify subjectivity clues
        //  (opin.featurefinder.FeatFind.java)


        // *** module 5: shallow parser ***

        // we are no longer using sundance
        // leave annotator in perl for now?


        // *** module 6: source finder ***
        // might not need this / leave in perl?


        // *** module 7: polarity classifier ***

        // leave this in perl for now?


        // *** module 8: direct subjective and speech event classifier ***


        // *** module 9: subjective sentence classifier ***


        // *** module 10: sgml markup ***


        // *** module 11: cleanup ***
        // we don't need this anymore, correct?




    }



}
