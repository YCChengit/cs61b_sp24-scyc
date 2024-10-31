package core;

import tileengine.TETile;
import tileengine.Tileset;

public class Avatar {
    private int x;
    private int y;
    private TETile tile;
    private BagRandomizer bagRandomizer;
    private int width;
    private int height;
    private TETile[][] world;


    public Avatar(BagRandomizer bagRandomizer, int width, int height) {
        this.bagRandomizer = bagRandomizer;
        this.width = width;
        this.height = height;
    }

    public void set_world(TETile[][] world) {
        this.world = world;
    }


    public void rand_pos(TETile[][] world) {
        this.x = bagRandomizer.getValue() % width;
        this.y = bagRandomizer.getValue() % height;
        while (world[x][y] != Tileset.FLOOR) {
            this.x = bagRandomizer.getValue() % width;
            this.y = bagRandomizer.getValue() % height;
        }
        tile = Tileset.AVATAR;
    }

    public void move(int DeltaX, int DeltaY) {
        if (canMove(DeltaX, DeltaY)) {
            world[x][y] = Tileset.FLOOR;
            x += DeltaX;
            y += DeltaY;
        }
    }

    public boolean canMove(int deltaX, int deltaY) {
        return world[x + deltaX][y + deltaY] == Tileset.FLOOR;
    }

    public void avatar_to_world() {
        world[x][y] = tile;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TETile getTile() {
        return tile;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void set_pos(int x, int y) {
        this.x = x;
        this.y = y;
        this.tile = Tileset.AVATAR;
    }

    public void setTile(TETile tile) {
        this.tile = tile;
    }


}
