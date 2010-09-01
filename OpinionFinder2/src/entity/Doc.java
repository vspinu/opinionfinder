package entity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.DCONST;


public class Doc implements Serializable{

	private String content;
	private File correspondingRawFile;
	private ArrayList<File> correspondingAutoAnns;
	private File correspondingGateDefault;
	private File correspondingManAnn;
	private File correspondingSentClassFile;
	private File correspondingGoldClassFile;
	private File correspondingSentSplitFile;
	ArrayList<Sentence> sentences;
	private int docID;
	private String dir;
	private String file;
	
	public Doc(String content) {
		this.content=content;
		this.correspondingAutoAnns=new ArrayList<File>();
	}
	
	public String getRawSpan(int spanS, int spanE){
		return content.substring(spanS, spanE);
	}

	public File getCorrespondingRawFile() {
		return correspondingRawFile;
	}

	public void setCorrespondingRawFile(File correspondingRawFile) {
		this.correspondingRawFile = correspondingRawFile;
	}

	public ArrayList<File> getCorrespondingAutoAnns() {
		return correspondingAutoAnns;
	}

	public void setCorrespondingAutoAnns(ArrayList<File> correspondingAutoAnns) {
		this.correspondingAutoAnns = correspondingAutoAnns;
	}

	public void addToCorrespondingAutoAnns(File correspondingAutoAnn) {
		this.correspondingAutoAnns.add(correspondingAutoAnn);
	}
	
	public File getCorrespondingGateDefault() {
		return correspondingGateDefault;
	}

	public void setCorrespondingGateDefault(File correspondingGateDefault) {
		this.correspondingGateDefault = correspondingGateDefault;
	}

	public File getCorrespondingManAnn() {
		return correspondingManAnn;
	}

	public void setCorrespondingManAnn(File correspondingManAnn) {
		this.correspondingManAnn = correspondingManAnn;
	}

	public File getCorrespondingSentClassFile() {
		return correspondingSentClassFile;
	}

	public void setCorrespondingSentClassFile(File correspondingSentClassFile) {
		this.correspondingSentClassFile = correspondingSentClassFile;
	}

	public File getCorrespondingGoldClassFile() {
		return correspondingGoldClassFile;
	}

	public void setCorrespondingGoldClassFile(File correspondingGoldClassFile) {
		this.correspondingGoldClassFile = correspondingGoldClassFile;
	}

	public File getCorrespondingSentSplitFile() {
		return correspondingSentSplitFile;
	}

	public void setCorrespondingSentSplitFile(File correspondingSentSplitFile) {
		this.correspondingSentSplitFile = correspondingSentSplitFile;
	}

	public ArrayList<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(ArrayList<Sentence> sentences) {
		this.sentences = sentences;
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return docID+" : "+dir+"/"+file;
	}
	
}
