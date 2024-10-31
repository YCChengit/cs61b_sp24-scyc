package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;




public class Room {

    public BagRandomizer bagRandom;
    private int width;
    private int height;
    private Point pos;
    private Point center;

    public Room(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.pos = new Point(x, y);
        this.center = new Point(x + width / 2, y + height / 2);
    }

    public boolean[][] roomToworld(boolean[][] world) {
        for (int i = pos.x; i < pos.x + width; i++) {
            for (int j = pos.y; j < pos.y + height; j++) {
                if (pos.x + width >= world.length || pos.y + height >= world[0].length) {
                    continue;
                }
                world[i][j] = true;
            }
        }
        return world;
    }

    public Point getCenter() {
        return center;
    }

}
