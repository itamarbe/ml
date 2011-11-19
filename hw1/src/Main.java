import java.util.List;

/**
 * User: itamar
 * Date: 11/18/11
 * Time: 10:29 AM
 */
public class Main {

    public static void main(String[] args) {
        String path = "./data/liver-disorders.csv";

        boolean randomize = true;
        boolean useInformationGain = false;
        boolean usePriors = true;

        // get path for command line
        if (args.length > 0)
            path = args[0];

        // load instances
        final List<Instance> instances = Instance.loadInstances(path);
        Simulator sim = new Simulator(randomize, useInformationGain, usePriors);

        // section c
        List<Result> results = sim.bulkRun(instances, 100, 0.7);

        // section d
        sim.plotOverallAccuracy(results);
        sim.plotPositiveAccuracy(results);
        sim.plotNegativeAccuracy(results);

        // section e.a
        //sim.plotDecisionTree(instances);

        // section e.b
        sim.plotConfusionMatrix(results);

        // section f
        final double positivePrior = RandomDT.getPositivePrior(instances);
        System.out.println("\nDataset positive prior: " + positivePrior + ", negative prior: " + (1 - positivePrior));
    }


}