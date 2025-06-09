package utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListUtils {

    public static <E> void assertListsEqual(List<E> l1, List<E> l2) {
        l1.sort(Comparator.comparing(Object::toString));
        l2.sort(Comparator.comparing(Object::toString));

        assertThat(l1, is(l2));
    }

    public static <E> void assertListsEqual(List<E> l1, Set<E> l2) {
        assertListsEqual(l1, new ArrayList<>(l2));
    }

    public static <E> void assertListsEqual(Set<E> l1, List<E> l2) {
        assertListsEqual(new ArrayList<>(l1), l2);
    }

    public static <E, G> void assertMapsEqual(Map<E, G> m1, Map<E, G> m2) {
        assertListsEqual(m1.keySet().stream().toList(), m2.keySet().stream().toList());
        assertListsEqual(m1.values().stream().toList(), m2.values().stream().toList());
    }

    public static <E> void assertCollectionsEqual(Collection<E> l1, Collection<E> l2) {
        assertListsEqual(new ArrayList<>(l1), new ArrayList<>(l2));
    }
}
