import java.util.List;

/**
 * User: itamar
 * Date: 11/18/11
 * Time: 10:29 AM
 */
public class Main {

    public static void main(String[] args) {
        String path = "/home/amir/school/ml/hw1/data/liver-disorders.csv";

        boolean randomize = true;

        // get path for command line
        if (args.length > 0)
            path = args[0];

        if (args.length > 1)
            randomize = Boolean.parseBoolean(args[1]);

        // load instances
        final List<Instance> instances = Instance.loadInstances(path);
        Simulator sim = new Simulator(randomize);

        // section c
        List<Result> results = sim.bulkRun(instances, 100, 0.3);

        // section d
        sim.plotOverallAccuracy(results);
        sim.plotPositiveAccuracy(results);
        sim.plotNegativeAccuracy(results);

        // section e.a
//        sim.plotDecisionTree(instances);

        // section e.b
        sim.plotConfusionMatrix(results);
    }



    

}