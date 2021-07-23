package game;

/**
 * reprezentuje vystrelenú guličku
 */
class ShootingMarble {
    private double x;
    private double y;
    private double dx;
    private double dy;
    private double size;
    private MarbleColor color;

    public ShootingMarble(double x, double y, double dx, double dy, double size, MarbleColor color) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.size = size;
        this.color = color;
    }

    public void update(double w, double h) {
        x += dx;
        y += dy;
        if (x < size)
            dx = -dx;
        if (y < size)
            dy = -dy;
        if (x > w - size)
            dx = -dx;
        if (y > h - size)
            dy = -dy;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public MarbleColor getColor() {
        return color;
    }

    public void setColor(MarbleColor color) {
        this.color = color;
    }
}
