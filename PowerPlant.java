class PowerPlant extends UtilityProvider {

    public PowerPlant(int row, int col) {
        super(row, col, 'P', "electricity");
    }
}

class WaterStation extends UtilityProvider {

    public WaterStation(int row, int col) {
        super(row, col, 'W', "water");
    }
}

class InternetHub extends UtilityProvider {

    public InternetHub(int row, int col) {
        super(row, col, 'T', "internet");
    }
}
