import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

class City {

    private final Cell[][] grid;
    private final int rows;
    private final int cols;

    private int storedPopulation;
    private int storedGoods;
    private int storedLifestyle;

    public City(Cell[][] grid) {
        this.grid = grid;
        this.rows = grid.length;
        this.cols = grid[0].length;
        this.storedPopulation = 0;
        this.storedGoods = 0;
        this.storedLifestyle = 0;
    }

    public void tick() {
        resetTickData();
        provideServices();
        distributeUtilities();
        distributeStoredResources();
        updateZones();
        collectNewProduction();
    }

    private void resetTickData() {
        forEachCell(cell -> {
            if (cell instanceof Zone) {
                ((Zone) cell).resetTickData();
            }
        });
    }

    private void provideServices() {
        // Education (Schools) - ONLY Housing
        forEachCell(cell -> {
            if (cell instanceof School) {
                School school = (School) cell;

                forEachCell(target -> {
                    if (!(target instanceof Housing)) {
                        return;
                    }

                    int dist = manhattanDistance(
                            school.getRow(), school.getCol(),
                            target.getRow(), target.getCol());

                    if (dist <= school.getRadius()) {
                        ((Zone) target).receiveService("education");
                        System.out.println(
                                "House at (" + target.getRow() + "," + target.getCol()
                                        + ") received education service");
                    }
                });
            }
        });

        // Security (Police Stations) - ALL Zones
        forEachCell(cell -> {
            if (cell instanceof PoliceStation) {
                PoliceStation police = (PoliceStation) cell;

                forEachCell(target -> {
                    if (!(target instanceof Zone)) {
                        return;
                    }

                    int dist = manhattanDistance(
                            police.getRow(), police.getCol(),
                            target.getRow(), target.getCol());

                    if (dist <= police.getRadius()) {
                        ((Zone) target).receiveService("security");

                        String type = "";

                        if (target instanceof Housing) {
                            type = "House";
                        } else if (target instanceof Commercial) {
                            type = "Commercial";
                        } else if (target instanceof Industrial) {
                            type = "Industrial";
                        }

                        System.out.println(
                                type + " at (" + target.getRow() + "," + target.getCol()
                                        + ") received security service");
                    }
                });
            }
        });

        // Health (Hospitals) - ONLY Housing
        forEachCell(cell -> {
            if (cell instanceof Hospital) {
                Hospital hospital = (Hospital) cell;

                forEachCell(target -> {
                    if (!(target instanceof Housing)) {
                        return;
                    }

                    int dist = manhattanDistance(
                            hospital.getRow(), hospital.getCol(),
                            target.getRow(), target.getCol());

                    if (dist <= hospital.getRadius()) {
                        ((Zone) target).receiveService("health");
                        System.out.println(
                                "House at (" + target.getRow() + "," + target.getCol()
                                        + ") received health service");
                    }
                });
            }
        });
    }

    private void distributeUtilities() {
        // Internet
        forEachCell(cell -> {
            if (cell instanceof InternetHub) {
                distributeUtilityFromProvider((InternetHub) cell, "internet");
            }
        });

        // Water
        forEachCell(cell -> {
            if (cell instanceof WaterStation) {
                distributeUtilityFromProvider((WaterStation) cell, "water");
            }
        });

        // Electricity
        forEachCell(cell -> {
            if (cell instanceof PowerPlant) {
                distributeUtilityFromProvider((PowerPlant) cell, "electricity");
            }
        });
    }

    private void distributeUtilityFromProvider(UtilityProvider provider, String utilityName) {
        boolean[][] visited = new boolean[rows][cols];
        Queue<Position> queue = new ArrayDeque<>();

        visited[provider.getRow()][provider.getCol()] = true;
        enqueueNeighbours(provider.getRow(), provider.getCol(), visited, queue);

        int remaining = provider.getCapacity();

        while (!queue.isEmpty() && remaining > 0) {
            Position pos = queue.poll();
            Cell cell = grid[pos.row][pos.col];

            if (cell instanceof Zone) {
                Zone zone = (Zone) cell;
                String type = provider.getUtilityType();

                int demand = zone.getUtilityDemand(type);
                int delivered = Math.min(demand, remaining);

                if (delivered > 0) {
                    zone.receiveUtility(type, delivered);
                    remaining -= delivered;

                    String zoneType = "";

                    if (zone instanceof Housing) {
                        zoneType = "House";
                    } else if (zone instanceof Commercial) {
                        zoneType = "Commercial";
                    } else if (zone instanceof Industrial) {
                        zoneType = "Industrial";
                    }

                    System.out.println(
                            zoneType + " at (" + zone.getRow() + "," + zone.getCol()
                                    + ") received " + delivered + " " + utilityName);
                }
            }

            enqueueNeighbours(pos.row, pos.col, visited, queue);
        }
    }

