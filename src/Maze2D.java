import javax.annotation.processing.SupportedSourceVersion;
import java.util.*;

public class Maze2D {
    static int depth = 0;
    int height, width;
    int[][] maze;
    int[][] mazePath;
    int[] mazeBases;
    boolean[][] visited;

    Maze2D(int x, int y) {
        width = x;
        height = y;
        maze = new int[height][width];
        mazePath = new int[height][width];
        mazeBases = new int[height * width];
        visited = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                mazePath[i][j] = -1;
                maze[i][j] = 0;
                mazeBases[getCellIndex(i, j)] = -1;
                visited[i][j] = false;
            }
        }
    }

    int getCellIndex(int x, int y) {
        return (width * y + x);
    }

    int getBaseCellIndex(int index) {
        while (mazeBases[index] >= 0) {
            index = mazeBases[index];
        }
        return index;
    }

    void mergeBases(int index1, int index2) {
        int base1 = getBaseCellIndex(index1);
        int base2 = getBaseCellIndex(index2);
        mazeBases[base2] = base1;
    }

    boolean isVisited(int x, int y) {
        if (x < 0 || x >= maze.length || y < 0 || y >= maze[0].length) {
            return false;
        }
        return (maze[x][y] & 0xF) != 0;
    }

    boolean visitedAllNeighbours(int x, int y) {
        return (x <= 0 && !isVisited(x - 1, y)) ||
                (x > width - 1 && !isVisited(x + 1, y)) ||
                (y <= 0 && !isVisited(x, y - 1)) ||
                (y > height - 1 && !isVisited(x, y + 1));
    }

    void generateMazePath(int x, int y, int dir) {
        System.out.println("Current cell: (" + x + ", " + y + "), dir: " + dir);
        visited[x][y] = true;
        if (visitedAllNeighbours(x, y)) {
            return;
        }

        int destinationIndex;
        boolean found;

        List<Integer> directions = Arrays.asList(1, 2, 4, 8);
        Collections.shuffle(directions);

        for (int tempDir : directions) {
            if ((dir & tempDir) == 0) {
                int currentIndex = getCellIndex(x, y);
                switch (tempDir) {
                    case 1:
                        if (x > 0  && !visited[x - 1][y]) { // travel west
                            depth += 1;
                            destinationIndex = getCellIndex(x - 1, y);
                            if (!isVisited(x - 1, y) && (destinationIndex != currentIndex)) {
                                mergeBases(currentIndex, destinationIndex);
                                generateMazePath(x - 1, y, dir | tempDir);
                                maze[x][y] |= (byte) 1;
                            }
                        }
                        break;
                    case 2:
                        if (x < width - 1 && !visited[x + 1][y]) {  // travel east
                            depth += 2;
                            destinationIndex = getCellIndex(x + 1, y);
                            if (!isVisited(x + 1, y) && (destinationIndex != currentIndex)) {
                                mergeBases(currentIndex, destinationIndex);
                                generateMazePath(x + 1, y, dir | tempDir);
                                maze[x + 1][y] |= (byte) 1;
                            }
                        }
                        break;
                    case 4:
                        if (y > 0&& !visited[x][y - 1]) { // travel south
                            depth += 4;
                            destinationIndex = getCellIndex(x, y - 1);
                            if (!isVisited(x, y - 1) && (destinationIndex != currentIndex)) {
                                mergeBases(currentIndex, destinationIndex);
                                generateMazePath(x, y - 1, dir | tempDir);
                                maze[x][y] |= (byte) 2;
                            }
                        }
                        break;
                    case 8:
                        if (y < height - 1 && !visited[x][y + 1]) { // travel north
                            depth += 8;
                            destinationIndex = getCellIndex(x, y + 1);
                            if (!isVisited(x, y + 1) && (destinationIndex != currentIndex)) {
                                mergeBases(currentIndex, destinationIndex);
                                generateMazePath(x, y + 1, dir | tempDir);
                                maze[x][y + 1] |= (byte) 2;
                            }
                        }
                        break;
                }
            }
        }
    }


    int[] generateRandomPermutation(int n) {
        int[] permutation = new int[n + 1];
        StringBuilder currentNumberString = new StringBuilder();
        Random r = new Random();
        int index = 0;
        int randomNumber;
        while (index < 4) {
            randomNumber = r.nextInt(n + 1);
            if (!currentNumberString.toString().contains(String.valueOf(randomNumber))) {
                permutation[index] = randomNumber;
                currentNumberString.append(randomNumber);
                index++;
            }
        }
        return permutation;
    }

    public static void main(String[] args) {
        Maze2D maze = new Maze2D(8, 8);
        maze.generateMazePath(1,2, 0);
        for (int[] el : maze.maze) {
            System.out.println(Arrays.toString(el));
        }
        System.out.println(Arrays.toString(maze.mazeBases));

    }

}
