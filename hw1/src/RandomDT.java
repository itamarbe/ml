import java.util.*;

/**
 * Assumes that all Features are continuous.
 * Missing values are considered as a separate possible feature value.
 * Serves only for classification, not regression.
 * At each node, the Feature used is selected randomly.
 * The splitting value used is also selected randomly.
 */
public class RandomDT {

    // the root of the tree
    private Node root;

    // random generator that create the random numbers
    private Random random;

    // choose whether to use random split or information gain
    private boolean useInformationGain;
    private boolean usePriors;

    /**
     * Creates a new instance of a Random Decision Tree.
     * Configuration options (if applicable), should be added here.
     */
    public RandomDT() {
        random = new Random();
    }

    public RandomDT(Random random, boolean useInformationGain, boolean usePriors) {
        this.random = random;
        this.useInformationGain = useInformationGain;
        this.usePriors = usePriors;
    }


    private double posClassPrior;

    /**
     * Trains the Decision Tree.
     *
     * @param trainSet received set of Instance objects.
     */
    public void train(List<Instance> trainSet) {
        posClassPrior = getPositivePrior(trainSet);

        root = recursiveTrainer(trainSet);
    }

    public static double getPositivePrior(List<Instance> instances) {
        double prior = 0;

        for (Instance instance : instances) {
            if (instance.getLabelValue() == 1) {
                prior++;
            }
        }

        prior /= instances.size();

        return prior;
    }

    private boolean isOneClassOnly(List<Instance> instances) {
        double label = instances.get(0).getLabelValue();

        for (Instance instance : instances) {
            if (label != instance.getLabelValue()) {
                return false;
            }
        }

        return true;
    }

    private boolean allHasSameValues(List<Instance> instances) {
        if (instances.size() <= 1)
            return true;

        List<Feature> firstFeatures = instances.get(0).getAllFeatures();

        for (Instance instance : instances) {
            for (int i = 0; i < firstFeatures.size(); i++) {

                // there is at least 1 feature of 1 different value
                if (instance.getFeatureValue(i) != firstFeatures.get(i).getValue()){
                    return false;
                }
            }
        }
        
        return true;
    }

    private Node recursiveTrainer(List<Instance> instances) {
        // stop condition - select a random variables according to priors
        if (instances.size() == 0) {
            return getNewDefaultLabel();
        }

        // stop condition - all the instances are in the same class
        if (isOneClassOnly(instances)) {
            return new Node(instances.get(0).getLabelValue());
        }

        // stop condition - all instances has the same feature values but has different classes
        // this is an edge cases where all instances feature values are the same.
        if (allHasSameValues(instances)) {
            return getNewDefaultLabel();
        }

        // select a random feature
        else {
            int featureIndex = getRandomFeature(instances);
            String featureName = instances.get(0).getFeatureName(featureIndex);
            if (singleValueFeature(instances, featureIndex)) {
                double positivePrior = getPositivePrior(instances);
                return new Node((positivePrior>=0.5)?1.0:0.0);
            }
            double splitValue = useInformationGain ? getGainSplitValue(instances, featureIndex) : getRandomSplitValue(instances, featureIndex);

            Pair<List<Instance>> splitInstances = splitInstances(instances, featureIndex, splitValue);
            if ((splitInstances.first.size()==0) || (splitInstances.second.size()==0)) {
                double splitValue2 = useInformationGain ? getGainSplitValue(instances, featureIndex) : getRandomSplitValue(instances, featureIndex);
            }

            Node left = recursiveTrainer(splitInstances.first);
            Node right = recursiveTrainer(splitInstances.second);

            return new Node(left, right, featureName, featureIndex, splitValue);
        }
    }

    private boolean singleValueFeature(List<Instance> instances, int featureIndex) {
        double firstValue = instances.get(0).getFeatureValue(featureIndex);
        for (Instance instance : instances) {
            if (instance.getFeatureValue(featureIndex)!=firstValue) return false;
        }
        return true;
    }

    private double calculateEntropy(List<Instance> instances) {
        double numberPositives = 0.0;
        double numberNegatives = 0.0;
        double total = instances.size();
        for (Instance instance : instances) {
            if (instance.getLabelValue() == 1.0) {
                numberPositives += 1.0;
            } else {
                numberNegatives += 1.0;
            }
        }
        double positiveProbability = numberPositives / total;
        double negativeProbability = numberNegatives / total;
        return -(positiveProbability>0.0?(positiveProbability * log2(positiveProbability)):0.0) - (negativeProbability>0.0?(negativeProbability * log2(negativeProbability)):0.0);
    }

