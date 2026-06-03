import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class MapLoader {

    public static City load(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        }

        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Map file is empty.");
        }

        int rows = lines.size();
        List<Character> firstRow = parseLine(lines.get(0));
        int cols = firstRow.size();

        if (cols == 0) {
            throw new IllegalArgumentException("Map has no columns.");
        }

        Cell[][] grid = new Cell[rows][cols];

        for (int r = 0; r < rows; r++) {
            List<Character> symbols = parseLine(lines.get(r));

            if (symbols.size() != cols) {
                throw new IllegalArgumentException(
                        "All map rows must have the same length. Row " + r
                                + " has " + symbols.size() + " columns, expected " + cols + ".");
            }

            for (int c = 0; c < cols; c++) {
                grid[r][c] = createCell(symbols.get(c), r, c);
            }
        }

        return new City(grid);
    }

    private static List<Character> parseLine(String line) {
        List<Character> result = new ArrayList<>();
        String[] parts = line.trim().split("\\s+");

        if (parts.length > 1) {
            for (String part : parts) {
                if (!part.isEmpty()) {
                    result.add(part.charAt(0));
                }
            }
        } else {
            for (char ch : line.toCharArray()) {
                if (!Character.isWhitespace(ch)) {
                    result.add(ch);
                }
            }
        }

        return result;
    }

    private static Cell createCell(char symbol, int row, int col) {
        switch (Character.toUpperCase(symbol)) {
            case 'H':
                return new Housing1(row, col);
            case 'I':
                return new Industrial1(row, col);
            case 'C':
                return new Commercial1(row, col);
            case 'P':
                return new PowerPlant(row, col);
            case 'W':
                return new WaterStation(row, col);
            case 'T':
                return new InternetHub(row, col);
            case 'F':
                return new PoliceStation1(row, col);
            case 'D':
                return new Hospital1(row, col);
            case 'S':
                return new School1(row, col);
            case 'R':
                return new Road(row, col);
            case 'E':
            case '.':
                return new EmptyCell(row, col);
            default:
                throw new IllegalArgumentException("Unknown cell type: '" + symbol + "'");
        }
    }
}
