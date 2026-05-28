abstract class ServiceBuilding extends Cell {

    private final String serviceType;
    private final int radius;

    protected ServiceBuilding(int row, int col, char symbol,
                              String serviceType, int radius) {
        super(row, col, symbol);
        this.serviceType = serviceType;
        this.radius = radius;
    }

    public String getServiceType() {
        return serviceType;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public boolean isConnectable() {
        return false;
    }
}