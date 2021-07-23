package game;

/**
 * reprezentuje level
 */
public class Level {
    private int initialNumberOfRows;
    private MarbleColor[] colors;
    private int triesPerRow;
    private String backgroundUrl;

    public Level(int initialNumberOfRows, MarbleColor[] colors, int triesPerRow, String backgroundUrl) {
        this.initialNumberOfRows = initialNumberOfRows;
        this.colors = colors;
        this.triesPerRow = triesPerRow;
        this.backgroundUrl = backgroundUrl;
    }

    public int getInitialNumberOfRows() {
        return initialNumberOfRows;
    }

    public void setInitialNumberOfRows(int initialNumberOfRows) {
        this.initialNumberOfRows = initialNumberOfRows;
    }

    public MarbleColor[] getColors() {
        return colors;
    }

    public void setColors(MarbleColor[] colors) {
        this.colors = colors;
    }

    public int getTriesPerRow() {
        return triesPerRow;
    }

    public void setTriesPerRow(int triesPerRow) {
        this.triesPerRow = triesPerRow;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }
}
