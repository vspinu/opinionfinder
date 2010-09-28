package opin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 *
 * <p>This is loosely based on
 * <a href=http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=4>the code here</a>.
 * I modified it to assume that any Windows besides Windows 95 will have cmd. Also obvious changes
 * have been made to encapsulate this in the Execute object, and to allow streaming input.</p>
 * <h2>Caveats</h2>
 * <ul>
 * <li>Assumes either "linux-like," "windows 95," or "any other windows" OS... This means
 * that, unless windows or windows 95 is detected using System.getProperty("os.name"),
 * we implode the argument array with some spaces and try to Process.getRuntime().exec(arglist).
 * If it's a windows 95, we first call "command.com," and for any other windows - "cmd.exe."
 * Then we similarly stitch arguments together to be run in the Windows command line.</li>
 * <li>This means that a command like "ls" or "dir" will only work in the respective
 * operating system.</li>
 * <li>However, if you have an installed application that is runnable from each type of command line in
 * the same manner, this should work just fine.</li>
 * <li>Changing process workign directory is done using setDir and getDir methods.</li>
 * </ul>
 *
 * <p> Please see http://code.google.com/p/hills/wiki/usingUtilExecute for examples. </p>
 *
 * Last Modified: Jul 14, 2010<br>
 * Version: 1.5<br>
 * Created: Jun 21, 2010<br>
 * Author: Katie Kuksenok (katie.kuksenok@gmail.com)
 *
 * @author Katie
 */
public class Execute {

    /* OS Setup */
    private String osName;
    private boolean windows;
    private boolean win95;

    /* Streams */
    private PrintStream infoStream;
    private PrintStream outStream;
    private PrintStream errStream;
    private InputStream inStream;
    private PrintStream inPrintStream;

    /* Prefixes */
    private String PREFIX_INFO = "INFO> ";

    /**
     * Sets the prefix for informative output (which is output from
     * this program, rather than anythign it might call) to something other
     * that the default <code><strong>INFO&gt;</strong></code>
     * @param pre New prefix
     */
    public void setPrefixInfo(String pre) {
        PREFIX_INFO = pre;
    }

    /**
     * Gets the prefix for informative output (which is output from
     * this program, rather than anythign it might call).
     * Defualt value is <code><strong>INFO&gt;</strong></code>
     * @return Current prefiz
     */
    public String getPrefixInfo() {
        return PREFIX_INFO;
    }
    private String PREFIX_OUT = "OUT > ";

    /**
     * Sets the prefix for standard output (which is output from
     * a program that this program would call) to something other
     * that the default <code><strong>OUT&nbsp;&gt;</strong></code>
     * @param pre New prefix
     */
    public void setPrefixOut(String pre) {
        PREFIX_OUT = pre;
    }

    /**
     * Gets the prefix for standard output (which is output from
     * a program that this program would call).
     * Defualt value is <code><strong>OUT&nbsp;&gt;</strong></code>
     * @return Current prefiz
     */
    public String getPrefixOut() {
        return PREFIX_OUT;
    }
    private String PREFIX_ERR = "ERR > ";

    /**
     * Sets the prefix for standard error (which is error output from
     * a program that this program would call) to something other
     * that the default <code><strong>ERR&nbsp;&gt;</strong></code>
     * @param pre New prefix
     */
    public void setPrefixErr(String pre) {
        PREFIX_ERR = pre;
    }

    /**
     * Gets the prefix for standard error (which is error output from
     * a program that this program would call).
     * Defualt value is <code><strong>ERR&nbsp;&gt;</strong></code>
     * @return Current prefix
     */
    public String getPrefixErr() {
        return PREFIX_ERR;
    }
    private String PREFIX_IN = "IN  > ";

    /**
     * Sets the prefix for echoing input (which is routed to
     * the program that this program would call) to something other
     * that the default <code><strong>IN&nbsp;&nbsp;&gt;</strong></code>
     * @param pre New prefix
     */
    public void setPrefixIn(String pre) {
        PREFIX_IN = pre;
    }

    /**
     * Gets the prefix for echoing input (which is routed to
     * the program that this program would call).
     * Defualt value is <code><strong>IN&nbsp;&nbsp;&gt;</strong></code>
     * @return Current prefix
     */
    public String getPrefixIn() {
        return PREFIX_IN;
    }
    private boolean usePrefixes;

