package tetris;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.TERenderer;
import tileengine.Tileset;

import java.util.*;

/**
 *  Provides the logic for Tetris.
 *
 *  @author Erik Nelson, Omar Yu, Noah Adhikari, Jasmine Lin
 */

public class Tetris {

    private static int WIDTH = 10;
    private static int HEIGHT = 20;

    // Tetrominoes spawn above the area we display, so we'll have our Tetris board have a
    // greater height than what is displayed.
    private static int GAME_HEIGHT = 25;

    // Contains the tiles for the board.
    private TETile[][] board;

    // Helps handle movement of pieces.
    private Movement movement;

    // Checks for if the game is over.
    private boolean isGameOver;

    // The current Tetromino that can be controlled by the player.
    private Tetromino currentTetromino;

    // The current game's score.
    private int score;

    /**
     * Checks for if the game is over based on the isGameOver parameter.
     * @return boolean representing whether the game is over or not
     */
    private boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Renders the game board and score to the screen.
     */
    private void renderBoard() {
        ter.drawTiles(board);
        renderScore();
        StdDraw.show();

        if (auxFilled) {
            auxToBoard();
        } else {
            fillBoard(Tileset.NOTHING);
        }
    }

    /**
     * Creates a new Tetromino and updates the instance variable
     * accordingly. Flags the game to end if the top of the board
     * is filled and the new piece cannot be spawned.
     */
    private void spawnPiece() {
        // The game ends if this tile is filled
        if (board[4][19] != Tileset.NOTHING) {
            isGameOver = true;
        }

        // Otherwise, spawn a new piece and set its position to the spawn point
        currentTetromino = Tetromino.values()[bagRandom.getValue()];
        currentTetromino.reset();
    }

    /**
     * Updates the board based on the user input. Makes the appropriate moves
     * depending on the user's input.
     */
    private void updateBoard() {
        // Grabs the current piece.
        Tetromino t = currentTetromino;
        if (actionDeltaTime() > 1000) {
            movement.dropDown();
            resetActionTimer();
            return;
        } else if (StdDraw.hasNextKeyTyped()) {
            char key = StdDraw.nextKeyTyped();
            switch (key) {
                case 'a':
                    movement.tryMove(-1, 0);
                    break;
                case 'd':
                    movement.tryMove(1, 0);
                    break;
                case 's':
                    movement.tryMove(0, -1);
                    break;
                case 'q':
                    movement.rotateLeft();
                    break;
                case 'e':
                    movement.rotateRight();
                    break;
                default:
                    break;
            }
        }

        Tetromino.draw(t, board, t.pos.x, t.pos.y);
    }

    /**
     * Increments the score based on the number of lines that are cleared.
     *
     * @param linesCleared
     */
    private void incrementScore(int linesCleared) {
        switch (linesCleared) {
            case 1:
                score += 100;
                break;
            case 2:
                score += 300;
                break;
            case 3:
                score += 500;
                break;
            case 4:
                score += 800;
                break;
            default:
                break;
        }
    }

    /**
     * Clears lines/rows on the provided tiles/board that are horizontally filled.
     * Repeats this process for cascading effects and updates score accordingly.
     * @param tiles
     */
    public void clearLines(TETile[][] tiles) {
        // Keeps track of the current number lines cleared
        int linesCleared = 0;

        int[] rowsToClear = new int[4];
        boolean isFullRow = true;
        for (int i = 0; i < tiles[0].length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[j][i] == Tileset.NOTHING) {
                    isFullRow = false;
                    break;
                }
            }

