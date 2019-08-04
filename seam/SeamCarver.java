import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class SeamCarver {
    private int width;
    private int height;
    private double[][] energy;
    private Color[][] img;
    private boolean isTransposed;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();

        width = picture.width();
        height = picture.height();
        isTransposed = false;
        pictureToColors(picture);
        colorsToEnergy();
    }

    private void pictureToColors(Picture picture) {
        img = new Color[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                img[i][j] = new Color(picture.getRGB(i, j));
    }

    private void colorsToEnergy() {
        energy = new double[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                energy[i][j] = energy(i, j);
    }

    public Picture picture() {
        makeHorizontal();
        Picture pic = new Picture(width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                pic.set(i, j, img[i][j]);
        return pic;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            throw new IllegalArgumentException();
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1)
            return 1000;
        return Math.sqrt(energyBetween(x + 1, y, x - 1, y) + energyBetween(x, y + 1, x, y - 1));
    }

    private double energyBetween(int x1, int y1, int x2, int y2) {
        Color rgb1 = img[x1][y1];
        Color rgb2 = img[x2][y2];

        double dr = rgb1.getRed() - rgb2.getRed();
        double dg = rgb1.getGreen() - rgb2.getGreen();
        double db = rgb1.getBlue() - rgb2.getBlue();

        return dr * dr + dg * dg + db * db;
    }

    public int[] findHorizontalSeam() {
        throw new IllegalArgumentException();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        makeVertical();
        return findHorizontalSeam();
    }


    private void doRemoveHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();
        validateHorizontalSeam(seam);

        int newHeight = height - 1;
        Color[][] newImg = new Color[width][newHeight];
        double[][] newEnergy = new double[width][newHeight];
        for (int i = 0; i < seam.length; i++) {
            int delIdx = seam[i];
            int after = height - delIdx - 1;
            System.arraycopy(img[i], 0, newImg[i], 0, delIdx);
            System.arraycopy(img[i], delIdx + 1, newImg[i], delIdx, after);

            System.arraycopy(energy[i], 0, newEnergy[i], 0, delIdx);
            System.arraycopy(energy[i], delIdx + 1, newEnergy[i], delIdx, after);
        }
        height = newHeight;
        energy = newEnergy;
        img = newImg;

        for (int i = 0; i < seam.length; i++) {
            energy[i][seam[i]] = energy(i, seam[i]);
            energy[i][seam[i]-1] = energy(i, seam[i]-1);
        }
    }


    public void removeHorizontalSeam(int[] seam) {
        makeHorizontal();
        doRemoveHorizontalSeam(seam);
    }

    private void validateHorizontalSeam(int[] seam) {
        if (seam.length != width || height <= 1)
            throw new IllegalArgumentException();

        if (seam[0] < 0 || seam[0] >= height)
            throw new IllegalArgumentException();

        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height)
                throw new IllegalArgumentException();
            if (Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void transpose() {
        int newHeight = width;
        int newWidth = height;
        double[][] newEnergy = new double[newWidth][newHeight];
        Color[][] newImg = new Color[newWidth][newHeight];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                newEnergy[j][i] = energy[i][j];
                newImg[j][i] = img[i][j];
            }
        width = newWidth;
        height = newHeight;
        energy = newEnergy;
        img = newImg;
        isTransposed = !isTransposed;
    }

    private void makeHorizontal() {
        if (isTransposed)
            transpose();
    }

    private void makeVertical() {
        if (!isTransposed)
            transpose();
    }

    public void removeVerticalSeam(int[] seam) {
        makeVertical();
        doRemoveHorizontalSeam(seam);
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("%s (%d-by-%d image)\n", args[0], picture.width(), picture.height());
        picture.show();

        int[] seam = new int[] { 8, 9, 10, 9, 8, 8, 8, 9, 9, 9, 9, 9 };

        SeamCarver sc = new SeamCarver(picture);
        sc.removeVerticalSeam(seam);
        Picture newP = sc.picture();
        newP.show();
    }

    public void energyDebug() {
        makeHorizontal();
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++)
                StdOut.printf("%9.0f ", energy[col][row]);
            StdOut.println();
        }
    }

}