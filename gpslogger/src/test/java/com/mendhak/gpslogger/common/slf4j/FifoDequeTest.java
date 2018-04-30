package com.mendhak.gpslogger.common.slf4j;

import com.mendhak.gpslogger.common.slf4j.FifoDeque;

import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static junit.framework.Assert.assertNotSame;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Ji Hoon on 2018-04-01.
 */

public class FifoDequeTest {
    private FifoDeque<Integer> deque;

    @Test
    public void testAdd() {
        deque = new FifoDeque(10);
        for(int i=0; i<10; i++)
            deque.add(i);
        assertThat("normal deque test", deque.size(),is(10));
    }

    //if max size is 10, size() must return below 10
    @Test
    public void testOverflowAdd() {
        deque = new FifoDeque(10);
        for(int i=0; i<15; i++)
            deque.add(i);
        assertThat("many element add test", deque.size(),is(10));
    }

    //if max size is 0, size() must return 0.
    @Test
    public void testAddToZeroSizeDeque() {
        deque = new FifoDeque(0);
        for(int i=0; i<3; i++)
            deque.add(i);
        assertThat("add to deque that max size is 0 test", deque.size(),is(0));
    }

    //it must handle to negatived max size
    @Test(expected = NoSuchElementException.class)
    public void testAddToNegativeMaxSizeDeque() {
        deque = new FifoDeque(-1);
        for(int i=0; i<3; i++)
            deque.add(i);
    }

    //if removeFirst, first element is must deleted
    @Test
    public void testRemoveFirst() {
        deque = new FifoDeque(5);
        for(int i=0; i<5; i++)
            deque.add(i);
        deque.removeFirst();
        assertThat("test if add to all filled deque, remove First",
                deque.getFirst(),is(1));
    }


}