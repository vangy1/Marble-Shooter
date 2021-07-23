package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MarbleCreator {
    /**
     * vygeneruje riadok lopticiek
     * farba sa zvolí náhodne z farieb poskytnutých v parametri
     *
     * @param colors farby na výber guličiek
     * @return riadok guličiek
     */
    public List<Marble> generateMarbleRow(MarbleColor[] colors) {
        List<Marble> marbleRow = new ArrayList<>();
        for (int i = 0; i < MarbleShooterPane.PANE_WIDTH / MarbleShooterPane.MARBLE_SIZE; i++) {
            Marble marble = new Marble(colors[new Random().nextInt(colors.length)], i, 0);
            marbleRow.add(marble);
        }
        return marbleRow;
    }

    /**
     * na základe dotyku vystrelenej guličky vypočíta v akom smere má vytvoriť novú guličku a vráti ju
     *
     * @param marbleBeingShot vystrelená gulička
     * @param hitMarble       gulička ktorej sa vystrelená gulička dotkla
     * @return nova gulička
     */
    public Marble createNewMarble(ShootingMarble marbleBeingShot, Marble hitMarble) {
        double xOfContact = marbleBeingShot.getX() + 20;
        double yOfContact = marbleBeingShot.getY() + 20;

        double xMin = hitMarble.getColumn() * MarbleShooterPane.MARBLE_SIZE;
        double xCen = xMin + MarbleShooterPane.HALF_MARBLE_SIZE;
        double yMin = hitMarble.getRow() * MarbleShooterPane.MARBLE_SIZE;
        double yCen = yMin + MarbleShooterPane.HALF_MARBLE_SIZE;

        double xDiff = Math.abs(xCen - xOfContact);
        double yDiff = Math.abs(yCen - yOfContact);

        if (yOfContact > yCen) {
            if (xOfContact > xCen) {  // 2 quadrant
                if (xDiff > yDiff) {
                    if (hitMarble.getRight() == null) {
                        return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() + 1, hitMarble.getRow());     // right
                    } else {
                        if (hitMarble.getBottomRight() == null) {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() + 1, hitMarble.getRow() + 1);
                        } else {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn(), hitMarble.getRow() + 1);        // bottom
                        }
                    }
                } else {
                    if (hitMarble.getBottom() == null) {
                        return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn(), hitMarble.getRow() + 1);        // bottom
                    } else {
                        if (hitMarble.getBottomRight() == null) {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() + 1, hitMarble.getRow() + 1);
                        } else {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() + 1, hitMarble.getRow());     // right
                        }
                    }
                }
            } else {                // 3 quadrant
                if (xDiff > yDiff) {
                    if (hitMarble.getLeft() == null) {
                        return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() - 1, hitMarble.getRow());     // left
                    } else {
                        if (hitMarble.getBottomLeft() == null) {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() - 1, hitMarble.getRow() + 1);
                        } else {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn(), hitMarble.getRow() + 1);       // bottom
                        }
                    }
                } else {
                    if (hitMarble.getBottom() == null) {
                        return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn(), hitMarble.getRow() + 1);       // bottom
                    } else {
                        if (hitMarble.getBottomLeft() == null) {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() - 1, hitMarble.getRow() + 1);
                        } else {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() - 1, hitMarble.getRow());     // left
                        }
                    }
                }
            }
        } else {
            if (xOfContact > xCen) {  // 1 quadrant
                if (xDiff > yDiff) {
                    if (hitMarble.getRight() == null) {
                        return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() + 1, hitMarble.getRow());     // right
                    } else {
                        if (hitMarble.getTopRight() == null) {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() + 1, hitMarble.getRow() - 1);
                        } else {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn(), hitMarble.getRow() - 1);       // top
                        }
                    }
                } else {
                    if (hitMarble.getTop() == null) {
                        return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn(), hitMarble.getRow() - 1);       // top
                    } else {
                        if (hitMarble.getTopRight() == null) {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() + 1, hitMarble.getRow() - 1);
                        } else {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() + 1, hitMarble.getRow());     // right
                        }
                    }
                }
            } else {                // 4 quadrant
                if (xDiff > yDiff) {
                    if (hitMarble.getLeft() == null) {
                        return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() - 1, hitMarble.getRow());    // left
                    } else {
                        if (hitMarble.getTopLeft() == null) {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() - 1, hitMarble.getRow() - 1);
                        } else {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn(), hitMarble.getRow() - 1);      // top
                        }
                    }
                } else {
                    if (hitMarble.getTop() == null) {
                        return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn(), hitMarble.getRow() - 1);      // top
                    } else {
                        if (hitMarble.getTopLeft() == null) {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() - 1, hitMarble.getRow() - 1);
                        } else {
                            return new Marble(marbleBeingShot.getColor(), hitMarble.getColumn() - 1, hitMarble.getRow());    // left

                        }
                    }
                }
            }
        }
    }

}
