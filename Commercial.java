class Commercial extends Zone {

    public Commercial(int row, int col) {
        super(row, col, 'C');
    }

    @Override
    protected int currentOutputForDemand() {
        return Math.max(1, lifestyleOutput);
    }

    @Override
    public String getZoneType() {
        return "Commercial";
    }

    @Override
    protected boolean hasRequiredUtilitiesForLevelOne() {
        return hasAllBasicUtilities();
    }

    @Override
    protected boolean canReachLevelOne() {
        return populationReceived > 0
                && goodsReceived > 0
                && hasAllBasicUtilities();
    }

    @Override
    protected boolean canReachLevelTwo() {
        return level >= 1 && hasSecurity;
    }

    @Override
    protected boolean canReachLevelThree() {
        int threshold = Math.max(1, lifestyleOutput);

        return level >= 2
                && populationReceived > threshold
                && goodsReceived > threshold;
    }

    @Override
    public void calculateOutput() {
        lifestyleOutput = 0;

        if (level <= 0) {
            return;
        }

        int m = minimumUtilityReceived();

        switch (level) {
            case 1:
                lifestyleOutput = m;
                break;
            case 2:
                lifestyleOutput = 2 * m;
                break;
            case 3:
                lifestyleOutput = 2 * m
                        + Math.min(populationReceived, goodsReceived);
                break;
        }
    }
}