package com.hovispace.javacommons.utilities.guava;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import org.junit.Test;

import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * A RangeMap is a special kind of mapping from disjoint non-empty ranges to non-null values.
 * Using queries, we may look up the value for any particular range in that map.
 *
 * RangeMap returns Null when we try to get the value for a range that is not present in map
 * In a case of an ImmutableRangeMap, a range of one key cannot overlap with a range of a key that needs to be inserted. If that happens, we get an IllegalArgumentException
 * Both keys and values in the RangeMap cannot be null. If either one of them is null, we get a NullPointerException
 */
public class GuavaRangeMapUnitTest {

    @Test
    public void test_retrieval_based_on_input() throws Exception {
        RangeMap<Integer, String> experienceRangeDesignationMap = TreeRangeMap.create();

        experienceRangeDesignationMap.put(Range.closed(0, 2), "Associate");
        experienceRangeDesignationMap.put(Range.closed(3, 5), "Senior Associate");
        experienceRangeDesignationMap.put(Range.closed(6, 8),  "Vice President");
        experienceRangeDesignationMap.put(Range.closed(9, 15), "Executive Director");

        assertThat(experienceRangeDesignationMap.get(6)).isEqualTo("Vice President");
        assertThat(experienceRangeDesignationMap.get(15)).isEqualTo("Executive Director");
    }

    @Test
    public void test_remove_a_value_based_on_range() throws Exception {
        RangeMap<Integer, String> experienceRangeDesignationMap = TreeRangeMap.create();

        experienceRangeDesignationMap.put(Range.closed(0, 2), "Associate");
        experienceRangeDesignationMap.put(Range.closed(3, 5), "Senior Associate");
        experienceRangeDesignationMap.put(Range.closed(6, 8), "Vice President");
        experienceRangeDesignationMap.put(Range.closed(9, 15), "Executive Director");

        experienceRangeDesignationMap.remove(Range.closed(9, 15));
        experienceRangeDesignationMap.remove(Range.closed(1, 4));

        assertThat(experienceRangeDesignationMap.get(9)).isNull();
        assertThat(experienceRangeDesignationMap.get(0)).isEqualTo("Associate");
        assertThat(experienceRangeDesignationMap.get(5)).isEqualTo("Senior Associate");
        assertThat(experienceRangeDesignationMap.get(1)).isNull();
    }

    @Test
    public void test_span_of_key_range() throws Exception {
        RangeMap<Integer, String> experienceRangeDesignationMap = TreeRangeMap.create();
        experienceRangeDesignationMap.put(Range.closed(0, 2), "Associate");
        experienceRangeDesignationMap.put(Range.closed(3, 5), "Senior Associate");
        experienceRangeDesignationMap.put(Range.closed(6, 8), "Vice President");
        experienceRangeDesignationMap.put(Range.closed(9, 15), "Executive Director");
        experienceRangeDesignationMap.put(Range.closed(16, 30), "Managing Director");
        Range<Integer> experienceSpan = experienceRangeDesignationMap.span();

        assertThat(experienceSpan.lowerEndpoint().intValue()).isZero();
        assertThat(experienceSpan.upperEndpoint().intValue()).isEqualTo(30);
    }

    @Test
    public void test_get_a_sub_rangeMap() throws Exception {
        RangeMap<Integer, String> experienceRangeDesignationMap = TreeRangeMap.create();

        experienceRangeDesignationMap.put(Range.closed(0, 2), "Associate");
        experienceRangeDesignationMap.put(Range.closed(3, 5), "Senior Associate");
        experienceRangeDesignationMap.put(Range.closed(6, 8), "Vice President");
        experienceRangeDesignationMap.put(Range.closed(8, 15), "Executive Director");
        experienceRangeDesignationMap.put(Range.closed(16, 30), "Managing Director");
        RangeMap<Integer, String> experiencedSubRangeDesignationMap = experienceRangeDesignationMap.subRangeMap(Range.closed(4, 14));

        assertThat(experiencedSubRangeDesignationMap.get(3)).isNull();
        assertTrue(experiencedSubRangeDesignationMap.asMapOfRanges().values().containsAll(asList("Executive Director", "Vice President", "Executive Director")));
    }

    @Test
    public void test_get_an_entry() throws Exception {
        RangeMap<Integer, String> experienceRangeDesignationMap = TreeRangeMap.create();

        experienceRangeDesignationMap.put(Range.closed(0, 2), "Associate");
        experienceRangeDesignationMap.put(Range.closed(3, 5), "Senior Associate");
        experienceRangeDesignationMap.put(Range.closed(6, 8), "Vice President");
        experienceRangeDesignationMap.put(Range.closed(9, 15), "Executive Director");
        Map.Entry<Range<Integer>, String> experienceEntry = experienceRangeDesignationMap.getEntry(10);

        assertThat(experienceEntry.getKey()).isEqualTo(Range.closed(9, 15));
        assertThat(experienceEntry.getValue()).isEqualTo("Executive Director");
    }
}
