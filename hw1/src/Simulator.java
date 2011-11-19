import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Simulator {


    private Random random = new Random();

    public Simulator(boolean randomize) {
        random = randomize ? new Random() : new Random(0);
    }


    public void plotDecisionTree(List<Instance> instances){
        RandomDT dt = new RandomDT(random);
        dt.train(instances);

        System.out.println(dt);
    }

    public List<Result> bulkRun(List<Instance> instances, int numberOfRuns, double testPercent) {
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

    public Result evaluate(List<Instance> train, List<Instance> test) {
        Result result = new Result();

        RandomDT dt = new RandomDT(random);
        dt.train(train);

        for (Instance testInstance : test) {
            double classifiedLabel = dt.classify(testInstance);
            double trueLabel = testInstance.getLabelValue();

            result.update(classifiedLabel, trueLabel);
        }

        return result;
    }

    public Pair<List<Instance>> splitDataSet(List<Instance> instances, double percent) {
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

    public void plotConfusionMatrix(List<Result> results) {
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
}
