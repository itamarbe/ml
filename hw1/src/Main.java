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

    private static boolean printTree = false;

    public static void main(String[] args) {
        String path = "/home/amir/school/ml/hw1/data/liver-disorders.csv";

        // get path for command line
        if (args.length > 0)
            path = args[0];

        // load instances
        final List<Instance> instances = Instance.loadInstances(path);

        List<Result> results = bulkRun(instances, 10, 0.3);

        plotConfusionMatrix(results);
    }


    private static void plotConfusionMatrix(List<Result> results) {
        int totalCorrect0 = 0;
        int totalCorrect1 = 0;
        int totalInCorrect0 = 0;
        int totalInCorrect1 = 0;

        for (Result result : results) {
            totalCorrect0 += result.correct0;
            totalCorrect1 += result.correct1;
            totalInCorrect0 += result.incorrect0;
            totalInCorrect1 += result.incorrect1;
        }

        System.out.println("\t\t\t\t\t Classified as 1 \t\t Classified as 0");
        System.out.println("Actual class is 1 \t\t"  + totalCorrect1   + "\t\t\t\t|\t\t" + totalInCorrect1);
        System.out.println("Actual class is 0 \t\t"  + totalInCorrect0 + "\t\t\t\t|\t\t" + totalCorrect0);
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

        if (printTree)
            System.out.println(dt);

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