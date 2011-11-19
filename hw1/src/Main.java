import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: itamar
 * Date: 11/18/11
 * Time: 10:29 AM
 */
public class Main {

    private static Random random = new Random(0); //TODO: replace seed

    public static void main(String[] args) {
        String path = "/home/amir/school/ml/hw1/data/liver-disorders.csv";

        // get path for command line
        if (args.length > 0)
            path = args[0];

        // load instances
        final List<Instance> instances = Instance.loadInstances(path);

        List<Result> results = bulkRun(instances, 2, 0.2);
    }

    private static List<Result> bulkRun(List<Instance> instances, int numberOfRuns, double testPercent) {
        List<Result> resultList = new ArrayList<Result>();

        for (int run = 1; run <= numberOfRuns; run++) {
            Pair<List<Instance>> instancesPair = splitDataSet(instances, testPercent);
            List<Instance> train = instancesPair.first;
            List<Instance> test = instancesPair.second;

            System.out.println("Starting run #" + run + " [train: " + train.size() + ", test:" + test.size() + "]");

            resultList.add(evaluate(train, test));
        }

        return resultList;
    }

    public static Result evaluate(List<Instance> train, List<Instance> test) {
        Result result = new Result();

        RandomDT dt = new RandomDT();
        dt.train(train);

        for (Instance testInstance : test) {
            double classifiedLabel = dt.classify(testInstance);
            double trueLabel = testInstance.getLabelValue();

            result.update(classifiedLabel, trueLabel);
        }

        return result;
    }

    public static Pair<List<Instance>> splitDataSet(List<Instance> instances, double percent) {
        List<Instance> train = new ArrayList<Instance>();
        List<Instance> test = new ArrayList<Instance>();

        for (Instance instance : instances) {
            if (random.nextDouble() < percent) {
                test.add(instance);
            } else {
                train.add(instance);
            }
        }

        return new Pair<List<Instance>>(train, test);
    }
}