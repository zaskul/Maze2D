import java.util.*;

public class Maze2D {
    int height, width;
    int[][] maze;
    boolean[][] visited;
    int[] base;

    // maze init
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
    void mergeBases(int index1, int index2) {
        int base1 = baseCell(index1);
        int base2 = baseCell(index2);
        base[base2] = base1;
    }

    void inspectMazeData() {
        for (int[] row : maze) {
            System.out.println(Arrays.toString(row));
        }
    }

    void generateMazePath(int x, int y, Random seed) {
        int indexSrc, indexDest;
        visited[y][x] = true;

        // check if all neighbouring cells are already visited
        // if true go back to previous cell
        if (    (x <= 0 || visited[y][x - 1]) && // WEST
                (x >= width - 1 || visited[y][x + 1]) && // EAST
                (y <= 0 || visited[y - 1][x]) && // NORTH
                (y >= height - 1 || visited[y + 1][x]) // SOUTH
        ) {
            return;
        }

        // randomize maze traverse order
        List<Integer> directions = Arrays.asList(1, 2, 4, 8);
        Collections.shuffle(directions, seed);

        // traverse through the maze
        indexSrc = cellIndex(y, x);
        for (int currentDirection : directions) {
            // WEST
            if (currentDirection == 1 && x > 0) {
                indexDest = cellIndex(y, x - 1);
                if (baseCell(indexDest) != baseCell(indexSrc)) {
                    mergeBases(indexSrc, indexDest);
                    maze[y][x] |= 2;
                }
                generateMazePath(x - 1, y, seed);
            }

            // EAST
            if (currentDirection == 2 && x < width - 1) {
                indexDest = cellIndex(y, x + 1);
                if (baseCell(indexDest) != baseCell(indexSrc)) {
                    mergeBases(indexSrc, indexDest);
                    maze[y][x + 1] |= 2;
                }
                generateMazePath(x + 1, y, seed);
            }

            // NORTH
            if (currentDirection == 4 && y > 0) {
                indexDest = cellIndex(y - 1, x);
                if (baseCell(indexDest) != baseCell(indexSrc)) {
                    mergeBases(indexSrc, indexDest);
                    maze[y][x] |= 1;
                }
                generateMazePath(x, y - 1, seed);
            }

            // SOUTH
            if (currentDirection == 8 && y < height - 1) {
                indexDest = cellIndex(y + 1, x);
                if (baseCell(indexDest) != baseCell(indexSrc)) {
                    mergeBases(indexSrc, indexDest);
                    maze[y + 1][x] |= 1;
                }
                generateMazePath(x, y + 1, seed);
            }
        }
    }

    void drawMaze() {
        String[][] mazeWallsColored = new String[(height * 2) + 1][(width * 2) + 1];
        String wallBackground = "\u001B[48;5;0m";
        String pathBackground = "\u001B[48;5;15m";
        String reset = "\u001B[0m";

        // place walls based on the maze data
        int verticalIndex;
        for (int i = 0; i < width; i++) {
            verticalIndex = 0;
            for (int j = 0; j < height; j++) {
                if ((maze[i][j] & (byte) 1) == 0) {
                    mazeWallsColored[(i * 2)][(j * 2)] = wallBackground;
                    mazeWallsColored[(i * 2)][(j * 2) + 1] = wallBackground;
                    mazeWallsColored[(i * 2)][(j * 2) + 2] = wallBackground;
                }
                if (((maze[i][j] & (byte) 2) == 0)) {
                    mazeWallsColored[(i * 2)][verticalIndex] = wallBackground;
                    mazeWallsColored[(i * 2) + 1][verticalIndex] = wallBackground;
                    mazeWallsColored[(i * 2) + 2][verticalIndex] = wallBackground;
                }
                verticalIndex += 2;
            }
        }

        // left anb right border
        for (int i = 0; i < (height * 2) + 1; i++) {
            mazeWallsColored[i][0] = wallBackground;
            mazeWallsColored[i][(height * 2)] = wallBackground;
        }

        // top and bottom border
        for (int i = 0; i < (width * 2) + 1; i++) {
            mazeWallsColored[0][i] = wallBackground;
            mazeWallsColored[(width * 2)][i] = wallBackground;
        }

        // render out the maze
        for (String[] row : mazeWallsColored) {
            for (String col : row) {
                System.out.print(Objects.requireNonNullElse(col, pathBackground) + "   " + reset);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Maze2D maze = new Maze2D(15, 15);
        Random seed = new Random(5);
        maze.generateMazePath(2, 1, seed);
        maze.drawMaze();
        maze.inspectMazeData();
    }
}