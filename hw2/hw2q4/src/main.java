import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;

public class main {
    public static void main(String[] args) {

        String arff = "C:\\ml\\hw2\\hw2q4\\data\\sonar.arff";
		Instances instances = loadARFF(arff);
        instances.setClassIndex(instances.numAttributes() - 1);

        for (Instance instance : instances) {
            System.out.println(instance);
        }

        Classifier classifier = buildClassifier(instances);

        System.out.println("hello world");
    }

    private static Classifier buildClassifier(Instances instances){
        Classifier perceptron = new Perceptron();

        try {
            perceptron.buildClassifier(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return perceptron;

    }


    public static Instances loadARFF(String path) {
		try {

		 BufferedReader reader = new BufferedReader(new FileReader(path));
		 Instances data = new Instances(reader);
		 reader.close();

		 return data;
		 } catch (Exception e){
			 e.printStackTrace();
			 return null;
		 }

	}
}
