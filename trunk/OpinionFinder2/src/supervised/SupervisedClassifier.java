package supervised;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;



public class SupervisedClassifier {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		boolean cv=false;
		File dataFile=null;
		File trainFile=null;
		File testFile=null;

		
		if(args.length==0){
			System.err.println("Usage: java SupervisedClassifier trainData (testData)");
			System.exit(-1);
		}
		else if(args.length==1){
			cv=true;
			dataFile = new File(args[0]);
			if(!dataFile.exists()){
				System.err.println("Data File does not exist!");
				System.exit(-1);
			}
		}
		else{
			trainFile = new File(args[0]);
			if(!trainFile.exists()){
				System.err.println("Train File does not exist!");
				System.exit(-1);
			}
			testFile = new File(args[1]);
			if(!testFile.exists()){
				System.err.println("Test File does not exist!");
				System.exit(-1);
			}
		}
		
		
		
		try{
			
			Evaluation eval;
			ArrayList<Double> predictions;
			
			if(cv){
				
				FileReader reader = new FileReader(dataFile);
				Instances data = new Instances(reader);
				data.setClassIndex(data.numAttributes() - 1);
				reader.close();		
											
				NaiveBayes naive = new NaiveBayes();
			    naive.setUseSupervisedDiscretization(true);		
			    
			    eval=new Evaluation(data);
			    
			    predictions = crossValidate(eval, naive,data,10,new Random(0));
			    
			    System.out.println(eval.toSummaryString(true));
			    System.out.println("\n\n\n\n");
			    System.out.println(eval.toClassDetailsString());
				writeEvaluation(new File("cv_eval.txt"), eval, predictions, data);
							
			}
			else{
				FileReader reader = new FileReader(trainFile);
				Instances train = new Instances(reader);
				train.setClassIndex(train.numAttributes() - 1);
				reader.close();
				
				reader = new FileReader(testFile);
				Instances test = new Instances(reader);
				test.setClassIndex(test.numAttributes() - 1);
				reader.close();
			    
				Instances filteredTrain = filterAttribute(train, "first");
				Instances filteredTest = filterAttribute(test, "first");
											
				NaiveBayes naive = new NaiveBayes();
			    naive.setUseSupervisedDiscretization(true);		
			    
			    eval=new Evaluation(filteredTrain);
			    
			    predictions = validate(eval, naive, filteredTrain, filteredTest);
			    
			    System.out.println(eval.toSummaryString(true));
			    System.out.println("\n\n\n\n");
			    System.out.println(eval.toClassDetailsString());
				writeEvaluation(new File("eval.txt"), eval, predictions, test);
				
				
			}			
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	

	private static void writeEvaluation(File file, Evaluation eval,
		ArrayList<Double> predictions, Instances data) throws Exception{
		
		FileOutputStream rawFile=null;
		OutputStreamWriter sWriter=null;
		BufferedWriter bWriter=null;
		
		
		try {
			rawFile = new FileOutputStream(file);		
			sWriter = new OutputStreamWriter(rawFile,"UTF-8");			
			bWriter = new BufferedWriter(sWriter,4096*5); // default 2048
			bWriter.append("************************************************************");
			bWriter.newLine();
			bWriter.append(eval.toSummaryString(true));
			bWriter.append("\n\n\n\n");
			bWriter.append(eval.toClassDetailsString());
			bWriter.append("\n\n\n\n");
			bWriter.append(eval.toMatrixString());
			bWriter.append("\n\n\n\n");
			
			int subjOK=0;
			int subjFalse=0;
			for(int i=0;i<predictions.size();i++){
				bWriter.append(data.attribute(0).value((int)data.instance(i).value(0))+"\tpredicted: "+data.classAttribute().value(predictions.get(i).intValue())+"\tactual: "+data.classAttribute().value((int)data.instance(i).classValue()));				
				bWriter.newLine();
				if(predictions.get(i).intValue()==0 && data.instance(i).classValue()==predictions.get(i).intValue()){
					subjOK++;
				}
				else if(predictions.get(i).intValue()==0 && data.instance(i).classValue()!=predictions.get(i).intValue()){
					subjFalse++;
				}				
			}
			
			bWriter.flush();
			
			System.out.println(subjOK);
			System.out.println(subjFalse);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(rawFile!=null){
					rawFile.close();	
				}
				if(sWriter!=null){
					sWriter.close();	
				}
				if(bWriter!=null){
					bWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static Instances filterAttribute(Instances data, String range) throws Exception{
		Remove idRemover = new Remove();
		idRemover.setAttributeIndices(range);
		idRemover.setInputFormat(data);
		Instances filteredData = Filter.useFilter(data, idRemover);
		return filteredData;
	}

	public static ArrayList<Double> crossValidate(Evaluation eval, Classifier classifier, Instances data, int numFolds, Random random) throws Exception{
		ArrayList<Double> output = new ArrayList<Double>();
		
	    data.randomize(random);
	    if (data.classAttribute().isNominal()) {
	      data.stratify(numFolds);
	    }
	    
	    for (int i = 0; i < numFolds; i++) {
	      Instances train = data.trainCV(numFolds, i, random);
	      train=filterAttribute(train, "first");
	      eval.setPriors(train);
	      Classifier copiedClassifier = Classifier.makeCopy(classifier);
	      copiedClassifier.buildClassifier(train);
	      Instances test = data.testCV(numFolds, i);
	      test=filterAttribute(test, "first");
	      double[] predictions = eval.evaluateModel(copiedClassifier, test);
	      
	      for(double d : predictions){
	    	  output.add(d);
	      }
	    }	    
	    
    
	    return output;
			
	}
	
	public static ArrayList<Double> validate(Evaluation eval, Classifier classifier, Instances train, Instances test) throws Exception{
		ArrayList<Double> output = new ArrayList<Double>();		
		classifier.buildClassifier(train);
		double[] predictions = eval.evaluateModel(classifier, test);
	    for(double d : predictions){
	    	output.add(d);
	    }		
		return output;
	}
	
	
	
	 
	
}
