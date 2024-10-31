package core;

import org.checkerframework.checker.units.qual.A;
import org.reflections.vfs.Vfs;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Generator {

    private BagRandomizer bagRandom;
    private int width;
    private int height;
    private int maxRoomSize;
    private double ROOMRATE;
    private boolean[][] aux_world;
    private ArrayList<Point> edge;
    private ArrayList<Point> room_center;
    private double occupiedRatio;
    private double MAXOCCUPIEDRATIO ;
//    private Branch root;


    public Generator(BagRandomizer bagRandom, int width, int height, int maxRoomSize) {
        this.bagRandom = bagRandom;
        this.width = width;
        this.height = height;
        this.aux_world = new boolean[width][height];
        this.maxRoomSize = maxRoomSize;
        this.ROOMRATE = 0.35;
        this.room_center = new ArrayList<>();
        this.occupiedRatio = 0;
        this.MAXOCCUPIEDRATIO = 0.5;
        this.edge = new ArrayList<>();
    }



    public void generate() {
        /**
         * test Branch

        // generate world
        this.root = new Branch(width / 2, height / 2, width, height, bagRandom);
        this.root.world = this.root.grow();
        while (this.root.occupiedRatio() < 0.03) {
            this.root = new Branch(this.root);
            this.root.world = this.root.grow();
        }
        */
        // generate rooms
        while (occupiedRatio < MAXOCCUPIEDRATIO) {
            aux_world = new boolean[width][height];
            room_center.clear();
            edge.clear();
            Point pos = room_pos();
            while (occupiedRatio <ROOMRATE) {
                aux_world = room_create(pos);
                pos = room_pos();
                occupiedRatio();
            }

            // generate hallways
            for (int i = 0; i < room_center.size() - 1; i++) {
                Point room_start = room_center.get(i);
                Point room_end = room_center.get(i + 1);
                hallway_create(room_start, room_end);
            }
            occupiedRatio();
        }
        Wall_create();

    }

    public void occupiedRatio() {
        int occupied = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (aux_world[i][j]) {
                    occupied++;
                }
            }
        }
        occupiedRatio = (double) occupied / ((width -4) * (height - 4));
    }

    public boolean[][] room_create(Point pos) {
        int width = 0;
        int height = 0;
        int times = 0;
        while (width < 2 || height < 2 || pos.x + width >= this.width || pos.y + height >= this.height) {
            width = bagRandom.getValue() % maxRoomSize + 1;
            height = bagRandom.getValue() % maxRoomSize + 1;
            if (times > 5) {
                return aux_world;
            }
            times++;
        }
        if (overlap(pos, width, height)) {
            return aux_world;
        } else {
            Room room = new Room(width, height, pos.x, pos.y);
            room_center.add(room.getCenter());
            return room.roomToworld(aux_world);
        }
    }

    public boolean overlap(Point pos, int width, int height) {
        for (int i = pos.x; i < pos.x + width; i++) {
            for (int j = pos.y; j < pos.y + height; j++) {
                if (aux_world[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    public void hallway_create(Point room_start, Point room_end) {
        int curX = room_start.x;
        int curY = room_start.y;
        int destX = room_end.x;
        int destY = room_end.y;

        while (curX != destX || curY != destY) {
            Direction d = random_direction(curX, curY, destX, destY);
            Room room = new Room(1, 1, curX, curY);
            aux_world[curX][curY] = true;
            curX += d.dx;
            curY += d.dy;
            }
        aux_world[destX][destY] = true;
    }

    private int count_neighbor(int x, int y) {
        int count = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (x + i < 0 || x + i >= width || y + j < 0 || y + j >= height) {
                    continue;
                }
                if (aux_world[x + i][y + j]) {
                    count++;
                }
            }
        }
        return count;
    }

    private Direction random_direction(int x, int y, int destx, int desty) {
        Direction[] directions = new Direction[2];
        if (destx - x > 0) {
            directions[0] = Direction.RIGHT;
        } else if (destx - x < 0) {
            directions[0] = Direction.LEFT;
        } else {
            directions[0] = Direction.ORIGIN;
        }
        if (desty - y > 0) {
            directions[1] = Direction.UP;
        } else if (desty - y < 0) {
            directions[1] = Direction.DOWN;
        } else {
            directions[1] = Direction.ORIGIN;
        }

        Direction direction = directions[bagRandom.getValue() % 2];
        int curX = x + direction.dx;
        int curY = y + direction.dy;
        while (curX < 0 || curX >= width || curY < 0 || curY >= height) {
            direction = directions[bagRandom.getValue() % 2];
            curX = x + direction.dx;
            curY = y + direction.dy;
        }
        return direction;
    }

    public Point room_pos() {
        int x = bagRandom.getValue() % width;
        int y = bagRandom.getValue() % height;
        while (aux_world[x][y] || x < 2 || y < 2 || x >= width - 2 || y >= height - 2) {
            x = bagRandom.getValue() % width;
            y = bagRandom.getValue() % height;
        }
        return new Point(x, y);
    }

    public void Wall_create() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (aux_world[i][j]) {
                    for (int x = -1; x < 2; x++) {
                        for (int y = -1; y < 2; y++) {
                            if (aux_world[i + x][j + y]) {
                                continue;
                            } else {
                                edge.add(new Point(i + x, j + y));
                            }
                        }
                    }
                }
            }
        }
    }

    public TETile[][] getWorld() {
        TETile[][] world = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (aux_world[i][j]) {
                    world[i][j] = Tileset.FLOOR;
                } else {
                    world[i][j] = Tileset.NOTHING;
                }
            }
        }

        for (Point p : edge) {
            world[p.x][p.y] = Tileset.WALL;
        }
        return world;
    }
}
