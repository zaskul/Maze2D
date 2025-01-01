
import java.util.*;

public class Maze2D {
    static boolean debug = false;
    int height, width;
    int[][] maze;
    boolean[][] visited;
    int[] base;

    Maze2D(int x, int y) {
        width = x;
        height = y;
        maze = new int[height][width];
        visited = new boolean[height][width];
        base = new int[height * width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = 0;
                visited[i][j] = false;
                base[width * i + j] = -1;
            }
        }
    }
    // used to store the cell index info as one array
    int cellIndex(int y, int x) {
        return width * y + x;
    }

    // traces back to the origin of the cell
    // makes sure that the algorithm doesn't go through walls
    int baseCell(int index) {
        while (base[index] >= 0) {
            index = base[index];
        }
        return index;
    }

    // if the cells have the same base they are in the same chain
    // therefore they can be merged together into one chain
    void merge(int index1, int index2) {
        int base1 = baseCell(index1);
        int base2 = baseCell(index2);
        base[base2] = base1;
    }

    void generateMazePath(int x, int y) {
    int indexSrc, indexDest;
    visited[y][x] = true;

    // check if all neighbouring cells are already visited
    // if true go back to previous cell
    boolean allVisited =
            (x <= 0 || visited[y][x - 1]) && // WEST
            (x >= width - 1 || visited[y][x + 1]) && // EAST
            (y <= 0 || visited[y - 1][x]) && // NORTH
            (y >= height - 1 || visited[y + 1][x]); // SOUTH

    if (debug) {
            System.out.println("CELL: (X: " + x + " Y: " + y + ") \n" + allVisited);
            System.out.printf("%15s\n %10s + %5s \n%15s%n",
                    y > 0 ? visited[y - 1][x] : "OOB Y+",
                    x > 0 ? visited[y][x - 1] : "OOB X-",
                    x < width - 1 ? visited[y][x + 1] : "OOB X+",
                    y < height - 1 ? visited[y + 1][x] : "OOB Y-");
            System.out.println("-----------");
//            for (int[] row : maze) {
//                System.out.println(Arrays.toString(row));
//            }
            for (boolean[] row : visited) {
                System.out.println(Arrays.toString(row));
            }
            System.out.println("----------------------------------");
        }

        if ( allVisited ) {
            return;
        }

        List<Integer> directions = Arrays.asList(1, 2, 4, 8);
        Collections.shuffle(directions);

        indexSrc = cellIndex(y, x);

        for (int tempDir : directions) {
            if (tempDir == 1 && x > 0) { // WEST
                indexDest = cellIndex(y, x - 1);
                if (baseCell(indexDest) != baseCell(indexSrc)) {
                    merge(indexSrc, indexDest);
                    maze[y][x] |= 1;
                }
                generateMazePath(x - 1, y);
            }
            if (tempDir == 2 && x < width - 1) { // EAST
                indexDest = cellIndex(y, x + 1);
                if (baseCell(indexDest) != baseCell(indexSrc)) {
                    merge(indexSrc, indexDest);
                    maze[y][x] |= 1;
                }
                generateMazePath(x + 1, y);
            }
            if (tempDir == 4 && y > 0) { // NORTH
                indexDest = cellIndex(y - 1, x);
                if (baseCell(indexDest) != baseCell(indexSrc)) {
                    merge(indexSrc, indexDest);
                    maze[y][x] |= 2;
                }
                generateMazePath(x, y - 1);
            }
            if (tempDir == 8 && y < height - 1) { // SOUTH
                indexDest = cellIndex(y + 1, x);
                if (baseCell(indexDest) != baseCell(indexSrc)) {
                    merge(indexSrc, indexDest);
                    maze[y][x] |= 2;
                }
                generateMazePath(x, y + 1);
            }
        }
    }

    public static void main(String[] args) {
        Maze2D maze = new Maze2D(8, 8);
        debug = true;
        maze.generateMazePath(1,2);
        for (int[] row : maze.maze) {
            System.out.println(Arrays.toString(row));
        }
//        maze.printMazePath();
        System.out.println(Arrays.toString(maze.base));
    }

}
