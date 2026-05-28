
abstract class Cell {
    //yege yapıyor ilk hafta

    protected final int row;
    protected final int col;
    protected final char symbol;

    protected Cell(int row, int col, char symbol) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isConnectable() {
        return false;
    }
}

abstract class Zone extends Cell {
    //yege ilk hafta
    protected int level;

    protected int electricityReceived;
    protected int waterReceived;
    protected int internetReceived;

    protected boolean hasSecurity;
    protected boolean hasHealth;
    protected boolean hasEducation;

    protected int populationReceived;
    protected int goodsReceived;
    protected int lifestyleReceived;

    protected int populationOutput;
    protected int goodsOutput;
    protected int lifestyleOutput;

    protected Zone(int row, int col, char symbol) {
        super(row, col, symbol);
        this.level = 0;
    }

    @Override
    public boolean isConnectable() {
        return true;
    }

    public int getLevel() {
        return level;
    }

    public void resetTickData() {
        electricityReceived = 0;
        waterReceived = 0;
        internetReceived = 0;

        hasSecurity = false;
        hasHealth = false;
        hasEducation = false;

        populationReceived = 0;
        goodsReceived = 0;
        lifestyleReceived = 0;

        populationOutput = 0;
        goodsOutput = 0;
        lifestyleOutput = 0;
    }

    public void receiveUtility(String type, int amount) {
        switch (type) {
            case "electricity":
                electricityReceived += amount;
                break;
            case "water":
                waterReceived += amount;
                break;
            case "internet":
                internetReceived += amount;
                break;
        }
    }

    public void receiveService(String type) {
        switch (type) {
            case "security":
                hasSecurity = true;
                break;
            case "health":
                hasHealth = true;
                break;
            case "education":
                hasEducation = true;
                break;
        }
    }

    public void receivePopulation(int amount) {
        populationReceived += amount;
    }

    public void receiveGoods(int amount) {
        goodsReceived += amount;
    }

    public void receiveLifestyle(int amount) {
        lifestyleReceived += amount;
    }

    public int getUtilityDemand(String type) {
        int cap = Math.max(1, currentOutputForDemand());

        switch (type) {
            case "electricity":
                return Math.max(0, cap - electricityReceived);
            case "water":
                return Math.max(0, cap - waterReceived);
            case "internet":
                return Math.max(0, cap - internetReceived);
            default:
                return 0;
        }
    }

    protected abstract int currentOutputForDemand();

    public abstract String getZoneType();

    public void updateLevel() {
        if (!hasRequiredUtilitiesForLevelOne()) {
            level = 0;
            return;
        }

        int target = targetLevel();

        if (target > level) {
            level++;
        } else if (target < level) {
            level--;
        }
    }

    private int targetLevel() {
        if (canReachLevelThree()) {
            return 3;
        }

        if (canReachLevelTwo()) {
            return 2;
        }

        if (canReachLevelOne()) {
            return 1;
        }

        return 0;
    }

    protected abstract boolean hasRequiredUtilitiesForLevelOne();

    protected abstract boolean canReachLevelOne();

    protected abstract boolean canReachLevelTwo();

    protected abstract boolean canReachLevelThree();

    public abstract void calculateOutput();

    public int getPopulationOutput() {
        return populationOutput;
    }

    public int getGoodsOutput() {
        return goodsOutput;
    }

    public int getLifestyleOutput() {
        return lifestyleOutput;
    }

    protected int minimumUtilityReceived() {
        return Math.min(electricityReceived, Math.min(waterReceived, internetReceived));
    }

    protected boolean hasAllBasicUtilities() {
        return electricityReceived > 0
                && waterReceived > 0
                && internetReceived > 0;
    }

    protected boolean hasElectricityAndWater() {
        return electricityReceived > 0 && waterReceived > 0;
    }
}