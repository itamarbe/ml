import weka.classifiers.AbstractClassifier;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public class Perceptron extends AbstractClassifier {
    private static final long serialVersionUID = 1234567890L;

    private double eta;
    private int numIterations;
    private double[] weights;

    public Perceptron() {
        eta = 1;                 //default value
        numIterations = 100;     //default value
    }

    /**
     * Sets the number of iterations used for training the Perceptron
     *
     * @param numIterations
     */
    public void setNumberOfIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    /**
     * Sets the learning rate - the rate at which the weights are updated
     *
     * @param eta
     */
    public void setLearningRate(double eta) {
        this.eta = eta;
    }

    /**
     * Trains the Perceptron
     */
    public void buildClassifier(Instances insts) throws Exception {
        if (insts.size() == 0)
            throw new Exception("Can't build classifier for empty instances list");

        // initialize weights
        initializeWeights(insts.numAttributes()); // include the bias

        // until termination do
        for (int iteration = 0; iteration < numIterations; iteration++) {
            // for each instance in D compute signs
            List<Double> instanceSigns = calculateSigns(insts);

            // for each linear unit weight w do
            for (int weightIndex = 0; weightIndex < weights.length; weightIndex++) {
                weights[weightIndex] += calculateWeightDelta(weightIndex, insts, instanceSigns);
            }
        }
    }

    /**
     * Classifies the given instance using the perceptron.
     * The value returned corresponds to the (0-based) index of the predicted
     * nominal class. For example, if the labels in the arff are defined as
     * { Yes, No }, then when the classifier predicts �Yes� this method will
     * return 0, and when the classifier predicts �No� this method will return 1.
     */
    public double classifyInstance(Instance inst) throws Exception {
        double perceptronTotal = 0.0;
        for (int dimIndex = 0; dimIndex < weights.length; dimIndex++) {
            perceptronTotal += weights[dimIndex] * inst.value(dimIndex);
        }

        return (perceptronTotal >= 0) ? 1 : 0;
    }

    private void initializeWeights(int numAttributes) {
        weights = new double[numAttributes];
        for (int i = 0; i < numAttributes; i++) {
            weights[i] = 0;
        }
    }

    private List<Double> calculateSigns(Instances insts) {
        List<Double> results = new ArrayList<Double>();
        for (Instance instance : insts) {
            results.add(Math.signum(getProductWeightsAttribute(instance)));
        }
        return results;
    }

    private double getProductWeightsAttribute(Instance instance) {
        double result = 1 * weights[weights.length-1]; // add the bias (last item in the array)
        for (int i = 0; i < weights.length-1; i++) {
            result += instance.value(i) * weights[i];
        }
        
        return result;
    }

    private double calculateWeightDelta(int weightIndex, Instances insts, List<Double> instanceSigns) {
        double weightDeltaSum = 0.0;
        for (int instanceIndex = 0; instanceIndex < insts.size(); instanceIndex++) {
            final Instance instance = insts.get(instanceIndex);
            weightDeltaSum += (instance.classValue() - instanceSigns.get(instanceIndex)) *
                    instance.value(weightIndex);
        }

        return weightDeltaSum * eta;
    }
}


