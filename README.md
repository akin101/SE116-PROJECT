# Grid-Based City Simulation Engine

A robust, object-oriented, grid-based city simulation engine implemented in Java. Inspired by classic city-building games like *SimCity*, this engine manages complex subsystems including utility distribution via Graph Traversal (BFS), spatial public service allocation using Manhattan distance, resource economics, and dynamic zone development across synchronous time steps ("ticks").

---

## 🛠️ Project Contributors (The Triplets)
Akın Arga
Ege Boyacı
Yiğit Ege Doğan

---

## 🚀 Key Features

* **Grid Map Architecture (`Cell` Hierarchy):** Every entity on the map inherits from a base `Cell` class. This includes developmental `Zone` types (Housing, Industrial, Commercial), `UtilityProvider` facilities, `ServiceBuilding` entities, and infrastructure like `Road` blocks.
* **Smart Utility Distribution (BFS):** Power Plants ($P$), Water Stations ($W$), and Internet Hubs ($T$) dynamically route resources through interconnected transportation networks (`Road` and `Zone` elements) using a **Breadth-First Search (BFS)** algorithm that respects capacity thresholds and spatial demands.
* **Spatial Service Coverage:** Public structures like Schools ($S$), Police Stations ($F$), and Hospitals ($D$) project essential services to targeted zones within explicit radii evaluated via **Manhattan Distance**:
  $$D_M = |x_1 - x_2| + |y_1 - y_2|$$
* **Resource Loop & Economic Engine:** The city maintains a synchronized supply chain loop. Houses generate *Population*, Industrial zones use population to craft *Goods*, and Commercial zones ingest both to produce *Lifestyle* enhancements.
* **Dynamic Zone Evolution (Leveling System):** Binalar (Zones) automatically level up (Levels 0–3) or downgrade based on their access to utilities, local security, health facilities, education, and raw structural resources.

---

## 🗺️ Map Symbol Guide

The engine reads ASCII text maps where each character represents a distinct tile:

| Symbol | Cell Type | Role / Function |
| :---: | :--- | :--- |
| **`H`** | Housing | Generates Population; requires basic utilities and social services to level up. |
| **`I`** | Industrial | Produces Goods; requires Electricity, Water, and Population. |
| **`C`** | Commercial | Creates Lifestyle; requires all utilities, Population, and Goods. |
| **`P`** | Power Plant | Supplies `electricity` up to its designated capacity. |
| **`W`** | Water Station | Supplies `water` up to its designated capacity. |
| **`T`** | Internet Hub | Supplies `internet` up to its designated capacity. |
| **`F`** | Police Station | Emits `security` within a Manhattan radius of 5. |
| **`D`** | Hospital | Emits `health` services within a Manhattan radius of 3. |
| **`S`** | School | Emits `education` services within a Manhattan radius of 4. |
| **`R`** | Road | Acts as a critical bridge/connector for utility pipelines. |
| **`.`** / **`E`**| Empty Cell | Unoccupied plots of land; no connectivity. |

---

## ⏱️ Simulation Cycle (`tick` Lifecycle)

During every discrete simulation step (`tick`), the engine executes the following phases sequentially:
1. **`resetTickData()`**: Flushes the temporary data/resource registries of all zones from the previous step.
2. **`provideServices()`**: Service buildings scan the grid and apply health, security, or education flags to zones within their distance boundary.
3. **`distributeUtilities()`**: Providers run BFS traversals to feed electricity, water, and internet to hungry zones along connectable pathways.
4. **`distributeStoredResources()`**: Global resource pools (Population, Goods, Lifestyle) are mathematically distributed back into respective zones to satisfy production prerequisites.
5. **`updateZones()`**: Zones evaluate their fulfillment conditions and safely level up or tier down, logging structural transformations.
6. **`collectNewProduction()`**: Active zones compute their latest resource outputs based on their current level and efficiency, caching them back into global storage for the next cycle.

---

