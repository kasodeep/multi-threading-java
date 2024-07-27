package parallel;

import junit.framework.TestCase;

import static parallel.OneDimAveragingPhaser.runParallelBarrier;
import static parallel.OneDimAveragingPhaser.runParallelFuzzyBarrier;

public class OneDimAveragingPhaserTest extends TestCase {

    final static private int nIterations = 40000;

    private static int getNCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    private double[] createArray(final int N) {
        final double[] input = new double[N + 2];
        int index = N + 1;
        while (index > 0) {
            input[index] = 1.0;
            index -= (nIterations / 4);
        }
        return input;
    }

    private void checkResult(final double[] ref, final double[] output) {
        for (int i = 0; i < ref.length; i++) {
            String msg = "Mismatch on output at element " + i;
            assertEquals(msg, ref[i], output[i]);
        }
    }

    /**
     * A helper function for tests of the two-task parallel implementation.
     *
     * @param N The size of the array to test
     * @return The speedup achieved, not all tests use this information
     */
    private double parTestHelper(final int N, final int nTasks) {
        // Create a random input
        double[] myNew = createArray(N);
        double[] myVal = createArray(N);

        final double[] myNewRef = createArray(N);
        final double[] myValRef = createArray(N);

        long barrierTotalTime = 0;
        long fuzzyTotalTime = 0;

        for (int r = 0; r < 3; r++) {
            final long barrierStartTime = System.currentTimeMillis();
            runParallelBarrier(nIterations, myNew, myVal, N, nTasks);
            final long barrierEndTime = System.currentTimeMillis();

            final long fuzzyStartTime = System.currentTimeMillis();
            runParallelFuzzyBarrier(nIterations, myNewRef, myValRef, N, nTasks);
            final long fuzzyEndTime = System.currentTimeMillis();

            checkResult(myNew, myNewRef);
            barrierTotalTime += (barrierEndTime - barrierStartTime);
            fuzzyTotalTime += (fuzzyEndTime - fuzzyStartTime);
        }

        return (double) barrierTotalTime / (double) fuzzyTotalTime;
    }

    /**
     * Test on large input.
     */
    public void testFuzzyBarrier() {
        final double expected = 1.05;
        final double speedup = parTestHelper(2 * 1024 * 1024, getNCores());

        final String errMsg = String.format("It was expected that the fuzzy barrier parallel implementation would " + "run %fx faster than the barrier implementation, but it only achieved %fx speedup", expected, speedup);
        assertTrue(errMsg, speedup >= expected);

        final String successMsg = String.format("Fuzzy barrier parallel implementation " + "ran %fx faster than the barrier implementation", speedup);
        System.out.println(successMsg);
    }
}