    /* Gobblers and Feeders */
    private StreamGobbler errorGobbler;
    private StreamGobbler outputGobbler;
    private StreamFeeder inputFeeder;
    private Process proc;
    private File workingDir;

    public File getWorkingDir() {
        return workingDir;
    }

    /**
     * Set workign directory to a file
     * @param wd
     */
    public void setWorkingDir(File wd) {
        workingDir = wd;
        printInfo("Working directory: " + workingDir.getAbsolutePath());
    }

    /**
     * Set working directory to a file constructed from a string
     * @param wd
     */
    public void setWorkingDir(String wd) {
        setWorkingDir(new File(wd));
    }

    /**
     * step up a directory
     * @param fold
     */
    public void cd(String fold) {
        String path = workingDir.getAbsolutePath();
        setWorkingDir(new File(path + "/" + fold));
    }

    /**
     * <p>To instantiate, we need to specify a stream where the Execute can
     * put information about what it is doing and how it feels about it, including
     * exception notification, and writing out what values programs exited with.
     * If this is null, a "silent" mode is taken on.</p>
     * <p>Since output, error, and info streams (or any combination thereof)
     * can, in the end, be the same stream, it may be helpful to use prefixes in front of every
     * line of output. Whether to use prefixes is also specified at instantiation
     * as a boolean flag. For further prefix manipulation, see: <strong>setPrefixIn(String)</strong>,
     * <strong>setPrefixInfo(String)</strong>, <strong>setPrefixOut(String)</strong>,
     * and <strong>setPrefixErr(String)</strong> to modify respective
     * prefixes.</p>
     *
     * @param infoStream Where to put info
     * @param usePrefixes Whether to use prefixes in output to distinguish streams
     */
    public Execute(PrintStream info, boolean usePrefixes) {
        osName = System.getProperty("os.name");
        windows = false;
        if (osName.split(" ")[0].toLowerCase().equals("windows")) {
            windows = true;
        }
        if (windows && osName.equals("Windows 95")) {
            win95 = true;
        }
        this.usePrefixes = usePrefixes;
        setOutputStream(null);
        setErrorStream(null);
        setInputStream(null);
        setInfoStream(info);
        workingDir = null;
    }

    /**
     * Change output stream; if this is null, then system call output
     * will be silenced.
     * @param ps Stream pointer or null
     */
    public void setOutputStream(PrintStream ps) {
        outStream = ps;
    }

    /**
     * Change information stream; if this is null, then Execute.java's output
     * will be silenced.
     * @param ps Stream pointer or null
     */
    public void setInfoStream(PrintStream ps) {
        infoStream = ps;
    }

    /**
     * Change error stream; if this is null, then system call error
     * will be silenced.
     * @param ps Stream pointer or null
     */
    public void setErrorStream(PrintStream ps) {
        errStream = ps;
    }

    /**
     * Change input stream into the system call
     * @param ps Stream pointer or null
     */
    public void setInputStream(InputStream is) {
        inStream = is;
    }

    /**
     * Get output stream pointer
     * @return stream
     */
    public PrintStream getOutputStream() {
        return outStream;
    }

    /**
     * Get info stream pointer
     * @return stream
     */
    public PrintStream getInfoStream() {
        return infoStream;
    }

    /**
     * Get error stream pointer
     * @return stream
     */
    public PrintStream getErrorStream() {
        return errStream;
    }

    /**
     * Get input stream pointer
     * @return stream
     */
    public InputStream getInputStream() {
        return inStream;
    }

    /**
     * Glues multiple parts of the command into one using spaces.
     * Equivalent to calling run(String), where string is thegiven array with
     * all elements separated by spaces.
     * 
     * @deprecated July, 2010 by Katie; careful inspection shows that proc.exec()
     * uses stringTokenizer/etc in Windows to get at the argument. Therefore,
     * parrins as an array, gluing back together, and then tokenizing again seems like
     * a terrible thing to be supporting.
     * @param command
     * @return result of calling run with this command
     */
    public boolean run(String[] command) {
        StringBuilder com = new StringBuilder("");
        for (int i = 0; i < command.length; i++) {
            if (i > 0) {
                com.append(' ');
            }
            com.append(command[i]);
        }
        return run(com.toString());
    }

