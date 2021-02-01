import java.util.Scanner;
public class BurrowsWheeler
{
    public static void main(String []args)
    {
        //Gets the input from the user
        Scanner sc = new Scanner(System.in);

        //Scans for the string and appends the dummy character
        String T = sc.nextLine() + "$";

        //Variable declaration since I use this a lot
        int length = T.length();

        //All the possible rotations. Produces a square matrix.
        char[][] BWMatrix = new char[length][length];

        //Gets all the possible rotations
        for(int i = 0; i < length; i++)
            for(int j = 0; j < length; j++)
                BWMatrix[i][j] = T.charAt((i + j) % length);

        //Sorts the strings
        char[] temp = new char[length];
        boolean swap = false;
        for(int i = 0; i < length - 1; i++)
            for(int j = i + 1; j < length; j++)
            {    
                for(int k = 0; k < length && !swap; k++)
                    if(BWMatrix[i][k] < BWMatrix[j][k])
                        break;
                    else if(BWMatrix[i][k] > BWMatrix[j][k])
                        swap = true;
                if(swap)
                {
                    temp = BWMatrix[i];
                    BWMatrix[i] = BWMatrix[j];
                    BWMatrix[j] = temp;
                    swap = false;
                }
            }

        //Gets the Burrows-Wheeler String and the sorted string
        char[] BWString = new char[length];
        char[] sorted = new char[length];
        char temp2;

        //Gets the Burrows-Wheeler Transformed String from the last column
        for(int i = 0; i < length; i++)
            BWString[i] += BWMatrix[i][length - 1];

        //Getting the sorted value. Note you normally wouldn't do this.
        //Mainly because it's a waste of memory
        for(int i = 0; i < length; i++)
            sorted[i] = BWMatrix[i][0];

        //A really bad way to get the original string back omegalul
        int count1 = 0; //Character counter for the BWT String
        int count2 = 0; //Character counter for the sorted String
        char ch = BWString[0]; //Setting the pointer beforehand
        char[] output = new char[length]; //The translated String
        int index = 0; //The index of the algorithm
        for(int i = 0; i < length; i++)
        {
            //Write the value found at the index
            output[length - 1 - i] = sorted[index];

            //Get the count for the char
            for(int j = 0; j < index + 1; j++)
                if(ch == BWString[j])
                    count1++;

            //Now get the respective value from the sorted array
            for(int j = 0; j < length; j++)
                if(sorted[j] == ch)
                    if(count2 == count1 - 1)
                    {
                        index = j;
                        break;
                    }    
                    else
                        count2++;

            //Reset the counters and set the new character
            count1 = 0;
            count2 = 0;
            ch = BWString[index];
        }

        //Printing functions
        System.out.println("BURROWS-WHEELER MATRIX");
        for(int i = 0; i < length; i++)
        {
            for(int j = 0; j < length; j++)
                System.out.print(BWMatrix[i][j]);
            System.out.println();
        }
        System.out.println();
        System.out.println("BURROWS-WHEELER TRANSFORMED STRING");
        for(char c : BWString)
            System.out.print(c);
        System.out.println("\n");
        System.out.println("SORTED STRING");
        for(char c : sorted)
            System.out.print(c);
        System.out.println("\n");
        System.out.println("TRANSLATED STRING");
        //You can always chop off the last character but I left it in
        for(char c : output)
            System.out.print(c);
        System.out.println();
    }
}
