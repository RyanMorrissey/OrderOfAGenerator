/*
	Author: Ryan Morrissey
	Course: CSCI.251.02 - Concepts of Parallel and Distributed Systems
	Date: 3/13/2016
	
	This program takes advantage of the Parallel Java 2 Library to calculate the order of a generator.
	Computation is partitioned among multiple threads so as to experience a speedup when run on a multicore
	parallel computer.
	
	Includes main
*/

import edu.rit.pj2.Task;
import edu.rit.pj2.Loop;
import java.math.BigInteger;

public class Order extends Task
{
    // I am having them be an int to fulfill requirements, but will be mainly using
    // the BigInteger version for calculations to prevent needless conversions.
    int modulus; // <p> value given from args
    int generator; // <g> value given from args
    int value = 0; // This will hold the smalled value of g^n mod p.
    BigInteger mod;
    BigInteger gen;
    
    public void main(String[] args)
    {
        // First to make sure the arguments are valid
        if(args.length != 2) usage();
        if(isNumeric(args[0]) == true)
        {
            modulus = Integer.parseInt(args[0]);
            mod = new BigInteger(args[0]);
            if(mod.isProbablePrime(64) == false) usage();
            if(modulus < 2) usage();
        }
        if(isNumeric(args[1]) == true)
        {
            generator = Integer.parseInt(args[1]);
            gen = new BigInteger(args[1]);
            if(generator < 1) usage();
            if(generator > (modulus - 1)) usage();
        }
        
        parallelFor (1, (modulus - 1)) .exec (new Loop()
        {
            // The run function, it will simple do the calculation needed
            public void run (int n)
            {
                int val = gen.modPow(BigInteger.valueOf(n), mod).intValue();
                if(val == 1)
                {
                    if(value == 0) value = n; // If value hasnt been set, then set it
                    else if(n < value) value = n; // If we found a smaller n, then set it
                }
            }
        });
        
        System.out.println(value);
    }
    
    // Hidden operations
    // Prints the usage message and exits
    public static void usage()
    {
        System.err.println("Usage: java p2j edu.rit.pj2.example.Order <p> <g>");
        System.err.println("<p> = modulus (must be prime)");
        System.err.println("<g> = generator (in the range 1 ≤ g ≤ p−1");
        terminate(1);
    }
	
    // Checks to see if a given string is actually a valid integer
    public static boolean isNumeric(String str)  
    {  
        try  
        {  
            double d = Integer.parseInt(str);  
        }  
        catch(NumberFormatException nfe)  
        {  
            return false;  
        }  
        return true;  
    }
}