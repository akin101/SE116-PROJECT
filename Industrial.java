class Industrial extends Zone {

    public Industrial(int row, int col) {
        super(row, col, 'I');
    }

    @Override
    protected int currentOutputForDemand() {
        return Math.max(1, goodsOutput);
    }

    @Override
    public String getZoneType() {
        return "Industrial";
    }

    @Override
    protected boolean hasRequiredUtilitiesForLevelOne() {
        return hasElectricityAndWater();
    }

    @Override
    protected boolean canReachLevelOne() {
        return populationReceived > 0 && hasElectricityAndWater();
    }

    @Override
    protected boolean canReachLevelTwo() {
        return level >= 1 && hasSecurity;
    }

    @Override
    protected boolean canReachLevelThree() {
        return level >= 2 && populationReceived > Math.max(1, goodsOutput);
    }

    @Override
    public void calculateOutput() {
        goodsOutput = 0;

        if (level <= 0) {
            return;
        }

        int m = minimumUtilityReceived();

        switch (level) {
            case 1:
                goodsOutput = m;
                break;
            case 2:
                goodsOutput = 2 * m;
                break;
            case 3:
                goodsOutput = 2 * m + populationReceived;
                break;
        }
    }
}