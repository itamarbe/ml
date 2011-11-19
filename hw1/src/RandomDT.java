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
        if (instances.size() == 0) {
            return null;
        }

        if (isOneClassOnly(instances)) {
            return new Leaf(instances);
        }

        // select a random feature
        else {
            int featureIndex = getRandomFeatureIndex(instances);
            if (getNumberOfFeatureValues(instances, featureIndex) == 1) {
                return new SplitLeaf(getNumberOfPositiveLabels(instances), instances.size());
            } else {
                String featureName = instances.get(0).getFeatureName(featureIndex);
                double splitValue = getRandomSplitValue(instances, featureIndex);

                Pair<List<Instance>> splitInstances = splitInstances(instances, featureIndex, splitValue);
                Node left = recursiveSplit(splitInstances.first);
                Node right = recursiveSplit(splitInstances.second);

                return new Node(left, right, featureName, featureIndex, splitValue);
            }
        }
    }

    private int getNumberOfPositiveLabels(List<Instance> instances) {
        int counter = 0;
        for (Instance instance : instances) {
            if (instance.getLabelValue() == 1.0) {
                counter++;
            }
        }
        return counter;
    }

    public int getNumberOfFeatureValues(List<Instance> instances, int featureIndex) {
        Set<Double> valueSet = new HashSet<Double>();
        for (Instance instance : instances) {
            valueSet.add(new Double(instance.getFeatureValue(featureIndex)));
        }
        return valueSet.size();
    }

    public class Node {
        public String featureName;

        public int featureIndex;

        public double splitValue;

        public Node left = null;

        public Node right = null;

        public Node(Node left, Node right, String featureName, int featureIndex, double splitValue) {
            this.left = left;
            this.right = right;
            this.featureName = featureName;
            this.splitValue = splitValue;
            this.featureIndex = featureIndex;
        }

        public Node() {
        }
    }

    public class Leaf extends Node {
        private double label;

        public Leaf(Node left, Node right, String featureName, int featureIndex, double splitValue) {
            super(left, right, featureName, featureIndex, splitValue);
        }

        public Leaf(List<Instance> instances) {
            super(null, null, null, 0, 0.0);
            this.label = instances.get(0).getLabelValue();
        }

    }

    public class SplitLeaf extends Node {

        public int numPositive;
        public int numTotal;

        public SplitLeaf(Node left, Node right, String featureName, int featureIndex, double splitValue) {
            super(left, right, featureName, featureIndex, splitValue);
        }

        public SplitLeaf(int numPositive, int numTotal) {

            this.numPositive = numPositive;
            this.numTotal = numTotal;
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

    private static Random random = new Random(0);

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
        Node current = root;
        while (true) {
            if (current == null) {
                System.out.println("Got null node :(");
                return -1;
            }
            int featureIndex = current.featureIndex;
            double splitValue = current.splitValue;
            double instanceFeatureValue = instance.getFeatureValue(featureIndex);

            if (instanceFeatureValue < splitValue) {
                if (current.left instanceof Leaf) {
                    return ((Leaf) (current.left)).label;
                } else if (current.left instanceof SplitLeaf) {
                    SplitLeaf s = (SplitLeaf) (current.left);
                    return random.nextDouble() <= ((new Double(s.numPositive)) / (new Double(s.numTotal))) ? 1.0 : 0.0;
                } else {
                    current = current.left;
                    continue;
                }
            } else {
                if (current.right instanceof Leaf) {
                    return ((Leaf) (current.right)).label;
                } else if (current.right instanceof SplitLeaf) {
                    SplitLeaf s = (SplitLeaf) (current.right);
                    return random.nextDouble() <= ((new Double(s.numPositive)) / (new Double(s.numTotal))) ? 1.0 : 0.0;
                } else {
                    current = current.right;
                    continue;
                }
            }
        }
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
        String prefixSequence = "--|";
        StringBuilder prefixString = new StringBuilder();
        for (int i = 0; i < indentDepth; i++) {
            prefixString.append(prefixSequence);
        }
        if (node == null) {
            return "";
        }

        if (node instanceof Leaf) {
            Leaf l = (Leaf) node;
            return " (" + Double.toString(l.label) + ")";
        }
        if (node instanceof SplitLeaf) {
            SplitLeaf s = (SplitLeaf) node;
            return " ({" + s.numPositive + "," + (s.numTotal - s.numPositive) + "})";
        }

        return "\n" + prefixString.toString() + (node.featureName + " < " + node.splitValue) +
                printNode(node.left, indentDepth + 1) +
                "\n" + prefixString.toString() + (node.featureName + " >= " + node.splitValue) +
                printNode(node.right, indentDepth + 1);
    }
}
