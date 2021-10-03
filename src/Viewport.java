public final class Viewport
{
    public int row;
    public int col;
    public int numRows;
    public int numCols;

    public Viewport(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
    }


    public boolean contains(Point p) {
        return p.y >= this.row && p.y < this.row + this.numRows
                && p.x >= this.col && p.x < this.col + this.numCols;
    }

    public Point worldToViewport(int col, int row) {
        return new Point(col - this.col, row - this.row);
    }

    public  Point viewportToWorld(int col, int row) {
        return new Point(col + this.col, row + this.row);
    }
}
