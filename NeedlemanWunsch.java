import java.util.Scanner;
public class NeedlemanWunsch
{
    //TODO: add more info printing and clean up code
    public static void main(String []args)
    {
        Scanner sc = new Scanner(System.in);
        //String sequence1 = sc.nextLine();
        //String sequence2 = sc.nextLine();
        String sequence1 = "atttaa"; //temp values for now
        String sequence2 = "attcaga";
        char[] seq1 = sequence1.toCharArray();
        char[] seq2 = sequence2.toCharArray();
        int[][] map = new int[seq2.length + 1][seq1.length + 1];
        String[][] indices = new String[seq2.length + 1][seq1.length + 1];

        //The Needleman-Wunsch algorithm serves to completely align two sequences.
        //During the alignment it also introduces gaps to help make sure it does not catch on fire
        //The alignment itself is a refinement of the dot-plot alignment
        //
        int match = 2; //temp
        int mismatch = -1; //temp
        int gap = -2; //temp

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
        print(map, sequence1, sequence2);
        System.out.println("\nIndex Version");
        print2(indices);

        //Creating the "optimal" sequence by traversing backwards
        int length1 = sequence1.length(), length2 = sequence2.length();
        int currentX = length1, currentY = length2;
        int oldX = -1, oldY = -1;
        String aligned1 = "" + sequence1.charAt(length1 - 1);
        String aligned2 = "" + sequence2.charAt(length2 - 1);

        while(true)
        {
            String location = indices[currentY][currentX];
            if(oldX == 0 && oldY == 0)
                break;
            String[] temp = location.split(":");
            currentX = Integer.parseInt(temp[0]);
            currentY = Integer.parseInt(temp[1]);
            if(oldX != -1)
                if(currentX != oldX)
                    aligned1 += sequence1.charAt(oldX - 1);
                else
                    aligned1 += "-";
            if(oldY != -1)
                if(currentY != oldY)
                    aligned2 += sequence2.charAt(oldY - 1);
                else
                    aligned1 += "-";
            oldX = currentX;
            oldY = currentY;
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
