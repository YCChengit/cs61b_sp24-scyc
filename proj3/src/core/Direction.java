package core;

public enum Direction {
    ORIGIN(0,0), UP(0,1), DOWN(0, -1), LEFT(-1,0), RIGHT(1,0);

    public final int dx;
    public final int dy;

    Direction(int x, int y){
        this.dx = x;
        this.dy = y;
    }
}
