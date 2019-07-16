import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture carvedPicture;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();

        carvedPicture = new Picture(picture.width(), picture.height());
        for (int col = 0; col < picture.width(); col++)
            for (int row = 0; row < picture.height(); row++)
                carvedPicture.set(col, row, picture.get(col, row));
    }

    public Picture picture() {
        return carvedPicture;
    }

    public int width() {
        return carvedPicture.width();
    }

    public int height() {
        return carvedPicture.height();
    }

    public double energy(int x, int y) {
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1)
            return 1000;
        return energyBetween(x + 1, y, x - 1, y) + energyBetween(x, y + 1, x, y - 1);
    }

    private double energyBetween(int x1, int y1, int x2, int y2) {
        Color rgb1 = new Color(carvedPicture.getRGB(x1, y1));
        Color rgb2 = new Color(carvedPicture.getRGB(x2, y2));

        double dr = rgb1.getRed() - rgb2.getRed();
        double dg = rgb1.getGreen() - rgb2.getGreen();
        double db = rgb1.getBlue() - rgb2.getBlue();

        return dr * dr + dg * dg + db * db;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        throw new IllegalArgumentException();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        throw new IllegalArgumentException();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        throw new IllegalArgumentException();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        throw new IllegalArgumentException();
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        throw new IllegalArgumentException();
    }

}