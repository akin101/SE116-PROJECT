public class Main {
    //main metodu-ege-yege-akın
    public static void main(String[] args) {
        args = new String[]{"map00.txt", "10"};

        if (args.length < 2) {
            System.out.println("Usage: java Main <map-file> <ticks>");
            return;
        }

        String mapFile = args[0];
        int ticks;

        try {
            ticks = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Tick count must be an integer.");
            return;
        }

        if (ticks < 0) {
            System.out.println("Tick count cannot be negative.");
            return;
        }

        try {
            City city = MapLoader.load(mapFile);

            for (int i = 1; i <= ticks; i++) {
                System.out.println("Tick " + i);
                city.tick();
            }

        } catch (IOException e) {
            System.out.println("Could not read map file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid map: " + e.getMessage());
        }
    }
}
