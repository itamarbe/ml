import java.util.List;

/**
 * User: itamar
 * Date: 11/18/11
 * Time: 10:29 AM
 */
public class Main {
    public static void main(String[] args) {
        String path = "/home/amir/school/ml/hw1/data/liver-disorders.csv";

        if (args.length > 0)
            path = args[0];

        final List<Instance> instances = Instance.loadInstances(path);

        for (Instance instance : instances) {
            System.out.println(instance);
        }

        RandomDT dt = new RandomDT();
        dt.train(instances);

        System.out.println(dt.toString());
    }
}
