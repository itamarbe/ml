class Result {
    public int correct0 = 0;
    public int correct1 = 0;
    public int incorrect0 = 0;
    public int incorrect1 = 0;

    public void update(double classifiedLabel, double trueLabel) {
        if (classifiedLabel == trueLabel) {
            if (classifiedLabel == 1.0) {
                correct1++;
            } else {
                correct0++;
            }
        } else {
            if (classifiedLabel == 0.0) {
                incorrect1++;
            } else {
                incorrect0++;
            }
        }
    }

    public double getOverallAccuracy() {
        double sum = correct0 + correct1 + incorrect0 + incorrect1;

        if (sum == 0) return sum;

        return (correct0 + correct1) / (sum);
    }

    public double getPositiveAccuracy() {
        double sum = correct1 + incorrect1;
        if (sum == 0) return sum;

        return (double) (correct1) / (sum);
    }

    public double getNegativeAccuracy() {
        double sum = correct0 + incorrect0;
        if (sum == 0) return sum;
        return (double) (correct0) / (sum);
    }
}
