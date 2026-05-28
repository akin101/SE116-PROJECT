public class Road extends Cell {

    public Road(int row, int col) {
        super(row, col, 'R');
    }

    @Override
    public boolean isConnectable() {
        return true;
    }
}