import deque.Deque61B;
import org.junit.jupiter.api.Test;
import deque.ArrayDeque61B;
import static com.google.common.truth.Truth.assertWithMessage;


public class MethodTest {

    @Test
    public void iterableTest() {
        Deque61B<String> deque = new ArrayDeque61B<>();
        deque.addFirst("a");
        deque.addFirst("b");
        deque.addFirst("c");
        deque.addFirst("d");
        deque.addFirst("e");
        for (String s : deque) {
            System.out.println(s);
        }
    }

    @Test
    public void equalsTest() {
        Deque61B<String> deque1 = new ArrayDeque61B<>();
        deque1.addFirst("a");
        deque1.addFirst("b");
        deque1.addFirst("c");
        deque1.addFirst("d");
        deque1.addFirst("e");
        Deque61B<String> deque2 = new ArrayDeque61B<>();
        deque2.addFirst("a");
        deque2.addFirst("b");
        deque2.addFirst("c");
        deque2.addFirst("d");
        deque2.addFirst("e");
        assertWithMessage("equals() should return true for two equal deques")
                .that(deque1.equals(deque2))
                .isTrue();

        Deque61B<String> deque3 = new ArrayDeque61B<>();
        deque3.addFirst("a");
        assertWithMessage("equals() should return false for two unequal deques")
                .that(deque1.equals(deque3))
                .isFalse();
    }

    @Test
    public void toStringTest() {
        Deque61B<String> deque = new ArrayDeque61B<>();
        deque.addFirst("a");
        deque.addFirst("b");
        deque.addFirst("c");
        deque.addFirst("d");
        deque.addFirst("e");
        System.out.println(deque.toString());
    }

}
