import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: itamar
 * Date: 11/19/11
 * Time: 12:53 PM
 */
public class Statistics {
    private List<Double> data;
    private int size;

    public Statistics(List<Double> data) {
        this.data = data;
        size = data.size();
    }

    double getMax() {
        return Collections.max(data);
    }

    double getMin() {
        return Collections.min(data);
    }

    double getMean() {
        double sum = 0.0;
        for (double a : data)
            sum += a;
        return sum / size;
    }

    double getVariance() {
        double mean = getMean();
        double temp = 0;
        for (double a : data)
            temp += (mean - a) * (mean - a);
        return temp / size;
    }

    double getStdDev() {
        return Math.sqrt(getVariance());
    }

    public double getMedian() {
        Double[] b = new Double[size];
        System.arraycopy(data.toArray(b), 0, b, 0, b.length);
        Arrays.sort(b);

        if (size % 2 == 0) {
            return (b[(b.length / 2) - 1] + b[b.length / 2]) / 2.0;
        } else {
            return b[b.length / 2];
        }
    }
}