/**
 * Created by IntelliJ IDEA.
 * User: amir
 * Date: 12/3/11
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Perceptron
extends AbstractClassifier {
    private static final long serialVersionUID = 1234567890L;

    private double eta;
    private int numIterations;
    private List<Double> weights;
    private Random random;

    public Perceptron() {
        random = new Random();
    }
    /**
    * Sets the number of iterations used for training the Perceptron
    * @param numIterations
    */
    public void setNumberOfIterations(int numIterations){
        this.numIterations = numIterations;
    }
    /**
    * Sets the learning rate - the rate at which the weights are updated
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
            throw new Exception("Can't build classifier for empty instances list")

        // initialize weights
        initializeWeights(insts[0].numAttributes);

        for (int iteration=0;iteration<numIterations;iteration++) {
            List<Double> instanceSigns = calculateSigns(insts);
            for (int weightIndex=0;weightIndex<weights.size();weightIndex++)
                weights[weightIndex]+=calculateWeightDelta(weightIndex,insts);
        }

    }

    /**
    * Classifies the given instance using the perceptron.
    * The value returned corresponds to the (0-based) index of the predicted
    * nominal class. For example – if the labels in the arff are defined as
    * { Yes, No }, then when the classifier predicts “Yes” this method will
    * return 0, and when the classifier predicts “No” this method will return 1.
    */
    public double classifyInstance(Instance inst) throws Exception {
        Double perceptronTotal = 0.0;
        for (int dimIndex = 0; dimIndex<weights.size(); dimIndex++)
            perceptronTotal += weights[dimIndex]*inst.getAttribute(dimIndex);
        return (Math.sign(perceptronTotal)+1)/2;
    }

    private void initializeWeights(int numAttributes) {
        weights = new ArrayList<Double>(numAttributes);
        for (int i=0;i<numAttributes;i++)
            weights[i]=random.nextDouble();
    }

    private Integer productWeightsAttribute(Instance instance) {
        Double result=0;
        for (int i=0;i<instance.numAttributes;i++) {
            result+=instance.getAttribute(i)*weights[i];
        }
        return result;
    }

    private List<Double> calculateSigns(Instances insts) {
        List<Double> results = new ArrayList<Integer>();
        for (Instance instance : insts) {
            results.append((Double)Math.sign(productWeightsAttribute(instance)));
        }
        return results;
    }

    private Double calculateWeightDelta(int weightIndex,Instances insts,List<Double> instanceSigns) {
        Double weightDelta = 0.0;
        for (int instanceIndex = 0; instanceIndex<insts.size(); instanceIndex++)
            weightDelta += (insts[instanceIndex].getLabel() - instanceSigns[instanceIndex])*
                           insts[instanceIndex].getAttribute(weightIndex);
        return weightDelta * eta;
    }
}


