package parallel;

import java.util.Random;

import junit.framework.TestCase;

public class ReciprocalArraySumTest extends TestCase {
    // Number of times to repeat each test, for consistent timing results.
    final static private int REPEATS = 60;

    private static int getNCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Create a double[] of length N to use as input for the tests.
     *
     * @param N Size of the array to create
     * @return Initialized double array of length N
     */
    private double[] createArray(final int N) {
        final double[] input = new double[N];
        final Random rand = new Random(314);

        for (int i = 0; i < N; i++) {
            input[i] = rand.nextInt(100);
            if (input[i] == 0.0) {
                i--;
            }
        }

        return input;
    }

    /**
     * A reference implementation of seqArraySum, in case the one in the main source file is accidentally modified.
     *
     * @param input Input to sequentially compute a reciprocal sum over
     * @return Reciprocal sum of input
     */
    private double seqArraySum(final double[] input) {
        double sum = 0;

        // Compute sum of reciprocals of array elements
        for (double num : input) {
            sum += 1 / num;
        }

        return sum;
    }

    /**
     * A helper function for tests of the two-task parallel implementation.
     *
     * @param N                  The size of the array to test
     * @param useManyTaskVersion Switch between two-task and many-task versions of the code
     * @param nTasks             Number of tasks to use
     * @return The speedup achieved, not all tests use this information
     */
    private double parTestHelper(final int N, final boolean useManyTaskVersion, final int nTasks) {
        // Create a random input
        final double[] input = createArray(N);

        // Use a reference sequential version to compute the correct result
        final double correct = seqArraySum(input);

        // Use the parallel implementation to compute the result
        double sum;
        if (useManyTaskVersion) {
            sum = ReciprocalArraySum.parManyTaskArraySum(input, nTasks);
        } else {
            assert nTasks == 2;
            sum = ReciprocalArraySum.parArraySum(input);
        }

        final double err = Math.abs(sum - correct);
        final String errMsg = String.format("Mismatch in result for N = %d, expected = %f, computed = %f, absolute " + "error = %f", N, correct, sum, err);
        assertTrue(errMsg, err < 1E-2);

        /*
         * Run several repeats of the sequential and parallel versions to get an accurate measurement of parallel performance.
         */
        final long seqStartTime = System.currentTimeMillis();
        for (int r = 0; r < REPEATS; r++) {
            seqArraySum(input);
        }
        final long seqEndTime = System.currentTimeMillis();

        final long parStartTime = System.currentTimeMillis();
        for (int r = 0; r < REPEATS; r++) {
            if (useManyTaskVersion) {
                ReciprocalArraySum.parManyTaskArraySum(input, nTasks);
            } else {
                ReciprocalArraySum.parArraySum(input);
            }
        }
        final long parEndTime = System.currentTimeMillis();

        final long seqTime = (seqEndTime - seqStartTime) / REPEATS;
        final long parTime = (parEndTime - parStartTime) / REPEATS;

        System.out.println(seqTime);
        System.out.println(parTime);

        return (double) seqTime / (double) parTime;
    }

    /**
     * Test that the two-task parallel implementation properly computes the results for a million-element array.
     */
    public void testParSimpleTwoMillion() {
        // Meaning the parallel version will at-least be 50% faster.
        final double minimalExpectedSpeedup = 1.5;

        final double speedup = parTestHelper(2_000_000, false, 2);
        final String errMsg = String.format("It was expected that the two-task parallel implementation would run at " + "least %fx faster, but it only achieved %fx speedup", minimalExpectedSpeedup, speedup);
        assertTrue(errMsg, speedup >= minimalExpectedSpeedup);
    }

    /**
     * Test that the two-task parallel implementation properly computes the results for a hundred million-element array.
     */
    public void testParSimpleTwoHundredMillion() {
        final double minimalExpectedSpeedup = 1.5;

        final double speedup = parTestHelper(200_000_000, false, 2);
        final String errMsg = String.format("It was expected that the two-task parallel implementation would run at " + "least %fx faster, but it only achieved %fx speedup", minimalExpectedSpeedup, speedup);
        assertTrue(errMsg, speedup >= minimalExpectedSpeedup);
    }

    /**
     * Test that the many-task parallel implementation properly computes the results for a million-element array.
     */
    public void testParManyTaskTwoMillion() {
        final int nCores = getNCores();
        // The idea is that without overheads the version will nCores faster...
        final double minimalExpectedSpeedup = (double) nCores * 0.6;

        final double speedup = parTestHelper(2_000_000, true, nCores);
        final String errMsg = String.format("It was expected that the many-task parallel implementation would run at " + "least %fx faster, but it only achieved %fx speedup", minimalExpectedSpeedup, speedup);
        assertTrue(errMsg, speedup >= minimalExpectedSpeedup);
    }

    /**
     * Test that the many-task parallel implementation properly computes the results for a hundred million-element array.
     */
    public void testParManyTaskTwoHundredMillion() {
        final int nCores = getNCores();
        final double minimalExpectedSpeedup = (double) nCores * 0.8;

        final double speedup = parTestHelper(200_000_000, true, nCores);
        final String errMsg = String.format("It was expected that the many-task parallel implementation would run at " + "least %fx faster, but it only achieved %fx speedup", minimalExpectedSpeedup, speedup);
        assertTrue(errMsg, speedup >= minimalExpectedSpeedup);
    }
}