    /**
     * Equivalent to the call run(command, false, out, err, in)
     * where out, err, and in are lastused respective streams. If no streams were
     * ever set, these will all be null.
     * @param command to run
     * @return true iff hat run call returns true
     */
    public boolean run(String command) {
        return run(command, false, outStream, errStream, inStream);
    }
    /**
     * Equivalent to the call run(command, addDotSlash, out, err, in)
     * where out, err, and in are lastused respective streams. If no streams were
     * ever set, these will all be null.
     * @param command to run
     * @param addDotSlash whether to add ./ in front to allow running on Linux
     * @return true iff hat run call returns true
     */
    public boolean run(String command, boolean addDotSlash) {
        return run(command, addDotSlash, outStream, errStream, inStream);
    }

    /**
     *
     * @param command
     * @param addDotSlash
     * @param out
     * @param err
     * @param in
     * @return
     */
    public boolean run(String command,
            boolean addDotSlash,
            PrintStream out,
            PrintStream err,
            InputStream in) {
        setOutputStream(out);
        setErrorStream(err);
        setInputStream(in);

        if (out == null || err == null) {
            printInfo("Warning:" + (out == null ? " no output stream defined!" : "")
                    + (err == null ? " no error stream defined!" : "") );
            printInfo("This may cause programs to behave badly. Providing streams is highly encouraged.");
        }

        if (command == null) {
            printInfo("No command given.");
            return false;
        }
        boolean startSuccess = true;
        printInfo("> > "+command);
        if (windows) {
            String cmd = "cmd.exe";
            if (win95) {
                cmd = "command.com";
            }
            startSuccess = startGobblers(new String[]{cmd, "/C", command});
            /* this ignores first 3 lines of output, which happens to be a MS copyright statemnt */
        } else {
            if(addDotSlash)
                command = "./" + command;
            startSuccess = startGobblers(command.split(" "));
        }

        if (startSuccess) {
            if (inStream == null && inputFeeder != null) {
                inputFeeder.end();
            }
            int exitVal = -1;
            try {
                exitVal = proc.waitFor();
            } catch (InterruptedException ex) {
                printInfo("Exception in run");
                printInfo(ex.getMessage());
            }
            printInfo(command+" exited with value " + exitVal);
            stopGobblers();
        } else {
            printInfo("Failed to execute " + command);
        }
        return true;
    }

    /**
     * Start up all the streams, and open the required process.
     * @param args - command; different for windows and linux.
     * @return true iff able to open all streams we wanted to open, and able to start the process
     */
    private boolean startGobblers(String[] args) {
        try {
            Runtime rt = Runtime.getRuntime();
            proc = rt.exec(args, null, workingDir);
            inPrintStream = new PrintStream(proc.getOutputStream(), true);
            errorGobbler = new StreamGobbler(proc.getErrorStream(),
                    errStream,
                    usePrefixes ? PREFIX_ERR : "");
            outputGobbler = new StreamGobbler(proc.getInputStream(),
                    outStream,
                    usePrefixes ? PREFIX_OUT : "");
            errorGobbler.start();
            outputGobbler.start();

            if (inStream != null) {
                inputFeeder = new StreamFeeder(inPrintStream,
                        inStream,
                        infoStream,
                        usePrefixes ? PREFIX_INFO : "",
                        usePrefixes ? PREFIX_IN : "");
                inputFeeder.start();
            }

            return true;
        } catch (IOException ioe) {
            printInfo("Exception in startGobblers");
            printInfo(ioe.getMessage());

            return false;
        }
    }

    /**
     * Cut all the streams
     */
    private void stopGobblers() {
        errorGobbler = null;
        outputGobbler = null;
        if (inputFeeder != null) {
            inputFeeder.end();
        }
        inputFeeder = null;
    }

    /**
     * Prints out status updates
     * @param msg to print
     */
    private void printInfo(String msg) {
        if (infoStream != null) {
            infoStream.println((usePrefixes ? PREFIX_INFO : "") + msg);
        }
    }

