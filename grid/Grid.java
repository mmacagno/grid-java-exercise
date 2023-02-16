// grid.java
// A business object maintaining a NxM grid of Integer
// The grid also uses an optional validator to filter out unwanted values

package grid;

import interfaces.*;
import exceptions.*;

public class Grid {
    private static final int MAX_ROWS = 10;
    private static final int MAX_COLUMNS = 10;

    private int rows, columns;
    private int[][] grid;
    private EntryValidator validator;

    public Grid(int rows, int columns, EntryValidator validator) {
        if ((rows <= 0) || (rows > MAX_ROWS) || (columns <= 0) || (columns > MAX_COLUMNS)) {
            throw new IllegalArgumentException(String.format("Invalid dimensions: %d, %d", rows, columns));
        }

        this.rows = rows;
        this.columns = columns;
        this.grid = new int[rows][columns];

        this.validator = validator;
    }
    
    public void set(int row, int column, int value) throws InvalidValueException {
        if ((row < 0) || (row >= this.rows) || (column < 0) || (column >= this.columns)) {
            throw new IllegalArgumentException(String.format("Invalid index: %d, %d", row, column));
        }
        
        if ((validator != null) && !validator.validate(value)) {
            throw new InvalidValueException(value);
        }

        this.grid[row][column] = value;
    }

    public int get(int row, int column) {
        if ((row < 0) || (row >= this.rows) || (column < 0) || (column >= this.columns)) {
            throw new IllegalArgumentException(String.format("Invalid index: %d, %d", row, column));
        }

        return this.grid[row][column];
    }

    public int calculateLargestSum(int depth) {
        // Given the recursive function to calculate the max sum for a given (row, column) starting point,
        // we visit the grid completely to determine the best sum
        int largestValue = -1;

        for (int row = 0; row < rows; row ++) {
            for (int column = 0; column < columns; column ++) {
                // System.out.println(String.format("Visiting (%d,%d), depth: %d", row, column, depth));
                largestValue = Integer.max(largestValue, this.calculateMaxSum(row, column, depth));
            }
        }

        return largestValue;
    }

    // This function calculates the max sum available where the starting point is on (row, column), moving down or right
    // Since we cannot reuse combinations in opposite direction, it is useless to go up or left
    // we stop short when the available cells down + right would not be sufficient to reach the depth
    public int calculateMaxSum(int row, int column, int depth) {
        // System.out.println(String.format("Calculating max for (%d,%d), depth: %d", row, column, depth));

        if (depth <= 0) {
            // System.out.println(String.format("(%d,%d), depth: %d, DEAD END", row, column, depth));
            return -1;
        }

        if (depth == 1) {
            // System.out.println(String.format("(%d,%d), depth: %d, value: %d", row, column, depth, grid[row][column]));
            return grid[row][column];
        }

        int maxSum = -1;

        // Calculate max for all possible combinations of depth down and depth right
        // Starting from this position, calculate the valid variants
        // For example, if I am at [0,0], with depth 3, I could sum:
        // (0,0)+(1,0)+(2,0) (3 vertical)
        // (0,0)+(0,1)+(0,2) (3 horizontal)
        // (0,0)+(1,0)+(0,1) (current value, 1 down and 1 right)
        // This would be sufficient for the case of sum of three, generalizing for any depth we need to calculate all the variations:
        // N horizontal, N vertical, N-1 vertical and 1 horizontal, etc.
        for (int downDepth = 0; downDepth <= depth - 1; downDepth++)
        {
            if (row + 1 < this.rows) {
                maxSum = Integer.max(maxSum, this.calculateMaxSum(row + 1, column, downDepth));
            }

            if (column + 1 < this.columns) {
                maxSum = Integer.max(maxSum, this.calculateMaxSum(row, column + 1, depth - 1 - downDepth));
            }
        }

        if (maxSum != -1) {
            maxSum = maxSum + grid[row][column];
            // System.out.println(String.format("(%d,%d), depth: %d, value: %d", row, column, depth, maxSum));
        } else {
            // System.out.println(String.format("(%d,%d), depth: %d, DEAD END", row, column, depth));
        }

        return maxSum;
    }
}
