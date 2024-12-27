import javax.annotation.processing.SupportedSourceVersion;
import java.util.*;

public class Maze2D {
    static int depth = 0;
    static boolean debug = false;
    int height, width;
    int[][] maze;
    int[] mazeBases;
    boolean[][] visited;

    Maze2D(int x, int y) {
        width = x;
        height = y;
        maze = new int[height][width];
        visited = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = 0;
                visited[i][j] = false;
            }
        }
    }


    void generateMazePath(int x, int y) {


        // check if all neighbouring cells are already visited
        // if true go back to previous cell
        visited[y][x] = true;
        depth++;
        if (depth == width * height) {return;}
        boolean allVisited = (x > 0 ? visited[y][x - 1] : true ) && // WEST
                (x < width - 1 ? visited[y][x + 1] : true) && // EAST
                (y > 0 ? visited[y - 1][x] : true) && // NORTH
                (y < height - 1 ? visited[y + 1][x] : true); // SOUTH

        if ( allVisited ) {
            return;
        }
        if (debug) {
            System.out.println("-----------");
            for (boolean[] row : visited) {
                System.out.println(Arrays.toString(row));
            }
            System.out.println("-----------");

            System.out.println("THREAD: " + depth + "\n" + "CELL: (X: " + x + " Y: " + y + ") \n" + allVisited);
            System.out.println(String.format("%15s\n %10s + %5s \n%15s",
                    y > 0 ? visited[y - 1][x] : "OOB Y+",
                    x > 0 ? visited[y][x - 1] : "OOB X-",
                    x < width - 1 ? visited[y][x + 1] : "OOB X+",
                    y < height - 1 ? visited[y + 1][x] : "OOB Y-"));
        }
        List<Integer> directions = Arrays.asList(1, 2, 4, 8);
        Collections.shuffle(directions);

        for (int tempDir : directions) {

            if (tempDir == 1 && x > 0) { // WEST
                generateMazePath(x - 1, y);
                maze[y][x - 1] |= tempDir;
            }
            if (tempDir == 2 && x < width - 1) { // EAST
                generateMazePath(x + 1, y);
                maze[y][x + 1] |= tempDir;
            }
            if (tempDir == 4 && y > 0) { // NORTH
                generateMazePath(x, y - 1);
                maze[y - 1][x] |= tempDir;
            }
            if (tempDir == 8 && y < height - 1) { // SOUTH
                generateMazePath(x, y + 1);
                maze[y + 1][x] |= tempDir;
            }
        }

    }

    public static void main(String[] args) {
        Maze2D maze = new Maze2D(4, 4);
        debug = true;
        maze.generateMazePath(1,2);

    }

}
