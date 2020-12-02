package com.hovispace.javacommons.utilities.guava;

import org.junit.Test;

import static com.google.common.base.Preconditions.*;
import static java.util.Arrays.binarySearch;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GuavaPreconditionsUnitTest {

    /**
     * The method checkArgument accepts a boolean condition and ensures the truthfulness of the parameters passed to the calling method.
     *
     * @throws IllegalArgumentException when the condition is false
     */
    @Test
    public void test_that_checkArgument_throws_IllegalArgumentException_exception_if_argument_is_evaluated_false() throws Exception {
        int age = -18;
        String message = "Age can't be zero or less than zero";

        assertThatThrownBy(() -> checkArgument(age > 0, message))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(message)
            .hasNoCause();
    }

    /**
     * The method checkElementIndex checks that an index is a valid index in a list, string or an array of a specified size.
     *
     * @throws IndexOutOfBoundsException if the index is not a valid element index
     */
    @Test
    public void test_that_checkElementIndex_throws_IndexOutOfBoundsException_exception_if_element_index_is_evaluated_false() throws Exception {
        int[] numbers = { 1, 2, 3, 4, 5 };
        String message = "Please check the bound of an array and retry";
        assertThatThrownBy(() -> checkElementIndex(6, numbers.length - 1, message))
            .isInstanceOf(IndexOutOfBoundsException.class)
            .hasMessageStartingWith(message)
            .hasNoCause();
    }

    /**
     * The method checkNotNull checks whether a value supplied as a parameter is null. It returns the value that's been checked.
     *
     * @throws NullPointerException if the value that has been passed to this method is null
     */
    @Test
    public void test_that_checkNotNull_throws_NullPointerException_if_passed_parameters_to_method_is_null() throws Exception {
        String nullObject = null;
        String message = "Please check the Object supplied, its null!";

        assertThatThrownBy(() -> checkNotNull(nullObject, message))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(message)
            .hasNoCause();
    }

    /**
     * The method checkPositionIndex checks that an index passed as an argument to this method is a valid index in a list, string or array of a specified size
     *
     * @throws IndexOutOfBoundsException if the index passed is not between 0 and the size given, else it returns the index value.
     */
    @Test
    public void test_that_checkPositionIndex_throws_IndexOutOfBoundsException_if_index_passed_is_not_between_0_and_the_size_given() throws Exception {
        int[] numbers = { 1, 2, 3, 4, 5 };
        String message = "Please check the bound of an array and retry";

        assertThatThrownBy(() -> checkPositionIndex(6, numbers.length - 1, message))
            .isInstanceOf(IndexOutOfBoundsException.class)
            .hasMessageStartingWith(message)
            .hasNoCause();
    }

    /**
     * The method checkState checks the validity of the state of an object and is not dependent on the method arguments.
     *
     * @throws IllegalStateException if the state of an object (boolean value passed as an argument to the method) is in an invalid state.
     */
    @Test
    public void test_that_checkState_throws_IllegalStateException_if_given_states_is_invalid() throws Exception {
        int[] validStates = { -1, 0, 1 };
        int givenState = 10;
        String message = "You have entered an invalid state";

        assertThatThrownBy(() -> checkState(binarySearch(validStates, givenState) > 0, message))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageStartingWith(message)
            .hasNoCause();
    }
}