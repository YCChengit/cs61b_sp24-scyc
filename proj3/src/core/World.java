package core;

import edu.princeton.cs.algs4.StdDraw;
import org.junit.jupiter.api.MethodOrderer;
import org.reflections.vfs.Vfs;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;

import java.awt.*;
import java.util.Random;

public class World {

    // build your own world!

    //world size
    private static int WIDTH = 60;
    private static int HEIGHT = 30;
    private static final String[] SAVE_FILE = {"src/save_1.txt", "src/save_2.txt", "src/save_3.txt"};
    private static int MAX_ROOM_SIZE = 8;

    //display size
    private static int DISPLAY_WIDTH = WIDTH + 1;
    private static int DISPLAY_HEIGHT = HEIGHT + 1;

    private TETile[][] world;
    private TETile[][] dark_world;
    private TERenderer ter;

    private Generator generator;

    private Avatar avatar;
    private Point mouse;

    private boolean ready_to_save;
    private boolean new_game;
    private boolean dark_mode;

    private void set_seed(long seed) {
        random = new Random(seed);
        bagRandom = new BagRandomizer(random, Math.max(WIDTH, HEIGHT));
        generator = new Generator(bagRandom, WIDTH, HEIGHT, MAX_ROOM_SIZE);
        avatar = new Avatar(bagRandom, WIDTH, HEIGHT);
    }

    private void world_create(){
        // generate world
        if (new_game){
            generator.generate();
            world = generator.getWorld();
            avatar.set_world(world);
            avatar.rand_pos(world);
        } else {
            load();
        }

        while (true) {
            //upadte world
            update();
            // render world
            if (dark_mode){
                ter.renderFrame(dark_world);
            } else {
                ter.renderFrame(world);
            }
            // render HUD
            render_HUD();
            StdDraw.pause(100);
        }
    }

    public void update() {
        //upadte world
        if (StdDraw.hasNextKeyTyped() && !ready_to_save) {
            char key = StdDraw.nextKeyTyped();
            if (key == 'W' || key == 'w') {
                avatar.move(0, 1);
            } else if (key == 'A' || key == 'a') {
                avatar.move(-1, 0);
            } else if (key == 'S' || key == 's') {
                avatar.move(0, -1);
            } else if (key == 'D' || key == 'd') {
                avatar.move(1, 0);
            } else if (key == ':') {
                ready_to_save = true;
            } else if (key == 'L' || key == 'l') {
                dark_mode = !dark_mode;
            }
        }
        else if (StdDraw.hasNextKeyTyped() && ready_to_save){
            char nextKey = StdDraw.nextKeyTyped();
            if (nextKey == 'Q' || nextKey == 'q') {
                save();
                System.exit(0);
            } else {
                ready_to_save = false;
            }
        }

        avatar.avatar_to_world();
        dark_update();
    }

