import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class main {
    public static void main(String[] args) {

        String arff = "C:\\ml\\hw2\\hw2q4\\data\\sonar.arff";
		Instances instances = loadARFF(arff);
        instances.setClassIndex(instances.numAttributes() - 1);

		// Simple learning & testing
		showSimpleTraining(instances);

		// Using Cross Validation
		// Note: CV is used to assess the real quality of the classifier.
		//       The classifier to use, however, is the one that would be trained on the full training set.
		showCVTraining(instances);
    }

    	private static void showSimpleTraining(Instances instances){
		System.out.println("\nSimple learning & testing");
		try {
			// Create and build a classifier
			Classifier classifier = buildClassifier(instances);

			// Check the classifications on the training set
			testClassifier(instances, classifier, true);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void showCVTraining(Instances instances){
		System.out.println("\nUsing Cross Validation - single run");
		try {

			Random rng = new Random();							// Create an NG
			Instances randData = new Instances(instances);		// Create a copy of the data
			randData.randomize(rng);							// Randomize the data

			int numFolds = 10;
			randData.stratify(numFolds);						// Make sure that the probability distributions of the classes is roughly the same between the folds

			double avgSuccessRatio = 0.0;
			for (int fold = 0; fold < numFolds; fold++){
				Instances train = randData.trainCV(numFolds, fold);
				Instances test = randData.testCV(numFolds, fold);

				Classifier classifier = buildClassifier(train);

				double foldSuccess = testClassifier(test, classifier, false);
				avgSuccessRatio += foldSuccess;
			}

			avgSuccessRatio /= numFolds;
			System.out.println("Average success ratio with CV: " + avgSuccessRatio);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    private static double testClassifier(Instances instances, Classifier classifier, boolean fullPrint) throws Exception {
		int successCount = 0;
		for (int i = 0; i < instances.numInstances(); i++) {
			Instance instance = instances.instance(i);
			double actual = instance.classValue();
			double predicted = classifier.classifyInstance(instance);
			if (actual == predicted){
				successCount++;
			}
			if (fullPrint){
				double[] distribution = classifier.distributionForInstance(instance);
				System.out.print(actual + "," + predicted);
				for (int d = 0; d < distribution.length; d++){
					System.out.print("," + distribution[d]);
				}
				System.out.println();
			}
		}

		double successRatio = successCount/(double)instances.numInstances();
		System.out.println("Success ratio on set: " + successRatio);

		return successRatio;
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
