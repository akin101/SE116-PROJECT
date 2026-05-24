
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
    }}