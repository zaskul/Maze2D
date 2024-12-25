import javax.annotation.processing.SupportedSourceVersion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Maze2D {
    int height, width;
    int[][] maze;
    int[][] mazePath;
    int[] mazeBases;

    Maze2D(int x, int y) {
        width = x;
        height = y;
        maze = new int[height][width];
        mazePath = new int[height][width];
        mazeBases = new int[height * width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                mazePath[i][j] = -1;
                maze[i][j] = 0;
                mazeBases[getCellIndex(i, j)] = -1;
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

    void generateMazePath(int x, int y, int dir) {
            if (dir == 15) return;
            int currentIndex = getCellIndex(x, y);
            int destinationIndex, tempDir;
            boolean found;
            System.out.println("XD" + x + y);
            Random r = new Random();
            do {
                tempDir = (int) Math.pow(2, r.nextInt(4));
                if((dir & tempDir) != 0) {
                    found = true;
                } else {
                    found = false;
                }
            } while (found == true && dir != 15);

            dir |= tempDir;

            switch (tempDir) {
                case 1:
                    if (x > 0) { // travel west
                        destinationIndex = getCellIndex(x - 1, y);
                        if (currentIndex != destinationIndex) {
                            mergeBases(currentIndex, destinationIndex);
                            generateMazePath(x - 1, y, dir);
                            maze[x][y] |= 1;
                            dir = 0;
                        }
                    }
                    break;
                case 2:
                    if (x < width - 1) {  // travel east
                        destinationIndex = getCellIndex(x + 1, y);
                        if (currentIndex != destinationIndex) {
                            mergeBases(currentIndex, destinationIndex);
                            generateMazePath(x + 1, y, dir);
                            maze[x + 1][y] |= 1;
                            dir = 0;

                        }
                    }
                    break;
                case 4:
                    if (y > 0) { // travel south
                        destinationIndex = getCellIndex(x, y - 1);
                        if (currentIndex != destinationIndex) {
                            mergeBases(currentIndex, destinationIndex);
                            generateMazePath(x, y - 1, dir);
                            maze[x][y] |= 2;
                            dir = 0;

                        }
                    }
                    break;
                case 8:
                    if (y < height - 1) { // travel north
                        destinationIndex = getCellIndex(x, y + 1);
                        if (currentIndex != destinationIndex) {
                            mergeBases(currentIndex, destinationIndex);
                            generateMazePath(x, y + 1, dir);
                            maze[x][y + 1] |= 2;
                            dir = 0;

                        }
                    }
                    break;
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
        Maze2D maze = new Maze2D(6, 6);
        maze.generateMazePath(2,3, 0);
        for (int[] el : maze.maze) {
            System.out.println(Arrays.toString(el));
        }

    }

}
