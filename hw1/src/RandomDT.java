import java.util.*;

/**
 * Assumes that all Features are continuous.
 * Missing values are considered as a separate possible feature value.
 * Serves only for classification, not regression.
 * At each node, the Feature used is selected randomly.
 * The splitting value used is also selected randomly.
 */
public class RandomDT {

    private Node root;

    private Random random;

    /**
     * Creates a new instance of a Random Decision Tree.
     * Configuration options (if applicable), should be added here.
     */
    public RandomDT() {
        random = new Random();
    }

    public RandomDT(Random random) {
        this.random = random;
    }


    /**
     * Trains the Decision Tree.
     *
     * @param trainSet received set of Instance objects.
     */
    public void train(List<Instance> trainSet) {
        root = recursiveTrainer(trainSet);
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

    private Node recursiveTrainer(List<Instance> instances) {
        // stop condition
        if (instances.size() == 0) {
            return new Node(random.nextInt(1));
        }

        // stop condition
        if (isOneClassOnly(instances)) {
            return new Node(instances.get(0).getLabelValue());
        }

        // select a random feature
        else {
            int featureIndex = getRandomFeature(instances);
            String featureName = instances.get(0).getFeatureName(featureIndex);
            double splitValue = getRandomSplitValue(instances, featureIndex);

            Pair<List<Instance>> splitInstances = splitInstances(instances, featureIndex, splitValue);

            Node left = recursiveTrainer(splitInstances.first);
            Node right = recursiveTrainer(splitInstances.second);

            return new Node(left, right, featureName, featureIndex, splitValue);
        }
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
     * @param current the current iterated node
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
}
