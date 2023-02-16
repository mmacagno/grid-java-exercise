
package tests;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import grid.GridFactory;
import grid.Grid;
import exceptions.*;

public class GridTests {
    @Test
    public void testBasics() throws InvalidValueException {
        Grid grid = new Grid(5, 5, null);

        grid.set(0, 0, 10);
        assertTrue("set/get not working", grid.get(0, 0) == 10);

        assertThrows(
            IllegalArgumentException.class,
            () -> grid.set(20, 20, 10));

        assertThrows(
            IllegalArgumentException.class,
            () -> grid.get(-1, -1));
    }

    @Test
    public void testInvalidInitializers() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Grid(20, 20, null));

        assertThrows(
            IllegalArgumentException.class,
            () -> new Grid(-1, 5, null));
    }

    @Test
    public void testValidators() throws InvalidValueException {
        Grid grid = new Grid(5, 5, (value) -> value > 100);

        assertThrows(
            InvalidValueException.class,
            () -> grid.set(0, 0, 10));

        grid.set(0, 0, 200);
        assertTrue("set/get not working", grid.get(0, 0) == 200);
    }

    @Test
    public void testMaxValueCalculations() throws InvalidFormatException, InvalidValueException {
        String serializedGrid;
        Grid grid;
        int maxValue;

        serializedGrid = "grid.1=9,2,1\ngrid.2=1,2,2\ngrid.3=1,6,9";
        grid = GridFactory.getInstance().Deserialize(serializedGrid, 3, 3, null);
        maxValue = grid.calculateLargestSum(3);
        assertTrue(String.format("Calculation algorithm does not work, expected: %d, actual %d", 17, maxValue), maxValue  == 17);

        serializedGrid = "grid.1=1,1,1\ngrid.2=1,9,1\ngrid.3=1,1,1";
        grid = GridFactory.getInstance().Deserialize(serializedGrid, 3, 3, null);
        maxValue = grid.calculateLargestSum(3);
        assertTrue(String.format("Calculation algorithm does not work, expected: %d, actual %d", 11, maxValue), maxValue  == 11);


        serializedGrid = "grid.1=1,1,1\ngrid.2=1,9,1\ngrid.3=1,1,1";
        grid = GridFactory.getInstance().Deserialize(serializedGrid, 3, 3, null);
        maxValue = grid.calculateLargestSum(4);
        assertTrue(String.format("Calculation algorithm does not work, expected: %d, actual %d", 12, maxValue), maxValue  == 12);

        serializedGrid = "grid.1=1,1,1,1\ngrid.2=1,9,1,1\ngrid.3=1,1,1,1";
        grid = GridFactory.getInstance().Deserialize(serializedGrid, 3, 4, null);
        maxValue = grid.calculateLargestSum(3);
        assertTrue(String.format("Calculation algorithm does not work, expected: %d, actual %d", 11, maxValue), maxValue  == 11);
    }
}
