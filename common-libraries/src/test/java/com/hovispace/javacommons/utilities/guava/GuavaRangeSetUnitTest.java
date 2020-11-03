package com.hovispace.javacommons.utilities.guava;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * A RangeSet is a set comprising of zero or more non-empty, disconnected ranges. When adding a range to a mutable RangeSet, any connected ranges are merged together while empty ranges are ignored.
 *
 * The Range in examples consists of integers. We can use a range consisting of any type as long as it implements the Comparable interface such as String, Character, floating point decimals etc In the case of an ImmutableRangeSet,
 * a range item present in the set cannot overlap with a range item that one would like to add. If that happens, we get an IllegalArgumentException
 *
 * Range input to a RangeSet cannot be null. If the input is null, we will get a NullPointerException
 */
public class GuavaRangeSetUnitTest {

    @Test
    public void test_adding_range() throws Exception {
        RangeSet<Integer> numberRangeSet = TreeRangeSet.create();

        numberRangeSet.add(Range.closed(0, 2));
        numberRangeSet.add(Range.closed(3, 5));
        numberRangeSet.add(Range.closed(6, 8));

        assertTrue(numberRangeSet.contains(1));
        assertFalse(numberRangeSet.contains(9));
    }

    @Test
    public void test_removing_range() throws Exception {
        RangeSet<Integer> numberRangeSet = TreeRangeSet.create();

        numberRangeSet.add(Range.closed(0, 2));
        numberRangeSet.add(Range.closed(3, 5));
        numberRangeSet.add(Range.closed(6, 8));
        numberRangeSet.add(Range.closed(9, 15));
        numberRangeSet.remove(Range.closed(3, 5));
        numberRangeSet.remove(Range.closed(7, 10));

        assertTrue(numberRangeSet.contains(1));
        assertFalse(numberRangeSet.contains(9));
        assertTrue(numberRangeSet.contains(12));
    }

    @Test
    public void test_range_span() throws Exception {
        RangeSet<Integer> numberRangeSet = TreeRangeSet.create();

        numberRangeSet.add(Range.closed(0, 2));
        numberRangeSet.add(Range.closed(3, 5));
        numberRangeSet.add(Range.closed(6, 8));
        Range<Integer> experienceSpan = numberRangeSet.span();

        assertThat(experienceSpan.lowerEndpoint().intValue()).isZero();
        assertThat(experienceSpan.upperEndpoint().intValue()).isEqualTo(8);
    }

    // subrange - get part of RangeSet based on a given Range
    @Test
    public void test_getting_a_subrange() throws Exception {
        RangeSet<Integer> numberRangeSet = TreeRangeSet.create();

        numberRangeSet.add(Range.closed(0, 2));
        numberRangeSet.add(Range.closed(3, 5));
        numberRangeSet.add(Range.closed(6, 8));
        RangeSet<Integer> numberSubRangeSet = numberRangeSet.subRangeSet(Range.closed(4, 14));

        assertFalse(numberSubRangeSet.contains(3));
        assertFalse(numberSubRangeSet.contains(14));
        assertTrue(numberSubRangeSet.contains(7));
    }

    // complement - get all the values except the one present in RangeSet
    @Test
    public void test_complement() throws Exception {
        RangeSet<Integer> numberRangeSet = TreeRangeSet.create();

        numberRangeSet.add(Range.closed(0, 2));
        numberRangeSet.add(Range.closed(3, 5));
        numberRangeSet.add(Range.closed(6, 8));
        RangeSet<Integer> numberRangeComplementSet = numberRangeSet.complement();

        assertTrue(numberRangeComplementSet.contains(-1000));
        assertFalse(numberRangeComplementSet.contains(2));
        assertFalse(numberRangeComplementSet.contains(3));
        assertTrue(numberRangeComplementSet.contains(1000));
    }

    // intersect - check if a range interval present in RangeSet intersects with some or all the values in another given range
    @Test
    public void test_intersection() throws Exception {
        RangeSet<Integer> numberRangeSet = TreeRangeSet.create();

        numberRangeSet.add(Range.closed(0, 2));
        numberRangeSet.add(Range.closed(3, 10));
        numberRangeSet.add(Range.closed(15, 18));

        assertTrue(numberRangeSet.intersects(Range.closed(4, 17)));
        assertFalse(numberRangeSet.intersects(Range.closed(19, 200)));
    }
}

