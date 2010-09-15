package opin.main;

/**
 * This class executes the main OpinionFinder pipeline.
 *
 * @author conrada
 */
public class RunOpinionFinder {


    public static void main(String[] args) {

        // get command line args; use commons cli?

        // read configuration files

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
