
package opin.featurefinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import opin.preprocessor.entity.GateDefaultLine;
import opin.featurefinder.entity.*;
import opin.entity.Corpus;
import opin.io.*;

/**
 * This class identifies features by searching for n-grams within a document.
 *
 * @author conrada
 */
public class FeatFind {

    private static int entryIdCounter = 0;

    /**
     * String identifying the encoding of the gate_default documents
     */
    private String encoding;

    /**
     * File containing ngram clues
     */
    private ArrayList<File> ngramFiles;

    /**
     * 
     * dimensions: feature file, ngram
     *
     * note: each entry could be either a word or lemma, so both hashtables
     *       will need to be consulted
     * note: main attributes:
     *       len: # of words in ngram
     *       word1: first word in ngram
     *       word#: #th word in ngram
     *       stemmed#: whether #th word is stemmed (/ lemmatized?), y/n
     *       pos#: part of speech for #th word ("anypos" = not constrained)
     *       type: ?
     *
     */
    private ArrayList<ArrayList<Entry>> ngramList;

    /**
     * dimensions: document #, gate_default row #, gate_default col #
     *
     * gate_default example lines:
        ID1 0,67 string GATE_SENTENCE
        ID2 0,7 string GATE_TOKEN category="RB" kind="word" lemma="nowhere" length="7" orth="upperInitial" string="Nowhere"
     * format: id# bytespanStart bytespanEnd category kind lemma length orth string
     *
     * all data in gate_default should be stored here
     *
     * NOTE: in the future, we could just pass this datastructure directly from
     *       the preprocessor, to remove the need for additional disk access
     */
    private ArrayList<ArrayList<GateDefaultLine>> gateDefaultContents;

    // 

    /**
     *
     * dimension: feature file
     *
     * <word/lemma, index into gateDefaultContents>
     * note: no need to distinguish between word and lemma, since both 
     *       can appear in same ngram
     * note: using hashtable bc. ~10% faster than hashmap (?)
     */
    private ArrayList<Hashtable<String, Integer>> wordLemmaTable;

    /**
     * stores names for feature files, to facilitate generation of auto_anns
     * for each feature file later.
     */
    private ArrayList<String> featureFileNames;
    

    public FeatFind(ArrayList<File> _ngramFiles, String _encoding) {

        ngramFiles = _ngramFiles;
        encoding = _encoding;
        
        gateDefaultContents = new ArrayList<ArrayList<GateDefaultLine>>();
        ngramList = new ArrayList<ArrayList<Entry>>();
        wordLemmaTable = new ArrayList<Hashtable<String, Integer>>();
        featureFileNames = new ArrayList<String>();

        // for each feature file, read ngramList into memory
        for (File ngramFile : ngramFiles) {

            // initialize a new row in entry list, hashtable
            ngramList.add(new ArrayList<Entry>());
            wordLemmaTable.add(new Hashtable<String, Integer>());
            featureFileNames.add(ngramFile.getName());
            ///*
            // debug
            System.out.println("-debug: processing featureFile "
                    +ngramFile.getName());
            //*/

            ArrayList<String> ngramLines = ReaderUtils.readLines(ngramFile);
            for (String ngramLine : ngramLines) {

                // each line is a feature
                // each token in the line is an attribute
                // if line starts with #, ignore
                if (!(ngramLine.length()==0 || ngramLine.charAt(0)=='#')) {


                    Scanner ngramLineScanner = new Scanner(ngramLine);
                    Entry thisEntry = new Entry(entryIdCounter, true);

                    ///*
                    // debug
                    System.out.println("-debug: new entry: id#"+entryIdCounter);
                    //*/

                    entryIdCounter++;

                    while (ngramLineScanner.hasNext()) {

                        String attrAndValue = ngramLineScanner.next();
                        Scanner attrValueSplitter = new Scanner(attrAndValue);
                        attrValueSplitter.useDelimiter("=");
                        String attribute = attrValueSplitter.next();
                        String value = attrValueSplitter.next();
                        thisEntry.addAttributes(attribute, value);
                        
                        // debug
                        System.out.println("-debug: adding attribute: "
                                +attribute+", value:"+value);
                        

                    }

                    ngramList.get(ngramList.size()-1).add(thisEntry);
                    
                }
                else {
                    /*
                    // debug
                    System.out.println("-debug: garbage line encountered: "
                            +ngramLine);
                    */
                }

            }
            
        }

    }