    private void enqueueNeighbours(int row, int col,
                                   boolean[][] visited, Queue<Position> queue) {
        int[][] dirs = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        for (int[] d : dirs) {
            int nr = row + d[0];
            int nc = col + d[1];

            if (isInside(nr, nc)
                    && !visited[nr][nc]
                    && grid[nr][nc].isConnectable()) {
                visited[nr][nc] = true;
                queue.add(new Position(nr, nc));
            }
        }
    }

    private void distributeStoredResources() {
        List<Housing> houses = new ArrayList<>();
        List<Industrial> industrials = new ArrayList<>();
        List<Commercial> commercials = new ArrayList<>();

        forEachCell(cell -> {
            if (cell instanceof Housing) {
                houses.add((Housing) cell);
            } else if (cell instanceof Industrial) {
                industrials.add((Industrial) cell);
            } else if (cell instanceof Commercial) {
                commercials.add((Commercial) cell);
            }
        });

        // Population HEM Industrial HEM Commercial arasında eşit bölünür
        int totalPopZones = industrials.size() + commercials.size();

        if (totalPopZones > 0 && storedPopulation > 0) {
            int eachPop = storedPopulation / totalPopZones;

            // Önce Industrial
            for (Industrial ind : industrials) {
                ind.receivePopulation(eachPop);

                if (eachPop > 0) {
                    System.out.println(
                            "Industrial at (" + ind.getRow() + "," + ind.getCol()
                                    + ") received " + eachPop + " population");
                }
            }

            // Sonra Commercial
            for (Commercial com : commercials) {
                com.receivePopulation(eachPop);

                if (eachPop > 0) {
                    System.out.println(
                            "Commercial at (" + com.getRow() + "," + com.getCol()
                                    + ") received " + eachPop + " population");
                }
            }

            storedPopulation -= eachPop * totalPopZones;
        }

        // Goods sadece Commercial'a
        if (!commercials.isEmpty() && storedGoods > 0) {
            int eachGoods = storedGoods / commercials.size();

            for (Commercial com : commercials) {
                com.receiveGoods(eachGoods);

                if (eachGoods > 0) {
                    System.out.println(
                            "Commercial at (" + com.getRow() + "," + com.getCol()
                                    + ") received " + eachGoods + " goods");
                }
            }

            storedGoods -= eachGoods * commercials.size();
        }

        // Lifestyle sadece Housing'e
        if (!houses.isEmpty() && storedLifestyle > 0) {
            int eachLifestyle = storedLifestyle / houses.size();

            for (Housing h : houses) {
                h.receiveLifestyle(eachLifestyle);

                if (eachLifestyle > 0) {
                    System.out.println(
                            "House at (" + h.getRow() + "," + h.getCol()
                                    + ") received " + eachLifestyle + " lifestyle");
                }
            }

            storedLifestyle -= eachLifestyle * houses.size();
        }
    }

    private void updateZones() {
        List<String> levelMessages = new ArrayList<>();

        forEachCell(cell -> {
            if (cell instanceof Zone) {
                Zone zone = (Zone) cell;

                int oldLevel = zone.getLevel();
                zone.updateLevel();
                int newLevel = zone.getLevel();

                if (newLevel > oldLevel) {
                    levelMessages.add(
                            zone.getZoneType() + " at (" + zone.getRow() + "," + zone.getCol()
                                    + ") levels up from " + oldLevel + " to " + newLevel);
                } else if (newLevel < oldLevel) {
                    levelMessages.add(
                            zone.getZoneType() + " at (" + zone.getRow() + "," + zone.getCol()
                                    + ") levels down from " + oldLevel + " to " + newLevel);
                }
            }
        });

        for (String msg : levelMessages) {
            System.out.println(msg);
        }
    }

    private void collectNewProduction() {
        List<String> generationMessages = new ArrayList<>();

        forEachCell(cell -> {
            if (cell instanceof Zone) {
                Zone zone = (Zone) cell;
                zone.calculateOutput();

                String zoneType = zone.getZoneType();
                String outputType = "";
                int output = 0;

                if (zone instanceof Housing) {
                    outputType = "population";
                    output = zone.getPopulationOutput();
                } else if (zone instanceof Industrial) {
                    outputType = "goods";
                    output = zone.getGoodsOutput();
                } else if (zone instanceof Commercial) {
                    outputType = "lifestyle";
                    output = zone.getLifestyleOutput();
                }

                generationMessages.add(
                        zoneType + " at (" + zone.getRow() + "," + zone.getCol()
                                + ") generated " + output + " " + outputType);

                storedPopulation += zone.getPopulationOutput();
                storedGoods += zone.getGoodsOutput();
                storedLifestyle += zone.getLifestyleOutput();
            }
        });

        for (String msg : generationMessages) {
            System.out.println(msg);
        }
    }

    private int manhattanDistance(int r1, int c1, int r2, int c2) {
        return Math.abs(r1 - r2) + Math.abs(c1 - c2);
    }

    private boolean isInside(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    private void forEachCell(Consumer<Cell> action) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                action.accept(grid[r][c]);
            }
        }
    }
}