    private double getGainSplitValue(List<Instance> instances, int featureIndex) {
        Map<Double, List<Instance>> valueListMap = new HashMap<Double, List<Instance>>();
        Double firstSplitValue = instances.get(0).getFeatureValue(featureIndex);
        Double secondSplitValue = firstSplitValue;
        Double minSplitValue = firstSplitValue;
        for (Instance instance : instances) {
            Double value = instance.getFeatureValue(featureIndex);
            if (value<minSplitValue) minSplitValue = value;
            if (!value.equals(firstSplitValue)) secondSplitValue = value;
            if (valueListMap.containsKey(value)) {
                valueListMap.get(value).add(instance);
            } else {
                List<Instance> valueList = new ArrayList<Instance>();
                valueList.add(instance);
                valueListMap.put(value, valueList);
            }
        }
        double bestEntropy = -1.0;
        double bestEntropySplitValue = (firstSplitValue!=minSplitValue)?firstSplitValue:secondSplitValue;
        for (Double currentSplitValue : valueListMap.keySet()) {
            List<Instance> listInstance = valueListMap.get(currentSplitValue);
            double currentEntropy = calculateEntropy(listInstance);
            if ((currentEntropy >= bestEntropy) && (!currentSplitValue.equals(minSplitValue))) {
                bestEntropy = currentEntropy;
                bestEntropySplitValue = currentSplitValue;
            }
        }
        return bestEntropySplitValue;
    }

    private Pair<List<Instance>> splitInstances(List<Instance> instances, int featureIndex, double splitValue) {
        List<Instance> lessThanValueInstances = new ArrayList<Instance>();
        List<Instance> greaterOrEqualThanValueInstances = new ArrayList<Instance>();

        for (Instance instance : instances) {
            if (instance.getFeatureValue(featureIndex) < splitValue) {
                lessThanValueInstances.add(instance);
            } else {
                greaterOrEqualThanValueInstances.add(instance);
            }
        }

        return new Pair<List<Instance>>(lessThanValueInstances, greaterOrEqualThanValueInstances);
    }

    private double getRandomSplitValue(List<Instance> trainSet, int featureIndex) {
        List<Double> possibleValue = new ArrayList<Double>();

        for (Instance instance : trainSet) {
            possibleValue.add(instance.getFeatureValue(featureIndex));
        }

        Collections.shuffle(possibleValue, random);
        return possibleValue.get(0);
    }

    private int getRandomFeature(List<Instance> trainSet) {
        Instance instance = trainSet.get(0);

        return (random.nextInt(instance.numFeatures()));
    }

    /**
     * Classify a given instance
     *
     * @param current  the current iterated node
     * @param instance the instance that neended to be classified
     * @return the classification of the given instance.
     *         If the Decision Tree has not been trained, either throw an exception
     *         or return "-1", which stands for an empty Label value.
     */
    public double recursiveClassifier(Node current, Instance instance) {
        // need to choose whether to go left or right
        if (current.getLabel() == null) {
            double value = instance.getFeatureValue(current.getFeatureIndex());

            if (value < current.getSplitValue()) {
                return recursiveClassifier(current.getLeft(), instance);
            } else {
                return recursiveClassifier(current.getRight(), instance);
            }
        }

        // we reached to a leaf
        return current.getLabel();
    }

    public double classify(Instance instance) {
        return recursiveClassifier(root, instance);
    }

    /**
     * Returns a tree-structure string representation of the internal
     * Decision Tree that has been learned by train().
     */
    @Override
    public String toString() {
        return printNode(root, 0);
    }

    private String printNode(Node node, int indentDepth) {
        StringBuilder prefixString = new StringBuilder();

        for (int i = 0; i < indentDepth; i++) {
            prefixString.append("--|");
        }

        // we reached to a classification leaf
        if (node.getLabel() != null) return " (" + node.getLabel() + ")";

        return "\n" + prefixString.toString() + (node.getFeatureName() + " < " + node.getSplitValue()) +
                printNode(node.getLeft(), indentDepth + 1) +
                "\n" + prefixString.toString() + (node.getFeatureName() + " >= " + node.getSplitValue()) +
                printNode(node.getRight(), indentDepth + 1);
    }

    private static double log2(double number) {
        return (Math.log(number) / Math.log(2));
    }

    public Node getNewDefaultLabel() {
        if (usePriors) {
            double label = (posClassPrior >= 0.5) ? 1 : 0;
            return new Node(label);
        }

        return new Node(random.nextInt(1));
    }
}
