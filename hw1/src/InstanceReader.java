import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InstanceReader {


    public List<Instance> loadInstancesFromCSV(String path) {
        List<Instance> instances = new ArrayList<Instance>();

        File inputFile = new File(path);

        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            String[] featureNames = new String[0];
            String line;
            int counter = 0;

            while ((line = br.readLine()) != null) {
                final String[] parsedLine = line.trim().split(",");

                // feature names
                if (counter == 0) {
                    featureNames = parsedLine;

                // instances
                } else {
                    if (featureNames.length != parsedLine.length) {
                        System.err.println("Feature names and values do not match, continue to next instance");
                    } else {
                        List<Feature> features = new ArrayList<Feature>();

                        for (int i = 0; i < parsedLine.length - 1; i++) {
                            features.add(new Feature(featureNames[i], Double.valueOf(parsedLine[i])));
                        }

                        double label = Double.valueOf(parsedLine[parsedLine.length-1]);
                        instances.add(new Instance(features, label));
                    }
                }
                counter++;
            }

            System.out.println("Successfully parsed " + path + " with total of " + counter + " lines");
            br.close();

        } catch (FileNotFoundException e) {
            System.err.println("Failed to find file " + path + " , exiting.");
        } catch (IOException e) {
            System.err.println("Failed to read file " + path + " , exiting.");
        }

        return instances;
    }

}
