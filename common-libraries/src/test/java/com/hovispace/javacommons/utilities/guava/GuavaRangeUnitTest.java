package com.hovispace.javacommons.utilities.guava;

import com.google.common.collect.Range;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Guava’s Range represents an interval, for example, a < range < b. Here range includes any value between a and b, called endpoints which form the boundary.
 * Any value between the boundary is a contiguous span of values of type Comparable.
 *
 * Each end of the range may be bounded or unbounded. If bounded, there is an associated endpoint value. If not it will be treated as infinity.
 * A range can be further defined as either open or closed based whether the range is exclusive or inclusive of the endpoints.
 *
 * If we use a square bracket [] to indicate that the range is closed on that side and
 * a parenthesis () to indicate that the range is either open or unbounded then a < range < b can be represented by (a..b) and a < range <= b by (a..b]
 */
public class GuavaRangeUnitTest {

    // open(a, b) – represents a < range < b, and in notation form, (a,b).
    @Test
    public void test_open_range() throws Exception {
        Range<Integer> range = Range.open(1, 4);
        assertFalse(range.contains(1));
        assertTrue(range.contains(2));
        assertTrue(range.contains(3));
        assertFalse(range.contains(4));
    }

    // closed(a, b) – represents a <= range <= b, and in notation form, [a,b].
    @Test
    public void test_closed_range() throws Exception {
        Range<Integer> range = Range.closed(1, 4);
        assertFalse(range.contains(0));
        assertTrue(range.contains(1));
        assertTrue(range.contains(2));
        assertTrue(range.contains(3));
        assertTrue(range.contains(4));
        assertFalse(range.contains(5));
    }


    // openClosed(a, b) – represents a < range <= b, and in notation form, (a,b].
    @Test
    public void test_open_closed_range() throws Exception {
        Range<Integer> range = Range.openClosed(1, 4);
        assertFalse(range.contains(0));
        assertFalse(range.contains(1));
        assertTrue(range.contains(2));
        assertTrue(range.contains(3));
        assertTrue(range.contains(4));
        assertFalse(range.contains(5));
    }

    // closedOpen(a, b) – represents a <= range < b, and in notation form, [a,b).
    @Test
    public void test_closed_open_range() throws Exception {
        Range<Integer> range = Range.closedOpen(1, 4);
        assertFalse(range.contains(0));
        assertTrue(range.contains(1));
        assertTrue(range.contains(2));
        assertTrue(range.contains(3));
        assertFalse(range.contains(4));
        assertFalse(range.contains(5));
    }

    // greaterThan(a) – represents range > a, and in notation form, (a..+∞).
    @Test
    public void test_greater_than_range() throws Exception {
        Range<Integer> range = Range.greaterThan(2);
        assertFalse(range.contains(2));
        assertTrue(range.contains(2000));
    }

    // atLeast(a, b) – represents range >= a, and in notation form, [a..+∞).
    @Test
    public void test_at_least_range() throws Exception {
        Range<Integer> range = Range.atLeast(2);
        assertTrue(range.contains(2));
        assertTrue(range.contains(2000));
    }

    // lessThan(a, b) – represents range < b, and in notation form, (-∞..b).
    @Test
    public void test_less_than_range() throws Exception {
        Range<Integer> range = Range.lessThan(2);
        assertFalse(range.contains(2));
        assertTrue(range.contains(1));
        assertTrue(range.contains(-1));
    }

    // atMost(a, b) – represents range <= b, and in notation form, (-∞..b].
    @Test
    public void test_at_most_range() throws Exception {
        Range<Integer> range = Range.atMost(2);
        assertTrue(range.contains(2));
        assertTrue(range.contains(1));
        assertTrue(range.contains(-1));
    }

    // all() – represents -∞ < range < +∞, and in notation form, (-∞..+∞).
    @Test
    public void test_all_range() throws Exception {
        Range<Integer> range = Range.all();
        assertTrue(range.contains(-200));
        assertTrue(range.contains(200));
    }
}
