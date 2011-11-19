import java.util.List;

/**
 * Represents an instance which consists of a set of Features and a Label.
 * Features and Labels are all of type double.
 * A missing Label is indicated by the value -1.
 */
public class Instance {

    // the features in each instance
    private List<Feature> features;

    // instance label
    private double labelValue;

    public Instance(List<Feature> features, double labelValue) {
        this.features = features;
        this.labelValue = labelValue;
    }

    /**
     * Loads the instances from the given CSV file.
     * The first line represents the names of the Features.
     * Each line thereafter represents a separate instance.
     * Each field in each line represents a Feature,
     * except the last field which is the Label.
     * Following is an example:
     * <p/>
     * Feature1,Feature2,Feature3,Label   * 1,2,3,0
     * 2,4,6,1
     * 3,6,9,-1
     * <p/>
     * In this example we have:
     * 3 instances
     * 3 Features
     * 2 classes (0, 1)
     * The last instance has an empty label
     *
     * @param path the location of the CSV file
     * @return a list of instances
     */
    public static List<Instance> loadInstances(String path) {
        if (path == null || path.equals("")) {
            System.err.println("please enter a valid path in order to load instances");
        }

        InstanceReader ir = new InstanceReader();

        return ir.loadInstancesFromCSV(path);
    }

    /**
     * @param index the feature index
     * @return the Feature at the given index.
     */
    public double getFeatureValue(int index) {
        return features.get(index).getValue();
    }

    /**
     * Returns the name of the Feature at the given index.
     *
     * @param index the feature index
     * @return the name of the Feature
     */
    public String getFeatureName(int index) {
        return features.get(index).getName();
    }

    /**
     * @return the Label. A missing Label is indicated by the value -1.
     */
    public double getLabelValue() {
        return labelValue;
    }

    /**
     * @return the number of Features in the instance
     *         (the Label is not considered a Feature).
     */
    public int numFeatures() {
        return features.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Feature feature : features) {
            sb.append(feature.toString()).append("\t | \t");
        }

        sb.append("label=").append(labelValue);

        return sb.toString();
    }

    public List<Feature> getAllFeatures() {
        return features;
    }
}