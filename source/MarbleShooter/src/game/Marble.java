package game;

import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * reprezentuje už pripevenú guličku
 */
public class Marble {
    private MarbleColor color;

    private Marble top;
    private Marble left;
    private Marble right;
    private Marble bottom;

    private Marble topLeft;
    private Marble topRight;
    private Marble bottomLeft;
    private Marble bottomRight;

    private int column;
    private int row;
    private ImageView imageView;

    public Marble(MarbleColor color, int column, int row) {
        this.color = color;
        this.column = column;
        this.row = row;
    }

    public MarbleColor getColor() {
        return color;
    }

    public void setColor(MarbleColor color) {
        this.color = color;
    }

    public List<Marble> getAllNeighbors() {
        List<Marble> neighbors = new ArrayList<>();
        neighbors.add(top);
        neighbors.add(left);
        neighbors.add(right);
        neighbors.add(bottom);
        neighbors.add(topLeft);
        neighbors.add(topRight);
        neighbors.add(bottomLeft);
        neighbors.add(bottomRight);
        return neighbors;
    }


    public Marble getTop() {
        return top;
    }

    public void setTop(Marble top) {
        this.top = top;
    }

    public Marble getLeft() {
        return left;
    }

    public void setLeft(Marble left) {
        this.left = left;
    }

    public Marble getRight() {
        return right;
    }

    public void setRight(Marble right) {
        this.right = right;
    }

    public Marble getBottom() {
        return bottom;
    }

    public void setBottom(Marble bottom) {
        this.bottom = bottom;
    }

    public Marble getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Marble topLeft) {
        this.topLeft = topLeft;
    }

    public Marble getTopRight() {
        return topRight;
    }

    public void setTopRight(Marble topRight) {
        this.topRight = topRight;
    }

    public Marble getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(Marble bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public Marble getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(Marble bottomRight) {
        this.bottomRight = bottomRight;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
