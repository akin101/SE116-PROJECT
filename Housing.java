class Housing extends Zone {

    public Housing(int row, int col) {
        super(row, col, 'H');
    }

    @Override
    protected int currentOutputForDemand() {
        return Math.max(1, populationOutput);
    }

    @Override
    public String getZoneType() {
        return "House";
    }

    @Override
    protected boolean hasRequiredUtilitiesForLevelOne() {
        return hasAllBasicUtilities();
    }

    @Override
    protected boolean canReachLevelOne() {
        return hasAllBasicUtilities();
    }

    @Override
    protected boolean canReachLevelTwo() {
        return level >= 1 && hasSecurity && hasHealth && hasEducation;
    }

    @Override
    protected boolean canReachLevelThree() {
        return level >= 2 && lifestyleReceived > 0;
    }

    @Override
    public void calculateOutput() {
        populationOutput = 0;

        if (level <= 0) {
            return;
        }

        int m = minimumUtilityReceived();

        switch (level) {
            case 1:
                populationOutput = m;
                break;
            case 2:
                populationOutput = 2 * m;
                break;
            case 3:
                populationOutput = 2 * m + lifestyleReceived;
                break;
        }
    }
}