    private void dark_update() {
        dark_world = new TETile[WIDTH][HEIGHT];
        int x = avatar.getX();
        int y = avatar.getY();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (Math.abs(i - x) <= 3 && Math.abs(j - y) <= 3) {
                    dark_world[i][j] = world[i][j];
                } else {
                    dark_world[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    public TETile get_mouse_tile(){
        int x = 0;
        int y = 0;
        // get mouse position
        x = (int) StdDraw.mouseX();
        y = (int) StdDraw.mouseY();
        if (x != mouse.x || y != mouse.y){
            mouse = new Point(x, y);
        }

        if (x >= WIDTH){
            return null;
        } else if (y >= HEIGHT){
            return null;
        }
        return world[x][y];
    }

    public void render_HUD(){
        // draw HUD

        if (get_mouse_tile() == null){
            return;
        } else {
            TETile tile = get_mouse_tile();
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textLeft(1, HEIGHT, tile.description().toUpperCase());
        }
        StdDraw.show();
    }

    public void save(){
        // save world
        String pos = avatar.getX() + " " + avatar.getY() + "\n";
        String world_str = "";
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[i][j] == Tileset.WALL) {
                    world_str += "W";
                } else if (world[i][j] == Tileset.FLOOR) {
                    world_str += "F";
                } else if (world[i][j] == Tileset.AVATAR) {
                    world_str += "A";
                } else if (world[i][j] == Tileset.NOTHING) {
                    world_str += "N";
                }
            }
            world_str += "\n";
        }
        String save_str = pos  + world_str;
        while (true) {
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2, "Save Game: slot 1");
            StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2 - 2, "Save Game: slot 2");
            StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2 - 4, "Save Game: slot 3");
            StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2 - 2, "Quit: Q");
            StdDraw.show();
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == '1') {
                    FileUtils.writeFile(SAVE_FILE[0], save_str);
                    break;
                } else if (key == '2') {
                    FileUtils.writeFile(SAVE_FILE[1], save_str);
                    break;
                } else if (key == '3') {
                    FileUtils.writeFile(SAVE_FILE[2], save_str);
                    break;
                } else if (key == 'Q' || key == 'q') {
                    break;
                }
            }
        }
    }

    public void load() {
        // load world
        String save_str = "";
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(DISPLAY_WIDTH , DISPLAY_HEIGHT , DISPLAY_WIDTH , DISPLAY_HEIGHT );
        while (true) {
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2, "Load Game: slot 1");
            StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2 - 2, "Load Game: slot 2");
            StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2 - 4, "Load Game: slot 3");
            StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2 - 6, "Quit: Q");
            StdDraw.show();
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == '1') {
                    save_str = FileUtils.readFile(SAVE_FILE[0]);
                    break;
                } else if (key == '2') {
                    save_str = FileUtils.readFile(SAVE_FILE[1]);
                    break;
                } else if (key == '3') {
                    save_str = FileUtils.readFile(SAVE_FILE[2]);
                    break;
                } else if (key == 'Q' || key == 'q') {
                    System.exit(0);
                }
            }
        }
        String[] lines = save_str.split("\n");
        String[] pos = lines[0].split(" ");
        int x = Integer.parseInt(pos[0]);
        int y = Integer.parseInt(pos[1]);
        avatar.set_pos(x, y);
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c == 'W') {
                    world[i - 1][j] = Tileset.WALL;
                } else if (c == 'F') {
                    world[i - 1][j] = Tileset.FLOOR;
                } else if (c == 'A') {
                    world[i - 1][j] = Tileset.AVATAR;
                } else if (c == 'N') {
                    world[i - 1][j] = Tileset.NOTHING;
                }
            }
        }
        avatar.set_world(world);
    }

    public void start_menu() {

        ter.initialize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2, "CS61B: THE GAME");
        StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2 - 2, "New Game: N");
        StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2 - 4, "Load Game: L");
        StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2 - 6, "Quit: Q");
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'N' || key == 'n') {
                    new_game = true;
                    //seed selection
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledRectangle(DISPLAY_WIDTH , DISPLAY_HEIGHT , DISPLAY_WIDTH , DISPLAY_HEIGHT );
                    String seed = "";
                    while (true) {
                        StdDraw.setPenColor(StdDraw.WHITE);
                        StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2, "Enter Seed: ");
                        StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2 - 4, "Press S to Start");
                        StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2 - 6, "Press Q to Quit");
                        StdDraw.setPenColor(StdDraw.BLACK);
                        StdDraw.filledRectangle(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2 - 2, DISPLAY_WIDTH / 2, 1);
                        StdDraw.setPenColor(StdDraw.WHITE);
                        StdDraw.text(DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2 - 2, seed);
                        StdDraw.show();
                        if (StdDraw.hasNextKeyTyped()) {
                            char nextKey = StdDraw.nextKeyTyped();
                            if (nextKey == 'S' || nextKey == 's') {
                                long seed_num = Long.parseLong(seed);
                                set_seed(seed_num);
                                break;
                            } else if (nextKey == 'Q' || nextKey == 'q') {
                                System.exit(0);
                            } else if (Character.isDigit(nextKey)) {
                                seed += nextKey;
                            } else {
                                seed = "";
                            }
                        }
                    }
                    break;
                } else if (key == 'L' || key == 'l') {
                    new_game = false;
                    break;
                } else if (key == 'Q' || key == 'q') {
                    System.exit(0);
                }
            }
        }
    }

    public static void main(String[] args) {
        // build world!
        long seed = args.length > 0 ? Long.parseLong(args[0]) : 0;
        World world = new World(seed);
        world.start_menu();
        world.world_create();
    }

    private Random random;
    public BagRandomizer bagRandom;

    public World(long seed){
        world = new TETile[WIDTH][HEIGHT];
        random = new Random(seed);
        bagRandom = new BagRandomizer(random, Math.max(WIDTH, HEIGHT));
        generator = new Generator(bagRandom, WIDTH, HEIGHT, MAX_ROOM_SIZE);
        mouse = new Point(0, 0);
        avatar = new Avatar(bagRandom, WIDTH, HEIGHT);
        ter = new TERenderer();
    }
}