    /**
     * Testing and examples as shown on http://code.google.com/p/hills/wiki/usingUtilExecute
     * @param args
     */
    public static void main(String args[]) throws FileNotFoundException {
        /* set up the directories */
        String dirC = "C:\\Users\\Katie\\Documents\\NetBeansProjects\\Libraries\\SVM-light";
        String dirJava = "C:\\Users\\Katie\\Documents\\NetBeansProjects\\Libraries\\SVM-light";
        String dirFiles = "C:\\Users\\Katie\\Documents\\NetBeansProjects\\Libraries\\SVM-light";
        /* set up the programs */
        String programC = "svm_classify";
        String programJava1 = "HelloWorld";
        String programJava2 = "Echo";
        /* output file */
        String oFile = "outtest.txt";
        /* input file */
        String iFile = "intest.txt";

        /* Streams */
        PrintStream out = new PrintStream(new FileOutputStream(dirFiles+"/"+oFile));
        PrintStream intermediate = new PrintStream(new FileOutputStream(dirFiles+"/"+iFile));
        PrintStream err = System.out;
        PrintStream info = System.out;
        InputStream in = null;

        /* Create an Execute instance */
        Execute exe = new Execute(info, true);
        exe.setPrefixOut("");

        /* Test out how the directory-changer works... */
        exe.run("dir", false, out, err, in);
        exe.setWorkingDir(dirC);
        exe.run("dir", false);

        /* Run a C program... */
        exe.run(programC, true);

        /* run Java programs... */
        exe.setWorkingDir(dirJava);
        exe.run("dir", false);
        exe.run("javac "+programJava1+".java", false);
        exe.run("java "+programJava1, false, intermediate, err, in);
        intermediate.close();
        in = new FileInputStream(dirFiles+"/"+iFile);
        exe.run("java "+programJava2, false, out, err, in);
    }
}

/**
 * <p>This is based on
 * http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=4</p>
 * <p>The idea here is to "gobble up" output and error streams independently of each other.</p>
 */
class StreamGobbler extends Thread {

    InputStream inStream;
    PrintStream outStream;
    String prefix;
    int ignore;

    /**
     * Create a new gobbler
     * @param inStream where words are coming from (the program exec'ed)
     * @param outSteam where they should go (System.out, for example)
     * @param prefix What prefix to use in front of all lines sent to the outStream
     */
    StreamGobbler(InputStream inStream, PrintStream outSteam, String prefix) {
        this.inStream = inStream;
        this.prefix = prefix;
        this.outStream = outSteam;
        ignore = 0;
    }

    /**
     * Start gobbling!
     */
    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(inStream);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (ignore == 0 && outStream != null) {
                    outStream.println(prefix + line);
                }
                if (ignore > 0) {
                    ignore--;
                }
            }

        } catch (IOException ioe) {
            if (outStream != null) {
                outStream.println(prefix + ioe.getMessage());
            }
        }
    }

    /**
     * Ignore the next few lines of the output
     * @param amt How many lines to ignore?
     */
    public void ignore(int amt) {
        ignore = amt;
    }
}

/**
 * <p>Inspired by the StreamGobbler, the StreamFeeder helps with feeding input from a stream to a program
 * being executed.</p>
 * @author Katie
 */
class StreamFeeder extends Thread {

    private PrintStream inPrintStream;
    private InputStream inStream;
    private PrintStream infoStream;
    private String prefixInfo;
    private String prefixIn;

    /**
     * Create a new StreamFeeder
     * @param inPrintStream Where to write to?
     * @param inStream Where to write from?
     * @param infoStream Where to give information?
     * @param prefix What is the prefix?
     */
    public StreamFeeder(PrintStream inPrintStream,
            InputStream inStream,
            PrintStream infoStream,
            String prefixInfo,
            String prefixIn) {
        this.inPrintStream = inPrintStream;
        this.inStream = inStream;
        this.infoStream = infoStream;
        this.prefixInfo = prefixInfo;
        this.prefixIn = prefixIn;
    }

    /**
     * Feed a line
     * @param line to feed
     */
    private void feed(String line) {
        inPrintStream.println(line);
    }

    /**
     * Close the feeder
     */
    public void end() {
        inPrintStream.close();
    }

    /**
     * Begins feeding
     */
    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(inStream);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (infoStream != null) {
                    infoStream.println(prefixIn + line);
                }
                feed(line);
            }
            end();
        } catch (IOException ioe) {
            if (infoStream != null) {
                infoStream.println(prefixInfo + ioe.getMessage());
            }
        }
    }
}