    /**
     *
     * note: in the future, we should change return type so that the next
     *       module doesn't need to perform disk access to get at data
     *
     * @param corpus
     */
    public void process(Corpus corpus) {

        ArrayList<File> docs = corpus.getDocs();

        // process each file
        for (File doc : docs) {

            // find gate_default
            File docGateDef = Corpus.getAnnotationFile("gate_default", doc);

            // read gate_default (1 line per word)
            /* gate_default example lines:
                    ID1 0,67 string GATE_SENTENCE
                    ID2 0,7 string GATE_TOKEN category="RB" kind="word" lemma="nowhere" length="7" orth="upperInitial" string="Nowhere"
            * format: id# bytespanStart bytespanEnd category kind lemma length orth string
            */
            ArrayList<String> gateDefLines = ReaderUtils.readLines(docGateDef);

            /* temp arrays for tracking multi-word ngrams
             * when the first word of a multi-word ngram is encountered,
             * the next word is stored in ngramNextWord, and the id for the
             * corresponding word's entry is stored in ngramId\
             * Also, start of corresponding bytespan of first word should be 
             * stored in ngramBytespanStart
             */
            ArrayList<Integer> ngramId;
            ArrayList<String> ngramNextWord;
            ArrayList<Integer> ngramBytespanStart;

            // read through entire file for each feature set
            // note: this is (probably) less efficient than checking all
            //       feature sets for each word, but this greatly simplifies
            //       generation of the auto_anns (in my opinion)

            for (int curFeatSet=0; curFeatSet<featureFileNames.size();
                    curFeatSet++) {

                ngramId = new ArrayList<Integer>();
                ngramNextWord = new ArrayList<String>();
                ngramBytespanStart = new ArrayList<Integer>();

                for (String gateDefLine : gateDefLines) {

                    Scanner gateDefLineScan = new Scanner(gateDefLine);

                    // token1: id (trash?)
                    String idToken = gateDefLineScan.next();

                    // token2: startBytespan,endBytespan
                    String bytespanToken = gateDefLineScan.next();
                    Scanner bytespanSplitter = new Scanner(bytespanToken);
                    bytespanSplitter.useDelimiter(",");
                    int bytespanStart = bytespanSplitter.nextInt();
                    int bytespanEnd = bytespanSplitter.nextInt();

                    // token3: "string"?
                    String stringToken = gateDefLineScan.next();

                    // token4: "GATE_SENTENCE" or "GATE_TOKEN"
                    // note: if "SENTENCE", no more tokens!
                    String isTokenToken = gateDefLineScan.next();
                    if (isTokenToken.equalsIgnoreCase("GATE_TOKEN")) {

                        // token5: category
                        String categoryToken = 






                    }
                    else {

                        // indicates beginning of a new sentence; should we
                        //  clear the ngram arrays?

                    }


                }


            }



        }



        
    }




    public static void main(String[] args) {

        // contains list of documents to process
        File doclistFile = null;
        // contains list of ngram feature files
	ArrayList<File> ngramFiles = new ArrayList<File>();
	String encoding="UTF-8";

	if (args.length >= 2) {

            // process required arguments
            doclistFile = new File(args[0]);
            File ngramFileList = new File(args[1]);
           
            if(!doclistFile.exists()) {
                System.out.println("Doclist file "+args[0]+" does not exist!");
		System.exit(-1);
            }
            if(!ngramFileList.exists()) {
                System.out.println("N-gram filelist "+args[1]+" does not exist!");
		System.exit(-1);
            }
            // second arg contains list of paths to feature files
            ArrayList<String> ngramFileListStr = ReaderUtils.readLines(ngramFileList);
            for (String ngramFile : ngramFileListStr) {
                ngramFiles.add(new File(ngramFile));
            }


            // process additional arguments
            int i=2;
            while(i<args.length) {
                if(args[i].equals("-e")) {
                    if(i+1<args.length) {
                        encoding=args[i+1];
                        i+=2;
                    }
                    else{
                        System.out.println("Encoding value is missing !");
			System.exit(-1);
                    }
		}
		else {

                    System.out.println("Unrecognized argument: "+args[i]);
                    System.out.println("Usage: <doclist> <ngram_file> (-e <encoding>)");
                    System.exit(-1);
                    
                    /*
                    // assume it's another ngram file
                    ngramFile = new File(args[i]);
                    ngramFiles.add(ngramFile);
                    i++;
                    if(!ngramFile.exists()) {
                        System.out.println("N-gram file "+args[i]
                                +" does not exist!");
                        System.exit(-1);
                    }
                    /*
                    System.out.println("Unknown argument "+args[i]+" !");
                    System.exit(-1);
                    */
		}
            }
	}

	else {
            // user didn't provide required arguments
            System.out.println("Usage: <doclist> <ngram_file> (-e <encoding>)");
            System.exit(-1);
        }

        /*
        //debug
        System.out.println("-debug: ngram paths: ");
        for (File ngramPath : ngramFiles) {
            System.out.println("\t"+ngramPath.getAbsolutePath());
        }
        */

        Corpus corpus = new Corpus(doclistFile);
        FeatFind featureFinder = new FeatFind(ngramFiles, encoding);
        featureFinder.process(corpus);

    }


}
