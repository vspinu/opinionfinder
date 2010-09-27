
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
import java.util.HashMap;
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
     * <first word/lemma, indices into gateDefaultContents>
     * note: no need to distinguish between word and lemma, since both 
     *       can appear in same ngram
     * note: using hashtable bc. ~10% faster than hashmap (?)
     */
    private ArrayList<Hashtable<String, ArrayList<Integer>>> wordLemmaTable;

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
        wordLemmaTable = new ArrayList<Hashtable<String, ArrayList<Integer>>>();
        featureFileNames = new ArrayList<String>();

        // for each feature file, read ngramList into memory
        for (File ngramFile : ngramFiles) {

            int tempIdCounter = 0;

            // initialize a new row in entry list, hashtable
            ngramList.add(new ArrayList<Entry>());
            wordLemmaTable.add(new Hashtable<String, ArrayList<Integer>>());
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
                    Entry thisEntry = new Entry(tempIdCounter, true);

                    /*
                    // debug
                    System.out.println("-debug: new entry: id#"+entryIdCounter);
                    //*/

                    String firstWord = "";

                    while (ngramLineScanner.hasNext()) {

                        String attrAndValue = ngramLineScanner.next();
                        Scanner attrValueSplitter = new Scanner(attrAndValue);
                        attrValueSplitter.useDelimiter("=");
                        String attribute = attrValueSplitter.next();
                        String value = attrValueSplitter.next();
                        thisEntry.addAttributes(attribute, value);
                        if (attribute.equals("word1")) {
                            firstWord = value;
                        }

                        /*
                        // debug
                        System.out.println("-debug: adding attribute: "
                                +attribute+", value:"+value);
                        //*/

                    }

                    // add to list
                    ngramList.get(ngramList.size()-1).add(thisEntry);

                    // add to hashtable
                    Hashtable<String, ArrayList<Integer>> thisTable
                            = wordLemmaTable.get(wordLemmaTable.size()-1);
                    // if firstWord already present, add this index
                    if (thisTable.containsKey(firstWord)) {
                        thisTable.get(firstWord).add(tempIdCounter);
                    }
                    else {
                        // if firstWord not already present, create new entry
                        //  and add this index
                        thisTable.put(firstWord, new ArrayList<Integer>());
                        thisTable.get(firstWord).add(tempIdCounter);
                    }

                    tempIdCounter++;
                    entryIdCounter++;

                }
                else {
                    /*
                    // debug
                    System.out.println("-debug: garbage line encountered: "
                            +ngramLine);
                    //*/
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

            System.out.println("featureFinder: processing "+doc.getName());

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
            ArrayList<String> ngramNextPos;
            ArrayList<Integer> ngramBytespanStart;
            ArrayList<Integer> ngramTotal;
            ArrayList<Integer> ngramNext;
            
            ArrayList<ArrayList<Integer>> featureId = new ArrayList<ArrayList<Integer>>();
            ArrayList<ArrayList<Integer>> featureStart = new ArrayList<ArrayList<Integer>>();
            ArrayList<ArrayList<Integer>> featureEnd = new ArrayList<ArrayList<Integer>>();

            // read through entire file for each feature set
            // note: this is (probably) less efficient than checking all
            //       feature sets for each word, but this greatly simplifies
            //       generation of the auto_anns (in my opinion)

            for (int curFeatSet=0; curFeatSet<featureFileNames.size();
                    curFeatSet++) {

                System.out.println("featureFinder: searching for features in "+featureFileNames.get(curFeatSet));

                ngramId = new ArrayList<Integer>();
                ngramNextWord = new ArrayList<String>();
                ngramNextPos = new ArrayList<String>();
                ngramBytespanStart = new ArrayList<Integer>();
                ngramTotal = new ArrayList<Integer>();
                ngramNext = new ArrayList<Integer>();

                featureId.add(new ArrayList<Integer>());
                featureStart.add(new ArrayList<Integer>());
                featureEnd.add(new ArrayList<Integer>());

                /*
                // debug
                System.out.println("-debug: processing doc "+doc.getName()+" with featurefile "+featureFileNames.get(curFeatSet));
                //*/

                for (String gateDefLine : gateDefLines) {

                    // make sure we're not dealing with a comment / empty line
                    if (!(gateDefLine.length()<=0 || gateDefLine.charAt(0)=='#')) {

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

                            // token5: category (category="RB")
                            //  aka: pos
                            String categoryToken = gateDefLineScan.next();
                            Scanner categorySplitter = new Scanner(categoryToken);
                            categorySplitter.useDelimiter("=");
                            categorySplitter.next();
                            String pos = categorySplitter.next();
                            pos = pos.substring(1,pos.length()-1);
                            String simplePos = convertGateDefPos(pos);

                            // token6: kind (kind="word")
                            // note: if kind=="punctuation", then there will
                            //  be no orth!
                            String kindToken = gateDefLineScan.next();
                            Scanner kindSplitter = new Scanner(kindToken);
                            kindSplitter.useDelimiter("=");
                            kindSplitter.next();
                            String kind = kindSplitter.next();
                            kind = kind.substring(1,kind.length()-1);
                            boolean isPunct = false;
                            if (kind.equalsIgnoreCase("punctuation")) {
                                isPunct = true;
                            }

                            // token7: lemma (lemma="nowhere")
                            // *** important token, since many ngrams
                            //     are lemmatized! ***
                            String lemmaToken = gateDefLineScan.next();
                            Scanner lemmaSplitter = new Scanner(lemmaToken);
                            lemmaSplitter.useDelimiter("=");
                            lemmaSplitter.next();
                            String lemma = lemmaSplitter.next();
                            lemma = lemma.substring(1,lemma.length()-1);
                            lemma = lemma.toLowerCase();

                            // token8: length (length="7")
                            String lengthToken = gateDefLineScan.next();
                            Scanner lengthSplitter = new Scanner(lengthToken);
                            lengthSplitter.useDelimiter("=");
                            lengthSplitter.next();
                            String lengthStr = lengthSplitter.next();
                            lengthStr = lengthStr.substring(1,
                                    lengthStr.length()-1);
                            int bytespanLength = Integer.parseInt(lengthStr);

                            // token9: orth (?) (orth="upperInitial")
                            String orth = "";
                            if (!isPunct) {
                                String orthToken = gateDefLineScan.next();
                                Scanner orthSplitter = new Scanner(orthToken);
                                orthSplitter.useDelimiter("=");
                                orthSplitter.next();
                                orth = orthSplitter.next();
                                orth = orth.substring(1,orth.length()-1);
                            }

                            // token10: original_word (string="Nowhere")
                            // *** important token, is original
                            //     unlemmatized word! ***
                            String wordToken = gateDefLineScan.next();
                            Scanner wordSplitter = new Scanner(wordToken);
                            wordSplitter.useDelimiter("=");
                            wordSplitter.next();
                            String word = wordSplitter.next();
                            word = word.substring(1,word.length()-1);
                            // convert to lower case, for standardization
                            word = word.toLowerCase();

                            /*
                            // debug
                            System.out.println("-debug: token: "+idToken+" "+bytespanStart+","+bytespanEnd+" "+stringToken+" "+isTokenToken+" "+category+" "+kind+" "+lemma+" "+bytespanLength+" "+orth+" "+word);
                            //*/

                            // first, process any multi-word ngrams
                            for (int multiNgramIndex = 0;
                                    multiNgramIndex < ngramId.size();
                                    multiNgramIndex++) {

                                String nextNgramItem = ngramNextWord
                                        .get(multiNgramIndex);
                                String nextNgramPos = ngramNextPos
                                        .get(multiNgramIndex);
                                // if current word / lemma matches the next word
                                //  and next pos in the ngram
                                if ((nextNgramItem.equals(word)
                                        || nextNgramItem.equals(lemma)) && 
                                        (nextNgramPos.equals(pos)
                                        || nextNgramPos.equals(simplePos)
                                        || nextNgramPos.equals("anypos"))) {

                                    
                                    /*
                                    // need to know whether we're matching word
                                    //  or lemma
                                    boolean isLemma = true;
                                    if (nextNgramItem.equals(word)) {
                                        isLemma = false;
                                    }
                                    */

                                    // check the id entry; if next == total, add clue
                                    //  to featureId, add ngramBytespanStart to
                                    //  featureStart, add bytespanEnd to featureEnd
                                    if (ngramTotal.get(multiNgramIndex)
                                            == ngramNext.get(multiNgramIndex)) {

                                        /*
                                        // debug
                                        System.out.println("-debug: this word ("+word+") at location "+bytespanStart+" is the last word of multi-word ngram "
                                                +ngramList.get(curFeatSet)
                                                .get(ngramId.get(multiNgramIndex))
                                                .toString());
                                        //*/

                                        featureId.get(curFeatSet)
                                                .add(ngramId.get(multiNgramIndex));
                                        featureStart.get(featureStart.size()-1)
                                                .add(ngramBytespanStart.get(multiNgramIndex));
                                        featureEnd.get(featureEnd.size()-1)
                                                .add(bytespanEnd);

                                        // also, remove these items from the
                                        //  multiword ngram lists
                                        ngramId.remove(multiNgramIndex);
                                        ngramNextWord.remove(multiNgramIndex);
                                        ngramNextPos.remove(multiNgramIndex);
                                        ngramBytespanStart.remove(multiNgramIndex);
                                        ngramTotal.remove(multiNgramIndex);
                                        ngramNext.remove(multiNgramIndex);

                                        // decrement multiNgramIndex?
                                        multiNgramIndex--;


                                    }
                                    else {

                                        /*
                                        // debug
                                        System.out.println("-debug: this word ("+word+") at location "+bytespanStart+" falls within multi-word ngram "
                                                +ngramList.get(curFeatSet)
                                                .get(ngramId.get(multiNgramIndex))
                                                .toString());
                                        //*/

                                        // we're in a multi-word ngram, but
                                        //  not yet at the end, so update
                                        //  for the next word
                                        int thisWordNum = ngramNext.get(multiNgramIndex);
                                        int nextWordNum = thisWordNum+1;
                                        String nextWordStr = ngramList.get(curFeatSet)
                                                .get(ngramId.get(multiNgramIndex))
                                                .getAttributes().get("word"+nextWordNum);
                                        String nextPosStr = ngramList.get(curFeatSet)
                                                .get(ngramId.get(multiNgramIndex))
                                                .getAttributes().get("pos"+nextWordNum);
                                        ngramNextWord.set(multiNgramIndex, nextWordStr);
                                        ngramNextPos.set(multiNgramIndex, nextPosStr);
                                        ngramNext.set(multiNgramIndex,nextWordNum);

                                    }


                                }
                                else {
                                    // next word / lemma does not match pattern
                                    //  for this multi-word ngram, so trash it

                                    /*
                                    // debug
                                    System.out.println("-debug: this word ("+word+") at location "+bytespanStart+" does not match next pattern for multi-word ngram "
                                            +ngramList.get(curFeatSet)
                                            .get(ngramId.get(multiNgramIndex))
                                            .toString());
                                    //*/

                                    ngramId.remove(multiNgramIndex);
                                    ngramNextWord.remove(multiNgramIndex);
                                    ngramNextPos.remove(multiNgramIndex);
                                    ngramBytespanStart.remove(multiNgramIndex);
                                    ngramTotal.remove(multiNgramIndex);
                                    ngramNext.remove(multiNgramIndex);
                                    
                                    multiNgramIndex--;
                                    

                                }

                            }

                            // next, lookup in hash for word and lemma
                            if (wordLemmaTable.get(curFeatSet).containsKey(word) 
                                    || wordLemmaTable.get(curFeatSet).containsKey(lemma)) {

                                ArrayList<Integer> matches = new ArrayList<Integer>();
                                if (wordLemmaTable.get(curFeatSet).get(word) != null) {
                                    
                                    // remove all cases with wrong pos
                                    ArrayList<Integer> potentialMatches =
                                            new ArrayList<Integer>();
                                    potentialMatches.addAll(wordLemmaTable.get(curFeatSet).get(word));

                                    for (int i=0; i<potentialMatches.size(); i++) {

                                        int potentialMatch = potentialMatches.get(i);

                                        String potentialFeatPos = ngramList.get(curFeatSet)
                                                .get(potentialMatch)
                                                .getAttributes().get("pos1");

                                        if (!(potentialFeatPos.equals(pos)
                                                || potentialFeatPos.equals(simplePos)
                                                || potentialFeatPos.equals("anypos"))) {

                                            potentialMatches.remove(i);
                                            i--;

                                        }

                                    }

                                    matches.addAll(potentialMatches);

                                }
                                if (wordLemmaTable.get(curFeatSet).get(lemma) != null) {

                                    // remove all cases with wrong pos
                                    ArrayList<Integer> potentialMatches =
                                            new ArrayList<Integer>();
                                    potentialMatches.addAll(wordLemmaTable.get(curFeatSet).get(lemma));

                                    for (int i=0; i<potentialMatches.size(); i++) {

                                        int potentialMatch = potentialMatches.get(i);

                                        String potentialFeatPos = ngramList.get(curFeatSet)
                                                .get(potentialMatch)
                                                .getAttributes().get("pos1");

                                        if (!(potentialFeatPos.equals(pos)
                                                || potentialFeatPos.equals(simplePos)
                                                || potentialFeatPos.equals("anypos"))) {

                                            potentialMatches.remove(i);
                                            i--;

                                        }

                                    }

                                    matches.addAll(potentialMatches);

                                }

                                // if length, words, pos are all same,
                                //  remove duplicate clues

                                for (int i=0; i<matches.size(); i++) {

                                    int firstMatch = matches.get(i);
                                    HashMap<String, String> firstTable
                                            = ngramList.get(curFeatSet)
                                            .get(firstMatch)
                                            .getAttributes();
                                    int firstLen = Integer.parseInt(firstTable.get("len"));

                                    for (int j=i+1; j<matches.size(); j++) {

                                        int secondMatch = matches.get(j);
                                        HashMap<String, String> secondTable
                                                = ngramList.get(curFeatSet)
                                                .get(secondMatch)
                                                .getAttributes();
                                        int secondLen = Integer.parseInt(secondTable.get("len"));

                                        if (firstLen == secondLen) {

                                            boolean isSame = true;
                                            for (int k=1; k<=firstLen; k++) {

                                                if (!(firstTable.get("word"+k).equals(secondTable.get("word"+k))
                                                        && firstTable.get("pos"+k).equals(secondTable.get("pos"+k)))) {
                                                    isSame = false;
                                                }

                                            }

                                            if (isSame) {
                                                matches.remove(j);
                                                j--;
                                            }

                                        }

                                    }

                                }

                                /*
                                // want to make sure that the same clue entry
                                //  is not included twice (ie, if lemma is same
                                //  as word)
                                // remove duplicates
                                for (int x=0; x<matches.size(); x++) {

                                    int firstInstance = matches.get(x);
                                    HashMap<String, String> firstMap =
                                            ngramList.get(curFeatSet)
                                            .get(matches.get(x)).getAttributes();
                                    for (int y=x+1; y<matches.size(); y++) {

                                        int secondInstance = matches.get(y);
                                        HashMap<String,String> secondMap =
                                            ngramList.get(curFeatSet)
                                            .get(matches.get(y)).getAttributes();
                                        if (firstInstance == secondInstance) {
                                            matches.remove(y);
                                            y--;
                                        }
                                        else if (firstMap.get("word1").equals(secondMap.get("word1"))
                                                && firstMap.get("pos1").equals(secondMap.get("pos1"))
                                                && firstMap.get("len").equals("1")
                                                && secondMap.get("len").equals("1")) {
                                            // some featfiles have repeated len1 clues
                                            // NOTE: we need to expand this to multi-grams
                                            matches.remove(y);
                                            y--;
                                        }

                                    }
                                }
                                */


                                /*
                                // debug
                                System.out.println("-debug: matches for word ("+word+"): ");
                                for (int j=0; j<matches.size(); j++) {
                                    System.out.println("\t"+matches.get(j));
                                    //System.out.println("\t"+ngramList.get(curFeatSet).get(matches.get(j)));
                                }
                                System.out.println("-debug: processing matches...");
                                //*/
                                boolean hasBeenAdded = false;
                                for (int matchNum = 0; matchNum < matches.size();
                                        matchNum++) {
                                    
                                    Entry thisMatch = ngramList.get(curFeatSet)
                                            .get(matches.get(matchNum));
                                    
                                    // read through matches list, check pos, remove
                                    //  entries w/ wrong pos


                                    // if length=1, add to featurelists
                                    if (thisMatch.getAttributes().get("len").equals("1")) {

                                        /*
                                        // debug
                                        System.out.println("-debug: this word ("+word+") at location "+bytespanStart+" matches single-word ngram "
                                                +ngramList.get(curFeatSet)
                                                .get(matches.get(matchNum))
                                                .toString());
                                        //*/

                                        // we don't want to mark a single unigram
                                        //  as a clue more than once, so
                                        //  only add to list if we
                                        //  haven't added one yet at this position
                                        if (!hasBeenAdded) {
                                            hasBeenAdded = true;
                                            featureId.get(curFeatSet)
                                                    .add(matches.get(matchNum));
                                            featureStart.get(curFeatSet)
                                                    .add(bytespanStart);
                                            featureEnd.get(curFeatSet)
                                                    .add(bytespanEnd);
                                        }
                                        
                                    }
                                    else {
                                        // if length>1, add to multi-word ngram lists
                                        
                                        /*
                                        // debug
                                        System.out.println("-debug: this word ("+word+") at location "+bytespanStart+" is first word in multi-word ngram "
                                                +ngramList.get(curFeatSet)
                                                .get(matches.get(matchNum))
                                                .toString());
                                        //*/

                                        ngramId.add(matches.get(matchNum));
                                        ngramNextWord.add(
                                                thisMatch.getAttributes().get("word2"));
                                        ngramNextPos.add(
                                                thisMatch.getAttributes().get("pos2"));
                                        ngramBytespanStart.add(bytespanStart);
                                        ngramTotal.add(
                                                Integer.parseInt(thisMatch.getAttributes().get("len")));
                                        ngramNext.add(2);

                                    }

                                }
                                
                            }



                        }
                        else {

                            // indicates beginning of a new sentence; should we
                            //  clear the ngram arrays?
                            // this should happen automatically, since
                            //  punctuation are treated as tokens, thus will
                            //  disrupt the ngram

                            /*
                            // debug
                            System.out.println("-debug: token: "+idToken+" "+bytespanStart+","+bytespanEnd+" "+stringToken+" "+isTokenToken);
                            //*/
                        }
                    }
                    else {

                        // line was blank / a comment

                        /*
                        // debug
                        System.out.println("-debug: dummy line: "+gateDefLine);
                        //*/

                    }

                }

                // we're done with this feature file, so write its auto_anns

                /*
                // debug
                System.out.println("-debug: auto_anns for "+featureFileNames.get(curFeatSet));
                for (int i=0; i<featureId.get(curFeatSet).size(); i++) {
                    Entry thisEntry = ngramList.get(curFeatSet).get(
                            featureId.get(curFeatSet).get(i));
                    HashMap<String,String> thisEntryHash = thisEntry.getAttributes();
                    int bytespanStart = featureStart.get(curFeatSet).get(i);
                    int bytespanEnd = featureEnd.get(curFeatSet).get(i);
                    String type = "";
                    if (thisEntryHash.containsKey("type")) {
                        type = "type=\""+thisEntryHash.get("type")+"\"\t";
                    }
                    String pattern = "";
                    if (thisEntryHash.containsKey("pattern")) {
                        pattern = "pattern=\""+thisEntryHash.get("pattern")+"\"\t";
                    }
                    String origPats = "";
                    if (thisEntryHash.containsKey("origpats")) {
                        origPats = "origpats=\""+thisEntryHash.get("origpats")+"\"\t";
                    }
                    System.out.println("\t"+thisEntry.toString());
                    System.out.println("\tID"+i+"\t"+bytespanStart+","+bytespanEnd
                            +"\tstring\tmatchfeat\t"+type+pattern+origPats);
                }
                //*/

                // add these features to master feature list?

                // final check for dups: remove anything with same start, end


                // write auto_anns
                File outputAnnDoc = Corpus.getAnnotationFile(
                        ngramFiles.get(curFeatSet).getName(), doc);

                ArrayList<String> featFileContents = new ArrayList<String>();

                for (int i=0; i<featureId.get(curFeatSet).size(); i++) {
                    Entry thisEntry = ngramList.get(curFeatSet).get(
                            featureId.get(curFeatSet).get(i));
                    HashMap<String,String> thisEntryHash = thisEntry.getAttributes();
                    int bytespanStart = featureStart.get(curFeatSet).get(i);
                    int bytespanEnd = featureEnd.get(curFeatSet).get(i);
                    String type = "";
                    if (thisEntryHash.containsKey("type")) {
                        type = "type=\""+thisEntryHash.get("type")+"\"\t";
                    }
                    String pattern = "";
                    if (thisEntryHash.containsKey("pattern")) {
                        pattern = "pattern=\""+thisEntryHash.get("pattern")+"\"\t";
                    }
                    String origPats = "";
                    if (thisEntryHash.containsKey("origpats")) {
                        origPats = "origpats=\""+thisEntryHash.get("origpats")+"\"\t";
                    }
                    if (pattern.equals("") && origPats.equals("")) {
                        // construct pattern manually
                        int patLength = Integer.parseInt(thisEntryHash.get("len"));
                        StringBuilder patternBuilder = new StringBuilder();
                        patternBuilder.append("pattern=\"%");
                        for (int j=1; j<=patLength; j++) {
                            patternBuilder.append(thisEntryHash.get("word"+j)
                                    +"||"+thisEntryHash.get("pos"+j)+"||"
                                    +thisEntryHash.get("stemmed"+j));
                            if (j != patLength) {
                                patternBuilder.append("~");
                            }
                        }
                        patternBuilder.append("%\"");
                        pattern = patternBuilder.toString();
                    }
                    featFileContents.add("ID"+i+"\t"+bytespanStart+","+bytespanEnd
                            +"\tstring\tmatchfeat\t"+type+pattern+origPats);
                }

                WriterUtils.writeLines(Corpus.getAnnotationFile(
                        ngramFiles.get(curFeatSet).getName(), doc), featFileContents);
                
            }

        }
        
    }

    /**
     * This method attempts to match a gate_default pos tag to one of the
     * simpler .tff pos tags.
     *
     * Note: need to also check the original gate_default against .tff pos,
     * since some .tffs use gate_default-style pos.
     *
     * If nothing matches, return ""
     *
     */
    public static String convertGateDefPos(String gateDefPos) {
        
        if (gateDefPos.equals("CD") || gateDefPos.equals("NN")
                || gateDefPos.equals("NNS") || gateDefPos.equals("NP")
                || gateDefPos.equals("NPS")) {
            return "noun";
        } else if (gateDefPos.equals("DT") || gateDefPos.equals("JJ")
                || gateDefPos.equals("JJR") || gateDefPos.equals("JJS")
                || gateDefPos.equals("WDT")) {
            return "adj";
        } else if (gateDefPos.equals("MD") || gateDefPos.equals("VB")
                || gateDefPos.equals("VBD") || gateDefPos.equals("VBG")
                || gateDefPos.equals("VBN") || gateDefPos.equals("VBP")
                || gateDefPos.equals("VBZ")) {
            return "verb";
        } else if (gateDefPos.equals("PP") || gateDefPos.equals("PP$") 
                || gateDefPos.equals("WP") || gateDefPos.equals("WP$")) {
            return "pronoun";
        } else if (gateDefPos.equals("RB") || gateDefPos.equals("RBR")
                || gateDefPos.equals("RBS") || gateDefPos.equals("WRB")) {
            return "adverb";
        }
        
        return "";

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
