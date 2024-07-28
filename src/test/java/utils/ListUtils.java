package utils;

import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListUtils
{

    public static <E> void assertListsEqual(List<E> l1, List<E> l2)
    {
        l1.sort(Comparator.comparing(Object::toString));
        l2.sort(Comparator.comparing(Object::toString));

        assertThat(l1, is(l2));
    }

}
