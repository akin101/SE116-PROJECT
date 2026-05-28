abstract class UtilityProvider extends Cell {

    private static final int DEFAULT_CAPACITY = 100;
    private final String utilityType;
    private final int capacity;

    protected UtilityProvider(int row, int col, char symbol, String utilityType) {
        super(row, col, symbol);
        this.utilityType = utilityType;
        this.capacity = DEFAULT_CAPACITY;
    }

    public String getUtilityType() {
        return utilityType;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean isConnectable() {
        return false;
    }
}