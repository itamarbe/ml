public class Node {
    private Node left;
    private Node right;

    private String featureName;
    private int featureIndex;

    private Double label;

    private double splitValue;

    public Node(Node left, Node right, String featureName, int featureIndex, double splitValue) {
        this.left = left;
        this.right = right;
        this.featureName = featureName;
        this.splitValue = splitValue;
        this.featureIndex = featureIndex;
    }

    public Node(double label) {
        this.label = label;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public String getFeatureName() {
        return featureName;
    }

    public int getFeatureIndex() {
        return featureIndex;
    }

    public Double getLabel() {
        return label;
    }

    public double getSplitValue() {
        return splitValue;
    }
}