            if (isFullRow) {
                linesCleared++;
                rowsToClear[linesCleared - 1] = i;
            } else {
                isFullRow = true;
            }
        }

        incrementScore(linesCleared);

        for (int i = 0; i < linesCleared; i++) {
            for (int j = 0; j < tiles.length; j++) {
                for (int k = rowsToClear[i]; k < tiles[0].length - 1; k++) {
                    tiles[j][k] = tiles[j][k + 1];
                }
            }
            if (i < linesCleared - 1)
                rowsToClear[i + 1] -= 1;
        }

        for (int i = 0; i < linesCleared; i++) {
            for (int j = 0; j < tiles.length; j++) {
                tiles[j][tiles[0].length - 1] = Tileset.NOTHING;
            }
        }

        fillAux();
    }

    /**
     * Where the game logic takes place. The game should continue as long as the game isn't
     * over.
     */
    public void runGame() {
        resetActionTimer();
        spawnPiece();

        while (!isGameOver()) {
            if (currentTetromino == null) {
//                Tetromino.draw(t, board, t.pos.x, t.pos.y);
                spawnPiece();
            }
            updateBoard();
            renderBoard();
            clearLines(board);

        }

        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Game Over!");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 1, "Score: " + score);
        StdDraw.show();

    }

    /**
     * Renders the score using the StdDraw library.
     */
    private void renderScore() {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2 - 3, HEIGHT - 1, "Score: " + score);

    }

    /**
     * Use this method to run Tetris.
     * @param args
     */
    public static void main(String[] args) {
        long seed = args.length > 0 ? Long.parseLong(args[0]) : (new Random()).nextLong();
        Tetris tetris = new Tetris(seed);
        tetris.runGame();
    }

    /**
     * Everything below here you don't need to touch.
     */

    // This is our tile rendering engine.
    private final TERenderer ter = new TERenderer();

    // Used for randomizing which pieces are spawned.
    private Random random;
    private BagRandomizer bagRandom;

    private long prevActionTimestamp;
    private long prevFrameTimestamp;

    // The auxiliary board. At each time step, as the piece moves down, the board
    // is cleared and redrawn, so we keep an auxiliary board to track what has been
    // placed so far to help render the current game board as it updates.
    private TETile[][] auxiliary;
    private boolean auxFilled;

    public Tetris() {
        board = new TETile[WIDTH][GAME_HEIGHT];
        auxiliary = new TETile[WIDTH][GAME_HEIGHT];
        random = new Random(new Random().nextLong());
        bagRandom = new BagRandomizer(random, Tetromino.values().length);
        auxFilled = false;
        movement = new Movement(WIDTH, GAME_HEIGHT, this);
        fillBoard(Tileset.NOTHING);
        fillAux();
    }

    public Tetris(long seed) {
        board = new TETile[WIDTH][GAME_HEIGHT];
        auxiliary = new TETile[WIDTH][GAME_HEIGHT];
        random = new Random(seed);
        bagRandom = new BagRandomizer(random, Tetromino.values().length);
        auxFilled = false;
        movement = new Movement(WIDTH, GAME_HEIGHT, this);

        ter.initialize(WIDTH, HEIGHT);
        fillBoard(Tileset.NOTHING);
        fillAux();
    }

    // Setter and getter methods.

    /**
     * Returns the current game board.
     * @return
     */
    public TETile[][] getBoard() {
        return board;
    }

    /**
     * Returns the score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the current auxiliary board.
     * @return
     */
    public TETile[][] getAuxiliary() {
        return auxiliary;
    }


    /**
     * Returns the current Tetromino/piece.
     * @return
     */
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    /**
     * Sets the current Tetromino to null.
     * @return
     */
    public void setCurrentTetromino() {
        currentTetromino = null;
    }

    /**
     * Sets the boolean auxFilled to true;
     */
    public void setAuxTrue() {
        auxFilled = true;
    }

    /**
     * Fills the entire board with the specific tile that is passed in.
     * @param tile
     */
    private void fillBoard(TETile tile) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = tile;
            }
        }
    }

    /**
     * Copies the contents of the src array into the dest array using
     * System.arraycopy.
     * @param src
     * @param dest
     */
    private static void copyArray(TETile[][] src, TETile[][] dest) {
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dest[i], 0, src[0].length);
        }
    }

    /**
     * Copies over the tiles from the game board to the auxiliary board.
     */
    public void fillAux() {
        copyArray(board, auxiliary);
    }

    /**
     * Copies over the tiles from the auxiliary board to the game board.
     */
    private void auxToBoard() {
        copyArray(auxiliary, board);
    }

    /**
     * Calculates the delta time with the previous action.
     * @return the amount of time between the previous Tetromino movement with the present
     */
    private long actionDeltaTime() {
        return System.currentTimeMillis() - prevActionTimestamp;
    }

    /**
     * Resets the action timestamp to the current time in milliseconds.
     */
    private void resetActionTimer() {
        prevActionTimestamp = System.currentTimeMillis();
    }

}
