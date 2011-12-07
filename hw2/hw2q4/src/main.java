import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class main {
    public static void main(String[] args) throws Exception {
        String path = "./data/sonar.arff";

        if (args.length > 0)
            path = args[0];

        // load data
        Instances instances = loadARFF(path);
        instances.setClassIndex(instances.numAttributes() - 1);

        test(instances, 0.1, 100);
        test(instances, 0.2, 50);
        test(instances, 0.7, 75);
        test(instances, 0.01, 200);
        test(instances, 0.3, 40);
    }

    private static void test(Instances instances, double eta, int numIterations) throws Exception {
        Random rng = new Random();
        Instances randData = new Instances(instances);
        randData.randomize(rng);

        int numFolds = 10;
        randData.stratify(numFolds);

        double avgSuccessRatio = 0.0;
        for (int fold = 0; fold < numFolds; fold++) {
            Instances train = randData.trainCV(numFolds, fold);
            Instances test = randData.testCV(numFolds, fold);

            Perceptron classifier = new Perceptron();
            classifier.setNumberOfIterations(numIterations);
			classifier.setLearningRate(eta);

            classifier.buildClassifier(train);

            double foldSuccess = testClassifier(test, classifier);
            avgSuccessRatio += foldSuccess;
        }

        avgSuccessRatio /= numFolds;
        System.out.println("learning rate: " + eta + "\t| number of iterations: " + numIterations + "\t| Average success ratio: " + avgSuccessRatio);
    }

    private static double testClassifier(Instances instances, Classifier classifier) throws Exception {
        int successCount = 0;
        for (int i = 0; i < instances.numInstances(); i++) {
            Instance instance = instances.instance(i);
            double actual = instance.classValue();
            double predicted = classifier.classifyInstance(instance);
            if (actual == predicted) {
                successCount++;
            }
        }

        return successCount / (double) instances.numInstances();
    }

    private static Instances loadARFF(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        Instances data = new Instances(reader);
        reader.close();

        return data;
    }
}
