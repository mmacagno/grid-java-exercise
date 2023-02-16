// Java programming exercise
// 2/15/2023

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import exceptions.*;
import grid.GridFactory;
import grid.Grid;

class GridProgram {
    private static final String DEFAULT_GRID_FILE = "./app.properties";

    public static void main (String[] args) {
        String filePath = DEFAULT_GRID_FILE;
        if (args.length > 0) {
            filePath = args[0];
        }

        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            String serializedGrid = new String(fileBytes, StandardCharsets.UTF_8);

            // Use the GridFactory to deserialize the file content into a grid object
            GridFactory instance = GridFactory.getInstance();

            // Use the following constraints:
            // Rows: 3
            // Columns: 3
            // Validator function for the individual values: positive int 1..9
            Grid grid = instance.Deserialize(serializedGrid, 3, 3, (value) -> (value >= 1 && value <=9));

            // Process and print out result
            System.out.println(String.format("Grid best result: %d", grid.calculateLargestSum(3)));
        }
        catch (InvalidFormatException exception) {
            System.out.println(exception.getMessage());
        }
        catch (InvalidValueException exception) {
            System.out.println(exception.getMessage());
        }
        catch (IOException exception) {
            System.err.println(String.format("Error reading File '%s'.", filePath));
        }
    }
}