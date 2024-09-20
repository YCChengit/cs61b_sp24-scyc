import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDeque61BPreconditionTest {

    @Test
    @DisplayName("ArrayDeque61B has no fields besides backing array and primitives")
    void noNonTrivialFields() {
        List<Field> badFields = Reflection.getFields(ArrayDeque61B.class)
                .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                .toList();

        assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
    }

    @Test
    public void TestConstructor() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        assertWithMessage("ArrayDeque61B constructor failed").that(ad.size()).isEqualTo(0);
    }

    @Test
    public void TestAddFirst() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addFirst(1);
        ad.addFirst(2);
        ad.addFirst(3);
        assertWithMessage("ArrayDeque61B addFirst failed").that(ad.size()).isEqualTo(3);
        assertWithMessage("ArrayDeque61B addFirst failed").that(ad.toList()).isEqualTo(List.of(3, 2, 1));
    }

    @Test
    public void TestAddLast() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        ad.addLast(2);
        ad.addLast(3);
        assertWithMessage("ArrayDeque61B addLast failed").that(ad.size()).isEqualTo(3);
        assertWithMessage("ArrayDeque61B addLast failed").that(ad.toList()).isEqualTo(List.of(1, 2, 3));
    }

    @Test
    public void TestRemoveFirst() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addFirst(1);
        ad.addFirst(2);
        ad.addFirst(3);
        assertWithMessage("ArrayDeque61B removeFirst failed").that(ad.removeFirst()).isEqualTo(3);
        assertWithMessage("ArrayDeque61B removeFirst failed").that(ad.size()).isEqualTo(2);
        assertWithMessage("ArrayDeque61B removeFirst failed").that(ad.toList()).isEqualTo(List.of(2, 1));
    }

    @Test
    public void TestRemoveLast() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        ad.addLast(2);
        ad.addLast(3);
        assertWithMessage("ArrayDeque61B removeLast failed").that(ad.removeLast()).isEqualTo(3);
        assertWithMessage("ArrayDeque61B removeLast failed").that(ad.size()).isEqualTo(2);
        assertWithMessage("ArrayDeque61B removeLast failed").that(ad.toList()).isEqualTo(List.of(1, 2));
    }

    @Test
    public void TestGet() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        ad.addLast(2);
        ad.addLast(3);
        assertWithMessage("ArrayDeque61B get failed").that(ad.get(0)).isEqualTo(1);
        assertWithMessage("ArrayDeque61B get failed").that(ad.get(1)).isEqualTo(2);
        assertWithMessage("ArrayDeque61B get failed").that(ad.get(2)).isEqualTo(3);
    }

    @Test
    public void TestResize() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        for (int i = 0; i < 8; i++) {
            ad.addLast(i);
        }
        assertWithMessage("ArrayDeque61B resize failed").that(ad.size()).isEqualTo(8);
        assertWithMessage("ArrayDeque61B resize failed").that(ad.toList()).isEqualTo(List.of(0, 1, 2, 3, 4, 5, 6, 7));
        ad.addLast(8);
        assertWithMessage("ArrayDeque61B resize failed").that(ad.size()).isEqualTo(9);
        assertWithMessage("ArrayDeque61B resize failed").that(ad.toList()).isEqualTo(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8));
    }
}
