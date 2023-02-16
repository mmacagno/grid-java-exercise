// Factory for grid objects

package grid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.InvalidFormatException;
import exceptions.InvalidValueException;
import interfaces.EntryValidator;

public class GridFactory {
    private static GridFactory instance;

    public static GridFactory getInstance() {
        synchronized (GridFactory.class) {
            if (instance == null) {
                instance = new GridFactory();
            }
        }

        return instance;
    }

    // Singleton: cannot instantiate directly
    private GridFactory() {}

    public Grid Deserialize(String serializedGrid, int expectedRows, int expectedColumns, EntryValidator validator) throws InvalidFormatException, InvalidValueException {
        if (serializedGrid == null) {
            throw new IllegalArgumentException("Invalid input string");
        }

        Grid grid = new Grid(expectedRows, expectedColumns, validator);

        String[] rows = serializedGrid.split("\n");
        if (rows.length != expectedRows) {
            throw new InvalidFormatException(String.format("Found %d rows, expected %d", rows.length, expectedRows));
        }

        for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
            // Check pattern of type ^grid\.
            Pattern startPattern = Pattern.compile("^grid\\.");
            Matcher matcher = startPattern.matcher(rows[rowIndex]);

            if (!matcher.find()) {
                throw new InvalidFormatException(String.format("Invalid row #%d: %s", rowIndex, rows[rowIndex]));
            } 
            
            // Determine if row sequence is correct
            // For this next pattern matching, we use a simple split
            // N=X,Y,Z,...
            // We split by = and , assuming to find the row sequence and the values
            String[] elements = rows[rowIndex].substring(matcher.end()).split("[=,]");

            // Expected: 1 number for the row id + expectedColumn values
            if (elements.length != expectedColumns + 1) {
                throw new InvalidFormatException(String.format("Row #%d does not contain the expected number of elements: %s", rowIndex, rows[rowIndex]));
            }
            
            // Check row identity
            try {
                Integer rowId = Integer.parseInt(elements[0]);
                if (rowId != rowIndex + 1) {
                    throw new InvalidFormatException(String.format("Row #%d is not at the right sequence: %s", rowIndex, rows[rowIndex]));
                }
            } catch (NumberFormatException exception) {
                throw new InvalidFormatException(String.format("Unable to parse row identifier on row %s", rows[rowIndex]));
            }

            // Extract individual numbers
            for (int colIndex = 1; colIndex < elements.length; colIndex++) {
                try {
                    Integer value = Integer.parseInt(elements[colIndex]);
                    grid.set(rowIndex, colIndex - 1, value);
                } catch (NumberFormatException exception) {
                    throw new InvalidFormatException(String.format("Unable to parse element %d on row %s", colIndex - 1, rows[rowIndex]));
                } catch (Exception exception) {
                    throw exception;
                }


            }
        }

        return grid;
    }
}
