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
}
