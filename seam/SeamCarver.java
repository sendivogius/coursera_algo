import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SeamCarver {
    private int width;
    private int height;
    private double[][] energy;
    private int[][] imgRGB;
    private boolean isTransposed;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();

        width = picture.width();
        height = picture.height();
        isTransposed = false;
        pictureToColors(picture);
        energy = new double[width][height];
        colorsToEnergy();
    }

    private void pictureToColors(Picture picture) {
        imgRGB = new int[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                imgRGB[i][j] = picture.getRGB(i, j);
            }
    }

    private void colorsToEnergy() {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                energy[i][j] = energy(i, j);
    }

    public Picture picture() {
        makeHorizontal();
        Picture pic = new Picture(width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                pic.setRGB(i, j, imgRGB[i][j]);
        return pic;
    }

    public int width() {
        makeHorizontal();
        return width;
    }

    public int height() {
        makeHorizontal();
        return height;
    }

    public double energy(int x, int y) {
        makeHorizontal();
        if (x < 0 || y < 0 || x >= width || y >= height)
            throw new IllegalArgumentException();

        return calcEnergy(x, y);
    }

    private double calcEnergy(int x, int y) {
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1)
            return 1000.0;
        return Math.sqrt(energyBetween(x + 1, y, x - 1, y) + energyBetween(x, y + 1, x, y - 1));
    }

    private int energyBetween(int x1, int y1, int x2, int y2) {
        int rgb1 = imgRGB[x1][y1];
        int rgb2 = imgRGB[x2][y2];

        int dr = (rgb1 & 0xFF) - (rgb2 & 0xFF);
        int dg = ((rgb1 >> 8) & 0xFF) - ((rgb2 >> 8) & 0xFF);
        int db = ((rgb1 >> 16) & 0xFF) - ((rgb2 >> 16) & 0xFF);

        return dr * dr + dg * dg + db * db;
    }

    public int[] findHorizontalSeam() {
        makeHorizontal();
        return doFindHorizontalSeam();
    }

    private int[] doFindHorizontalSeam() {
        int[] dys = { 0, -1, 1 };

        // temp variables
        double currentEnergy;
        int[][] pred = new int[width][height];
        double[][] shortestEnergy = new double[width][height];
        for (double[] row : shortestEnergy)
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        Arrays.fill(shortestEnergy[0], 1000);

        // calculate shortest energies
        for (int col = 1; col < width; col++)
            for (int row = 0; row < height; row++) {
                currentEnergy = energy[col][row];
                int neigX = col - 1;
                int neigY;
                for (int dy : dys) {
                    neigY = row + dy;
                    if (neigY < 0 || neigY >= height)
                        continue;
                    double energyThrNeigh = currentEnergy + shortestEnergy[neigX][neigY];
                    if (energyThrNeigh < shortestEnergy[col][row]) {
                        shortestEnergy[col][row] = energyThrNeigh;
                        pred[col][row] = neigY;
                    }
                }
            }

        // find minimum energy
        double[] lastColEnergies = shortestEnergy[width - 1];
        int minRow = 0;
        for (int i = 1; i < height - 1; i++) {
            if (lastColEnergies[i] < lastColEnergies[minRow])
                minRow = i;
        }

        // track back path
        Stack<Integer> path = new Stack<>();
        path.push(minRow);
        for (int currentCol = width - 1; currentCol > 0; currentCol--) {
            minRow = pred[currentCol][minRow];
            path.push(minRow);
        }

        int[] finalPath = new int[width];
        for (int i = 0; i < width; i++)
            finalPath[i] = path.pop();

        return finalPath;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        makeVertical();
        int[] seam = doFindHorizontalSeam();
        // makeHorizontal();
        return seam;
    }


    private void doRemoveHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();
        validateHorizontalSeam(seam);

        int newHeight = height - 1;
        int[][] newImg = new int[width][newHeight];
        double[][] newEnergy = new double[width][newHeight];
        for (int i = 0; i < seam.length; i++) {
            int delIdx = seam[i];
            int after = height - delIdx - 1;
            System.arraycopy(imgRGB[i], 0, newImg[i], 0, delIdx);
            System.arraycopy(imgRGB[i], delIdx + 1, newImg[i], delIdx, after);

            System.arraycopy(energy[i], 0, newEnergy[i], 0, delIdx);
            System.arraycopy(energy[i], delIdx + 1, newEnergy[i], delIdx, after);
        }
        height = newHeight;
        energy = newEnergy;
        imgRGB = newImg;

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < height)
                energy[i][seam[i]] = calcEnergy(i, seam[i]);
            if (seam[i] > 0)
                energy[i][seam[i] - 1] = calcEnergy(i, seam[i] - 1);
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
        int[][] newImg = new int[newWidth][newHeight];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                newEnergy[j][i] = energy[i][j];
                newImg[j][i] = imgRGB[i][j];
            }
        width = newWidth;
        height = newHeight;
        energy = newEnergy;
        imgRGB = newImg;
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
        Picture picture = new Picture("rand.png");
        StdOut.printf("%s (%d-by-%d image)\n", args[0], picture.width(), picture.height());
        picture.show();

        SeamCarver sc = new SeamCarver(picture);
        // sc.debugEnergy();
        StdOut.println(sc.width());
        StdOut.println(sc.width());
        StdOut.println(sc.findVerticalSeam());
        StdOut.println(sc.width());
        StdOut.println(sc.width());
        StdOut.println(sc.height());
        StdOut.println(sc.width());

    }

}
