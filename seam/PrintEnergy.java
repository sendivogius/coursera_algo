/******************************************************************************
 *  Compilation:  javac PrintEnergy.java
 *  Execution:    java PrintEnergy input.png
 *  Dependencies: SeamCarver.java
 *                
 *
 *  Read image from file specified as command line argument. Print energy
 *  of each pixel as calculated by SeamCarver object. 
 * 
 ******************************************************************************/

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class PrintEnergy {

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
        
        SeamCarver sc = new SeamCarver(picture);
        
        StdOut.printf("Printing energy calculated for each pixel.\n");

        // sc.energyDebug();

        int[] seam = new int[] {2, 1, 2, 3, 2, 3, 4, 5, 6, 7};
        sc.removeVerticalSeam(seam);

        StdOut.printf("Removed seam, recal optm.\n");
        sc.energyDebug();

        StdOut.printf("Removed seam, recal everyhtin.\n");
        SeamCarver sc2 = new SeamCarver(sc.picture());

        sc2.energyDebug();

    }

}
