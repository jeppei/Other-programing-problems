/*
 * A number of buildings is creating a skyling which needs to be painted on a wall. How many horizontal strokes do you need to paint the buildings.
 * For instancce this skyline requires 6 strokes.
 * 
 *   #
 *   #  ##
 *   ## ####
 *  ########
 *  
 * Given an array of each buildings height, determine the number of strokes. The heights in the example above correspondes to the following array {1, 4, 2, 1, 3, 3, 2, 2}.
 */

package Projects.Jobs;

/**
 *
 * @author User
 */
public class Skyline {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { new Skyline(args); }
        int change;
    
    Skyline( String[] input) {
        //int[] input =  {5, 4, 3, 2, 1, 2, 3, 4, 5};       // The input
        int noStrokes = Integer.parseInt(input[0]);         // We must do the strokes for the first skyline
        
        for (int i = 0; i < input.length-1; i++) {
            change = Integer.parseInt(input[i+1])-Integer.parseInt(input[i]);   // The change between two adjacent skylines
            if (change>0) {noStrokes=noStrokes+change; }                        // If the change is positive then we need more strokes
                                                                                // If the change is negative or zero no new strokes appear
            if (noStrokes> 1000000000) { i=input.length; }                      // The algorithm stops if the number of strokes exceeds 1000000000 
        }
        
        if (noStrokes> 1000000000) {System.out.println(-1); }       // We return -1 if the number of strokes exceeds 1000000000
        else                       {System.out.println(noStrokes);} // Otherwise return the number of strokes.
    }
    
}
