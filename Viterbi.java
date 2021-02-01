import java.util.Scanner;
public class Viterbi
{
    public static void main(String []args)
    {
        //The general idea is to apply the viterbi algorithm to the casino problem
        //I will make a generalized algorithm later
        
        Scanner sc = new Scanner(System.in);
        String sequence = "HHHHTTTTH";
        String result = ""; //F is fair and B is broken smh
        
        double transistion = .1; //Chance for the coins to switch
        double normal = .5; //Chance for the balanced coin to land on heads
        double weights = .75; //Chance for the weighted coin to land on heads
        double initialFair = .5; //Chance that the Fair coin will be selected at the start
        int size = sequence.length(); //Length of sequence
        
        double[][] values = new double[2][size];
        int heads = 1;
        double prob[][] = {{1-normal, normal}, {1-weights, weights}};
        double initProb[] = {initialFair, 1-initialFair};
        double[] swap = {1 - transistion, transistion};
        
        for(int i = 1; i < size; i++)
        {
            if(sequence.charAt(i) == 'H') //Checks if the result is heads or tails
                heads = 1;
            else
                heads = 0;
            if(i == 0) //Sets the initial values based on the initial probability
            {
                values[0][0] = log(initProb[0] * prob[0][heads]);
                values[1][0] = log(initProb[1] * prob[1][heads]);
            }
            
            //Probability of P(x|pi)
            double emissionF = prob[0][heads];
            double emissionW = prob[1][heads];
            
            //Probability of P(x->x) and P(x|pi) for Fair
            double fairToFair = values[0][i-1] + log(swap[0] * emissionF);
            double weightedToFair = values[1][i-1] + log(swap[1] * emissionF);
            
            //Probability of P(x->x) and P(x|pi) for Weighted
            double weightedToWeighted = values[1][i-1] + log(swap[0] * emissionW);
            double fairToWeighted = values[0][i-1] + log(swap[1] * emissionW);
            
            //Next Fair Values
            values[0][i] = max(fairToFair, weightedToFair);
            
            //Next Weighted Values
            values[1][i] = max(weightedToWeighted, fairToWeighted);
            
        }
        
        for(int i = 0; i < size; i++)
        {
            result += (values[0][i] > values[1][i]) ? "F" : "B";
        }
        
        System.out.println(sequence);
        System.out.println(result);
    }
    
    public static double log(double d)
    {
        return Math.log(d) / Math.log(10);
    }
    
    public static double max(double a, double b)
    {
        return (a > b) ? a : b;
    }
    
}
