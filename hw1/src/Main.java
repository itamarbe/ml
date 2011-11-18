import com.sun.xml.internal.bind.v2.TODO;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: itamar
 * Date: 11/18/11
 * Time: 10:29 AM
 */
public class Main {
    public static void main(String[] args) {
        String path = "/home/amir/school/ml/hw1/data/liver-disorders.csv" ;

        if (args.length > 0)
            path = args[0];

        final List<Instance> instances = Instance.loadInstances(path);

//        for (Instance instance : instances) {
//            System.out.println(instance);
//        }

        List<Instance> train,test;
        Pair<List<Instance>> instancesPair;
        RandomDT dt;
        final int numberOfRuns = 2;
        final double testPercent = 0.2;
        Random random = new Random(0); //TODO: replace seed
        List<Result> resultList = new ArrayList<Result>();

        for (int run=0;run<numberOfRuns;run++) {
            System.out.println("Run #" + run);
            instancesPair = splitTrainTest(instances,random,testPercent);
            train = instancesPair.first;
            test = instancesPair.second;
            dt = new RandomDT();
            dt.train(train);
            Result result = new Result();
            for (Instance instance : test) {
                double label = dt.classify(instance);
                if (label == instance.getLabelValue()) {
                    if (label == 1.0) {
                        result.correct1++;
                    } else {
                        result.correct0++;
                    }
                } else {
                    if (label == 0.0) {
                        result.incorrect1++;
                    } else {
                        result.incorrect0++;
                    }
                }
            }
            resultList.add(result);
        }


        //System.out.println(dt.toString());
    }

    public static Pair<List<Instance>> splitTrainTest(List<Instance> instances,Random random,double testPercent) {
        List<Instance> train = new ArrayList<Instance>();
        List<Instance> test = new ArrayList<Instance>();

        for (Instance instance : instances) {
            if (random.nextDouble() < testPercent) {
                test.add(instance);
            } else {
                train.add(instance);
            }
        }

        return new Pair<List<Instance>>(train,test);
    }
}