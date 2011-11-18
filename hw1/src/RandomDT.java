import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Assumes that all Features are continuous.
 * Missing values are considered as a separate possible feature value.
 * Serves only for classification, not regression.
 * At each node, the Feature used is selected randomly.
 * The splitting value used is also selected randomly.
 */
public class RandomDT {

    private Node root;

    /**
     * Creates a new instance of a Random Decision Tree.
     * Configuration options (if applicable), should be added here.
     */
    public RandomDT() {
    }

    /**
     * Trains the Decision Tree.
     *
     * @param trainSet received set of Instance objects.
     */
    public void train(List<Instance> trainSet) {
        root = recursiveSplit(trainSet);
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

    private Node recursiveSplit(List<Instance> instances) {
        // stop condition
        if (instances.size() == 0 || isOneClassOnly(instances)) {
            return null;
        }

        // select a random feature
        else {
            int featureIndex = getRandomFeatureIndex(instances);
            String featureName = instances.get(0).getFeatureName(featureIndex);
            double splitValue = getRandomSplitValue(instances, featureIndex);

            Pair<List<Instance>> splitInstances = splitInstances(instances, featureIndex, splitValue);
            Node left = recursiveSplit(splitInstances.first);
            Node right = recursiveSplit(splitInstances.second);

            return new Node(left, right, featureName, splitValue);
        }
    }

    public class Node {
        public String featureName;

        public double splitValue;

        public Node left;

        public Node right;


        public Node(Node left, Node right, String featureName, double splitValue) {
            this.left = left;
            this.right = right;
            this.featureName = featureName;
            this.splitValue = splitValue;
        }
    }

    public class Pair<E> {
        public E first;
        public E second;

        public Pair(E first, E second) {
            this.first = first;
            this.second = second;
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

    private static Random random = new Random();

    private int getRandomFeatureIndex(List<Instance> trainSet) {
        Instance instance = trainSet.get(0);

        return (random.nextInt(instance.numFeatures()));

    }

    /**
     * Classify a given instance
     *
     * @param instance
     * @return the classification of the given instance.
     *         If the Decision Tree has not been trained, either throw an exception
     *         or return "-1", which stands for an empty Label value.
     */
    public double classify(Instance instance) {
        return 0;
    }

    /**
     * Returns a tree-structure string representation of the internal
     * Decision Tree that has been learned by train().
     */
    @Override
    public String toString() {

        return printNode(root);


    }

    private String printNode(Node node) {
        if (node == null) {
            return "";
        }

        return (node.featureName + "\n") + printNode(node.left) + printNode(node.right);
    }
}
