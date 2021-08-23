import java.util.Scanner;
public class SmithWaterman
{
    public static void main(String []args)
    {
        Scanner sc = new Scanner(System.in);
        //Sequences retrieved from the user input
        //String sequence1 = sc.nextLine();
        //String sequence2 = sc.nextLine();
        //Sequences hardcoded for testing purposes
        String sequence1 = "atttaaccgtac"; 
        String sequence2 = "gctaccatggaggcta";

        //The two sequences will be compared to each other via a grid/2d array.
        char[] seq1 = sequence1.toCharArray();
        char[] seq2 = sequence2.toCharArray();
        int[][] map = new int[seq2.length + 1][seq1.length + 1];
        String[][] indices = new String[seq2.length + 1][seq1.length + 1];

        //The Smith-Waterman algorithm tries to pair the largest common segment together.
        //The returned pair starts at the first positive valued pair and ends at the highest valued pair.
        //This allows the pairing to be more accurate and is the successor to the Needleman-Wunsch algorithm.

        //These values are used to calculate the score of the specific alignment.
        //The rules of how they are calculated will be explained later.
        int match = 2;
        int mismatch = -1;
        int gap = -2;

        //Sets the initial values for the graph
        for(int i = 0; i < map.length; i++)
            map[i][0] = gap * i;
        for(int i = 0; i < map[0].length; i++)
            map[0][i] = gap * i;            

        //Creating the rest of the graph
        for(int i = 1; i < map.length; i++)
        {
            for(int j = 1; j < map[i].length; j++)
            {
                /*
                 * When generating the subsequent values of the map, SW uses dynamic programming
                 * The value at [i,j] is determined by the highest value from 3 options
                 *  1) Going diagonal which means no gap needs to be inserted. 
                 *     The added score is simply its match/mismatch score.
                 *  2) Adding a gap to strand 1 which adds a gap penalty but nothing else.
                 *  3) Adding a gap to strand 2 which also adds a gap penalty.
                 */
                int option = 1;
                int max = map[i - 1][j - 1];
                if(seq1[j - 1] == seq2[i - 1])
                    max += match;
                else
                    max += mismatch;
                if(max < map[i - 1][j] + gap)
                {
                    max = map[i - 1][j] + gap;
                    option = 2;
                }
                if(max < map[i][j - 1] + gap)
                {
                    max = map[i][j - 1] + gap;
                    option = 3;
                }

                map[i][j] = max;

                switch(option)
                {
                    case 1: indices[i][j] = (j - 1) + ":" + (i - 1); break;
                    case 2: indices[i][j] = (j) + ":" + (i - 1); break;
                    case 3: indices[i][j] = (j - 1) + ":" + (i); break;
                }
            }
        }

        //Printing out the stuff for convienence/sanity
        System.out.println("Needleman-Wunsch Graph");
        print(map, sequence1, sequence2); //if print 1 is so good, why isn't there a print 2?
        System.out.println("\nIndex Version");
        print2(indices); //oh shit it's print 2

        //Creating the "optimal" sequence by traversing backwards
        int length1 = sequence1.length(), length2 = sequence2.length();

        //The current X and Y are the ends because NW tries to pair the entire sequence.
        //If you want to only stop at the max value, then that's Smith-Waterman
        int currentX = length1, currentY = length2;
        int maxValue = -1;
        
        //Finds the point with the highest score and sets the start to there.
        for(int i = 1; i <= length1; i++) {
            for(int j = 1; j <= length2; j++) {
                if(map[j][i] > maxValue) {
                    maxValue = map[j][i];
                    String location = indices[j][i];
                    String[] temp = location.split(":");
                    currentX = Integer.parseInt(temp[0]);
                    currentY = Integer.parseInt(temp[1]);
                }
            }
        }
        
        int oldX = -1, oldY = -1;
        String aligned1 = "";
        String aligned2 = "";
        String alignedValue = "";

        while(oldX != 0 && oldY != 0) //while not at the end of the plot
        {
            //if the prevous and current X locations are the same, there's a gap
            if(oldX != -1)
                if(currentX != oldX)
                    aligned1 += sequence1.charAt(oldX);
                else
                    aligned1 += "-";
            //same idea but with the Y location and sequence 2
            if(oldY != -1)
                if(currentY != oldY && oldY != -1)
                    aligned2 += sequence2.charAt(oldY);
                else
                    aligned2 += "-";

            //checks the value and if it is zero or less it exits
            if(map[currentY][currentX] <= 0)
                break;
            String location = indices[currentY][currentX];
            String[] temp = location.split(":");
            oldX = currentX;
            oldY = currentY;
            currentX = Integer.parseInt(temp[0]);
            currentY = Integer.parseInt(temp[1]);
        }

        //Prints out the aligned sequence
        System.out.print("\nSequence 1 Aligned: ");
        for(int i = aligned1.length() - 1; i >= 0; i--)
            System.out.print(aligned1.charAt(i));
        System.out.print("\nSequence 2 Aligned: ");
        for(int i = aligned2.length() - 1; i >= 0; i--)
            System.out.print(aligned2.charAt(i));
        System.out.println();

    }

    //Prints out the character grid
    private static void print(int[][] map, String sequence1, String sequence2)
    {
        System.out.print("      ");
        for(int i = 0; i < map[0].length - 1; i++)
            System.out.printf("%4c", sequence1.charAt(i));
        System.out.println();
        for(int i = 0; i < map.length; i++)
        {
            if(i == 0)
                System.out.print("   ");
            else
                System.out.printf("%2c ", sequence2.charAt(i - 1));
            for(int j = 0; j < map[i].length; j++)  
                System.out.printf("%3d ", map[i][j]);
            System.out.println();
        }
    }

    //Prints our the target coordinates of the grid.
    private static void print2(String[][] str)
    {
        for(String[] a : str)
        {
            for(String b : a)
                System.out.printf("%5s", b);
            System.out.println();
        }
    }
}
