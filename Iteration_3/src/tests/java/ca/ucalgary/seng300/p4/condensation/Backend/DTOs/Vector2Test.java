package ca.ucalgary.seng300.p4.condensation.Backend.DTOs;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class Vector2Test {

    @Test
    public void testConstructor() {
        Vector2 vector = new Vector2(3, 4);
        assertEquals(3, vector.x());
        assertEquals(4, vector.y());
    }

    @Test
    public void testAdd() {
        Vector2 v1 = new Vector2(1, 2);
        Vector2 v2 = new Vector2(3, 4);
        Vector2 result = v1.add(v2);
        assertEquals(4, result.x());
        assertEquals(6, result.y());
    }

    @Test
    public void testAddWithZero() {
        Vector2 v1 = new Vector2(5, -3);
        Vector2 result = v1.add(Vector2.ZERO);
        assertEquals(5, result.x());
        assertEquals(-3, result.y());
    }

    @Test
    public void testAddNegativeValues() {
        Vector2 v1 = new Vector2(-1, -2);
        Vector2 v2 = new Vector2(-3, -4);
        Vector2 result = v1.add(v2);
        assertEquals(-4, result.x());
        assertEquals(-6, result.y());
    }

    @Test
    public void testSub() {
        Vector2 v1 = new Vector2(5, 7);
        Vector2 v2 = new Vector2(2, 3);
        Vector2 result = v1.sub(v2);
        assertEquals(3, result.x());
        assertEquals(4, result.y());
    }

    @Test
    public void testSubWithZero() {
        Vector2 v1 = new Vector2(5, -3);
        Vector2 result = v1.sub(Vector2.ZERO);
        assertEquals(5, result.x());
        assertEquals(-3, result.y());
    }

    @Test
    public void testSubNegativeValues() {
        Vector2 v1 = new Vector2(-1, -2);
        Vector2 v2 = new Vector2(-3, -4);
        Vector2 result = v1.sub(v2);
        assertEquals(2, result.x());
        assertEquals(2, result.y());
    }

    @Test
    public void testFromArray() {
        int[] array = {5, 6};
        Vector2 vector = Vector2.fromArray(array);
        assertEquals(5, vector.x());
        assertEquals(6, vector.y());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFromArrayWithShortArray() {
        int[] array = {1};
        Vector2.fromArray(array);
    }

    @Test(expected = NullPointerException.class)
    public void testFromArrayWithNull() {
        Vector2.fromArray(null);
    }

    @Test
    public void testToArray() {
        Vector2 vector = new Vector2(7, 8);
        int[] array = vector.toArray();
        assertNotNull(array);
        assertEquals(2, array.length);
        assertEquals(7, array[0]);
        assertEquals(8, array[1]);
    }

    @Test
    public void testZeroConstant() {
        Vector2 zero = Vector2.ZERO;
        assertEquals(0, zero.x());
        assertEquals(0, zero.y());
    }

    @Test
    public void testEquality() {
        Vector2 v1 = new Vector2(1, 2);
        Vector2 v2 = new Vector2(1, 2);
        Vector2 v3 = new Vector2(2, 1);
        assertEquals(v1, v2);
        assertThat(v1, not(equals(v3)));
    }

    @Test
    public void testHashCode() {
        Vector2 v1 = new Vector2(1, 2);
        Vector2 v2 = new Vector2(1, 2);
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testToString() {
        Vector2 vector = new Vector2(3, 4);
        String expected = "Vector2[x=3, y=4]";
        assertEquals(expected, vector.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAddWithNull() {
        Vector2 v1 = new Vector2(1, 2);
        v1.add(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSubWithNull() {
        Vector2 v1 = new Vector2(1, 2);
        v1.sub(null);
    }
}