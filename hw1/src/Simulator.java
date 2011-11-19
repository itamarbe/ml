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
        dt.train(instances,false);

        System.out.println(dt);
    }

    public List<Result> bulkRun(List<Instance> instances, int numberOfRuns, double testPercent) {
        List<Result> resultList = new ArrayList<Result>();

        for (int run = 1; run <= numberOfRuns; run++) {
            Pair<List<Instance>> instancesPair = splitDataSet(instances, testPercent);
            List<Instance> train = instancesPair.first;
            List<Instance> test = instancesPair.second;

            resultList.add(evaluate(train, test));
        }

        return resultList;
    }

    public Result evaluate(List<Instance> train, List<Instance> test) {
        Result result = new Result();

        RandomDT dt = new RandomDT(random);
        dt.train(train,true);

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

        System.out.println("\n*************************************************************");
        System.out.println("\t\t\t\t\t Classified as 1 \t\t Classified as 0");
        System.out.println("Actual class is 1 \t\t"  + totalCorrect1   + "\t\t\t\t|\t\t" + totalInCorrect1);
        System.out.println("Actual class is 0 \t\t"  + totalInCorrect0 + "\t\t\t\t|\t\t" + totalCorrect0);
    }

    public void plotOverallAccuracy(List<Result> results) {
        List<Double> overallAccuracy = new ArrayList<Double>();
        for (Result result : results) {
            overallAccuracy.add(result.getOverallAccuracy());
        }

        System.out.println("\n******************");
        System.out.println("Overall accuracy:");
        printStatistics(overallAccuracy);
    }

    public void plotPositiveAccuracy(List<Result> results) {
        List<Double> positiveAccuracy = new ArrayList<Double>();
        for (Result result : results) {
            positiveAccuracy.add(result.getPositiveAccuracy());
        }

        System.out.println("\n******************");
        System.out.println("Positive accuracy:");
        printStatistics(positiveAccuracy);

    }

    public void plotNegativeAccuracy(List<Result> results) {
        List<Double> negativeAccuracy = new ArrayList<Double>();
        for (Result result : results) {
            negativeAccuracy.add(result.getNegativeAccuracy());
        }

        System.out.println("\n******************");
        System.out.println("Negative accuracy:");
        printStatistics(negativeAccuracy);
    }

    private void printStatistics(List<Double> accuracy){
        Statistics st = new Statistics(accuracy);
        System.out.println("Max: " + st.getMax());
        System.out.println("Min: " + st.getMin());
        System.out.println("Median: " + st.getMedian());
        System.out.println("Average: " +st.getMean());
        System.out.println("Std: " +st.getStdDev());
    }

}
