import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDeque61BTest {

//     @Test
//     @DisplayName("ArrayDeque61B has no fields besides backing array and primitives")
//     void noNonTrivialFields() {
//         List<Field> badFields = Reflection.getFields(ArrayDeque61B.class)
//                 .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
//                 .toList();
//
//         assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
//     }

    @Test
    public void add_first_from_empty() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addFirst(1);
        assertThat(ad.size()).isEqualTo(1);
        assertThat(ad.toList()).isEqualTo(List.of(1));
    }

    @Test
    public void add_first_from_non_empty() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addFirst(1);
        ad.addFirst(2);
        ad.addFirst(3);
        assertThat(ad.size()).isEqualTo(3);
        assertThat(ad.toList()).isEqualTo(List.of(3, 2, 1));
    }

    @Test
    public void add_last_from_empty() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        assertThat(ad.size()).isEqualTo(1);
        assertThat(ad.toList()).isEqualTo(List.of(1));
    }

    @Test
    public void add_last_from_non_empty() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        ad.addLast(2);
        ad.addLast(3);
        assertThat(ad.size()).isEqualTo(3);
        assertThat(ad.toList()).isEqualTo(List.of(1, 2, 3));
    }

    @Test
    public void remove_first_trigger_resize() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        for (int i = 0; i < 9; i++) {
            ad.addFirst(i);
        }
        ad.removeFirst();
        assertThat(ad.size()).isEqualTo(8);
        assertThat(ad.toList()).isEqualTo(List.of(7, 6, 5, 4, 3, 2, 1, 0));
    }

    @Test
    public void remove_last_trigger_resize() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        for (int i = 0; i < 9; i++) {
            ad.addLast(i);
        }
        ad.removeLast();
        assertThat(ad.size()).isEqualTo(8);
        assertThat(ad.toList()).isEqualTo(List.of(0, 1, 2, 3, 4, 5, 6, 7));
    }

    @Test
    public void add_first_after_remove_to_empty() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addFirst(1);
        ad.removeFirst();
        ad.addFirst(2);
        assertThat(ad.size()).isEqualTo(1);
        assertThat(ad.toList()).isEqualTo(List.of(2));
    }

    @Test
    public void add_last_after_remove_to_empty() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        ad.removeLast();
        ad.addLast(2);
        assertThat(ad.size()).isEqualTo(1);
        assertThat(ad.toList()).isEqualTo(List.of(2));
    }

    @Test
    public void remove_first_to_empty() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addFirst(1);
        ad.removeFirst();
        assertThat(ad.size()).isEqualTo(0);
        assertThat(ad.toList()).isEqualTo(List.of());
    }

    @Test
    public void remove_last_to_empty() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        ad.removeLast();
        assertThat(ad.size()).isEqualTo(0);
        assertThat(ad.toList()).isEqualTo(List.of());
    }

    @Test
    public void remove_first() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addFirst(1);
        ad.addFirst(2);
        ad.addFirst(3);
        assertThat(ad.removeFirst()).isEqualTo(3);
        assertThat(ad.size()).isEqualTo(2);
        assertThat(ad.toList()).isEqualTo(List.of(2, 1));
    }

}
