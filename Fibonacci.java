import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Fibonacci
{
    private static final BigInteger BIGINTTWO   = new BigInteger("2");
    private static final int        NUMRUNS     = 1000;
    private static final int        TINYNUMS    = 10;
    private static final int        SMALLNUMS   = 25;
    private static final int        MEDIUMNUMS  = 1000;
    private static final int        LARGENUMS   = 10000;
    private static final int        HUGENUMS    = 100000;
    private static final int        GINORMNUM   = 1000000;

    public static void main(String[] args)
    {
        run(TINYNUMS, NUMRUNS);
        run(SMALLNUMS, NUMRUNS);
        run(MEDIUMNUMS, NUMRUNS);
        run(LARGENUMS, NUMRUNS);
        run(HUGENUMS, NUMRUNS);
        run(GINORMNUM, NUMRUNS);
    }

    private static void run(int numSize, int runNums)
    {
        LinkedList<Long> recursive	= new LinkedList<Long>();
        LinkedList<Long> memo		= new LinkedList<Long>();
        LinkedList<Long> lastTwo	= new LinkedList<Long>();
        LinkedList<Long> doubling	= new LinkedList<Long>();
        for (int i = 0; i < runNums; i++)
        {
            long before;
            long after;
            if (numSize <= SMALLNUMS) // Would take too long otherwise
            {
                BigInteger number = BigInteger.valueOf(numSize);
                before = System.nanoTime();
                fibRecursive(number);
                after = System.nanoTime();
                recursive.add(after-before);
            }
            if (numSize <= HUGENUMS)
            {
                before = System.nanoTime();
                fibMemo(numSize);
                after = System.nanoTime();
                memo.add(after-before);
            }
            before = System.nanoTime();
            fibLastTwo(numSize);
            after = System.nanoTime();
            lastTwo.add(after-before);

            before = System.nanoTime();
            fibDoubling(numSize);
            after = System.nanoTime();
            doubling.add(after-before);

        }

        System.out.println("\n---------------------------------------");
        System.out.println("Fibonacci Number Index: " + numSize);
        System.out.println("---------------------------------------\n");


        System.out.printf("Method\t\t"
                + "Min\t\t"
                + "Max\t\t"
                + "Sum\t\t"
                + "Average (All in milliseconds)\n\n");

        if (numSize < SMALLNUMS) // It takes FAR too long otherwise.
        {
            double minRecursive = (double)Collections.min(recursive) / 1000000.0;
            double maxRecursive = (double)Collections.max(recursive) / 1000000.0;
            double sumRecursive = 0;

            for (long value : recursive)
            {
                sumRecursive += (double)value / 1000000.0;
            }
            double averageRecursive = sumRecursive / (double)recursive.size();
            System.out.printf("Recursive\t"
                    + "%8f\t"
                    + "%8f\t"
                    + "%8f\t"
                    + "%8f\n\n",
                    minRecursive,
                    maxRecursive,
                    sumRecursive,
                    averageRecursive);
        }
        if (numSize <= HUGENUMS)
        {
            double minMemo = (double)Collections.min(memo) / 1000000.0;
            double maxMemo = (double)Collections.max(memo) / 1000000.0;
            double sumMemo = 0;
            for (long value : memo)
            {
                sumMemo += (double)value / 1000000.0;
            }

            System.out.printf("Memo\t\t"
                    + "%8f\t"
                    + "%8f\t"
                    + "%8f\t"
                    + "%8f\n\n",
                    minMemo,
                    maxMemo,
                    sumMemo,
                    sumMemo / (double)memo.size());
        }
        double minLastTwo = (double)Collections.min(lastTwo) / 1000000.0;
        double maxLastTwo = (double)Collections.max(lastTwo) / 1000000.0;
        double sumLastTwo = 0;

        for (long value : lastTwo)
        {
            sumLastTwo += (double)value / 1000000.0;
        }

        System.out.printf("LastTwo\t\t"
                + "%8f\t"
                + "%8f\t"
                + "%8f\t"
                + "%8f\n\n",
                minLastTwo,
                maxLastTwo,
                sumLastTwo,
                sumLastTwo / (double)lastTwo.size());

        double minDoubling = (double)Collections.min(doubling) / 1000000.0;
        double maxDoubling = (double)Collections.max(doubling) / 1000000.0;
        double sumDoubling = 0;

        for (long value : doubling)
        {
            sumDoubling += (double)value / 1000000.0;
        }

        System.out.printf("Doubling\t"
                + "%8f\t"
                + "%8f\t"
                + "%8f\t"
                + "%8f\n",
                minDoubling,
                maxDoubling,
                sumDoubling,
                sumDoubling / (double)doubling.size());



    }

    public static BigInteger fibRecursive(BigInteger number)
    {
        if (number.compareTo(BigInteger.ONE) <= 0)
            return number;
        return fibRecursive(number.subtract(BigInteger.ONE))
                .add(fibRecursive(number
                        .subtract(BIGINTTWO)));
    }

    public static BigInteger fibMemo(int number)
    {
        ArrayList<BigInteger> solutions = new ArrayList<>();
        solutions.add(0, BigInteger.ZERO);
        solutions.add(1, BigInteger.ONE);

        for (int i = 2; i <= number; i++)
        {
            solutions.add(solutions.get(i-2).add(solutions.get(i-1)));
        }
        return solutions.get(number);
    }

    public static BigInteger fibLastTwo(int number)
    {
        BigInteger fib1 = BigInteger.ZERO;
        BigInteger fib2 = BigInteger.ONE;

        for (int i = 1; i <= number; i++)
        {
            BigInteger tmpFib = fib1;
            fib1 = fib1.add(fib2);
            fib2 = tmpFib;
        }

        return fib1;
    }

    public static BigInteger fibDoubling(int number)
    {
        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;
        for (int i = 31 - Integer.numberOfLeadingZeros(number); i >= 0; i--) {

            BigInteger d = a.multiply((b.shiftLeft(1)).subtract(a)); // F(2n)
            b = a.multiply(a).add(b.multiply(b)); // F(2n+1)
            a = d;
            if (((1 << i) & number) != 0) { // advance by one
                BigInteger c = a.add(b);
                a = b;
                b = c;
            }
        }
        return a;
    }
}
