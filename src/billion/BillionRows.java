package billion;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.round;
import static java.nio.channels.FileChannel.MapMode.READ_ONLY;
import static java.nio.file.StandardOpenOption.READ;

public class BillionRows {

    // Path to the file where data is stored.
    private static final String FILE_NAME = "C:\\Users\\admin\\Desktop\\stations.txt";

    public static void main(String[] args) throws IOException {
        long[] segments = getSegments(Runtime.getRuntime().availableProcessors());

        // For the purpose of measuring time.
        long start = System.nanoTime();

        var result = IntStream.range(0, segments.length - 1)
                .parallel()
                .mapToObj(i -> processSegment(segments[i], segments[i + 1]))
                .flatMap(m -> Arrays.stream(m.hashTable).filter(Objects::nonNull))
                .collect(Collectors.toMap(m -> new String(m.city), m -> m, Measurement::merge, TreeMap::new));

        long end = System.nanoTime();

        System.out.println(result);
        System.out.printf("\nTotal Execution Time: %.4f seconds%n", (end - start) / 1_000_000_000.0);
    }

    /**
     * The function returns custom implementation of LinearProbingHashMap.
     * It takes the file chunk and process it and store the results.
     * */
    private static LinearProbingHashMap processSegment(long start, long end) {
        LinearProbingHashMap results = new LinearProbingHashMap(1 << 19);

        /*
         * fileChannel allows us to map the files chunk into memory for direct access.
         * */
        try (var fileChannel = (FileChannel) Files.newByteChannel(Path.of(FILE_NAME), READ)) {
            MappedByteBuffer bb = fileChannel.map(READ_ONLY, start, end - start);
            byte[] buffer = new byte[100];

            while (bb.hasRemaining()) {
                int startLine = bb.position();
                if (startLine >= bb.limit()) break;

                int currentPosition = startLine;
                byte b;
                int hash = 7;
                int wordLen = 0;

                // Read the city name until ';'
                while (currentPosition < bb.limit() && (b = bb.get(currentPosition++)) != ';') {
                    buffer[wordLen++] = b;
                    hash = hash * 31 + b;
                }

                if (currentPosition >= bb.limit()) break;

                // Parse the temperature value.
                int temp;
                int negative = 1;
                if (bb.get(currentPosition) == '-') {
                    negative = -1;
                    currentPosition++;
                }

                if (currentPosition + 2 < bb.limit() && bb.get(currentPosition + 1) == '.') {
                    temp = negative * ((bb.get(currentPosition) - '0') * 10 + (bb.get(currentPosition + 2) - '0'));
                    currentPosition += 3;
                } else if (currentPosition + 3 < bb.limit()) {
                    temp = negative * ((bb.get(currentPosition) - '0') * 100 +
                            ((bb.get(currentPosition + 1) - '0') * 10 + (bb.get(currentPosition + 3) - '0')));
                    currentPosition += 4;
                } else {
                    break; // Malformed data; avoid buffer overflow.
                }

                if (currentPosition >= bb.limit()) break;

                currentPosition++;
                results.put(hash, buffer, wordLen, temp);
                bb.position(currentPosition);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    /**
     * GetSegments divides the files into different chunks.
     * It uses RandomAccessFile in reading mode, to get the file size and move the pointers.
     *
     * @return - It returns the chunks arrays, which contains the starting pointers in file for different chunks.
     */
    private static long[] getSegments(int segmentCount) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "r")) {
            long fileSize = raf.length();
            if (fileSize < 100000) {
                return new long[]{0, fileSize};
            }

            // Ensure no segment exceeds buffer limits.
            while (fileSize / segmentCount >= (Integer.MAX_VALUE - 150)) {
                segmentCount *= 2;
            }

            long[] chunks = new long[segmentCount + 1];
            chunks[0] = 0;
            long segmentSize = fileSize / segmentCount;

            for (int i = 1; i < segmentCount; i++) {
                long chunkOffset = chunks[i - 1] + segmentSize;
                raf.seek(chunkOffset);

                // Align to the next line boundary
                while (raf.readByte() != '\n') {
                    if (raf.getFilePointer() >= fileSize) break;
                }
                chunks[i] = raf.getFilePointer();
            }
            chunks[segmentCount] = fileSize;
            return chunks;
        }
    }

    public static class LinearProbingHashMap {
        final Measurement[] hashTable;
        int slots;

        public LinearProbingHashMap(int slots) {
            this.slots = slots;
            this.hashTable = new Measurement[slots];
        }

        void put(int hash, byte[] key, int len, int temperature) {
            hash = Math.abs(hash);

            int i = hash & (slots - 1);
            while (hashTable[i] != null) {
                if (keyIsEqual(key, hashTable[i].city, len)) { // Handling hash collisions.
                    hashTable[i].add(temperature);
                    return;
                }
                i++;
                if (i == slots) {
                    i = 0;
                }
            }

            var cityArr = new byte[len];
            System.arraycopy(key, 0, cityArr, 0, len);
            hashTable[i] = new Measurement(cityArr, hash, temperature, temperature, 1, temperature);
        }

        private boolean keyIsEqual(byte[] one, byte[] other, int len) {
            if (len != other.length)
                return false;
            for (int i = 0; i < len; i++) {
                if (one[i] != other[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * The Measurement class is used to represent the cities and their statistics.
     */
    static class Measurement {
        byte[] city;
        int hash;
        int min;
        int max;
        int count;
        long sum;

        public Measurement(byte[] city, int hash, int min, int max, int count, long sum) {
            this.city = city;
            this.hash = hash;
            this.min = min;
            this.max = max;
            this.count = count;
            this.sum = sum;
        }

        public void add(int temperature) {
            min = Math.min(min, temperature);
            max = Math.max(max, temperature);
            count++;
            sum += temperature;
        }

        public Measurement merge(Measurement other) {
            min = Math.min(min, other.min);
            max = Math.max(max, other.max);
            count += other.count;
            sum += other.sum;
            return this;
        }

        @Override
        public String toString() {
            return (min * 1.0) / 10 + "/" + round((sum * 1.0) / count) / 10.0 + "/" + (max * 1.0) / 10;
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            return Arrays.equals(city, ((Measurement) obj).city);
        }
    }
}

