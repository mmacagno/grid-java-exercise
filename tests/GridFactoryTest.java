
package tests;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import grid.GridFactory;
import exceptions.*;

public class GridFactoryTest {
    @Test
    public void testDeserializeProperInput1() throws InvalidFormatException, InvalidValueException {
        String serializedGrid = "grid.1=1,2,3\ngrid.2=3,4,5\ngrid.3=7,8,9";
        assertTrue("grid was not returned", GridFactory.getInstance().Deserialize(serializedGrid, 3, 3, (value) -> (value >= 1 && value <=9)) != null);
    }

    @Test
    public void testDeserializeProperInput2() throws InvalidFormatException, InvalidValueException {
        String serializedGrid = "grid.1=15,20\ngrid.2=30,40\ngrid.3=70,80";
        assertTrue("grid was not returned", GridFactory.getInstance().Deserialize(serializedGrid, 3, 2, (value) -> (value >= 1 && value <=100)) != null);
    }

    @Test
    public void testDeserializeInvalidFormat1() {
        String serializedGrid = "gridx2.1=15,20\ngrid.2=30,40\ngrid.3=70,80";

        assertThrows(
            InvalidFormatException.class, 
            () -> GridFactory.getInstance().Deserialize(serializedGrid, 3, 2, (value) -> (value >= 1 && value <=100)));
    }

    @Test
    public void testDeserializeInvalidFormat2() {
        String serializedGrid = "grid.1=15,20\ngrid.2=30,40,50\ngrid.3=70,80";

        assertThrows(
            InvalidFormatException.class, 
            () -> GridFactory.getInstance().Deserialize(serializedGrid, 3, 2, (value) -> (value >= 1 && value <=100)));
    }

    @Test
    public void testDeserializeInvalidValue() {
        String serializedGrid = "grid.1=15,20\ngrid.2=30,40\ngrid.3=70,80";

        assertThrows(
            InvalidValueException.class, 
            () -> GridFactory.getInstance().Deserialize(serializedGrid, 3, 2, (value) -> (value >= 1 && value <=10)));
    }
}
