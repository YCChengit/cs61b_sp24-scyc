import core.BagRandomizer;
import core.Generator;
import org.junit.Test;
import tileengine.TERenderer;
import tileengine.TETile;

import java.util.Random;

public class WorldCreateTests {

    @Test
    public void worldCreateTest1() {
        int seed = 12;
        Random random = new Random(seed);
        int WIDTH = 60;
        int HEIGHT = 30;
        int MAX_ROOM_SIZE = 8;
        BagRandomizer bagRandom = new BagRandomizer(random, Math.max(WIDTH, HEIGHT));
        Generator gen = new Generator(bagRandom, WIDTH, HEIGHT, MAX_ROOM_SIZE);
        gen.generate();
        TETile[][] world = gen.getWorld();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        while (true) {
            // render world
            ter.renderFrame(world);
        }
    }
}
