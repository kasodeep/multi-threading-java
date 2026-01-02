package filecopy;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelFileCopy {

    private static final int CHUNK_SIZE = 4 * 1024 * 1024; // 4MB

    public static void copy(
            PReadWriteFile src,
            PReadWriteFile dst,
            long fileSize,
            int threads
    ) {

        try (ExecutorService pool = Executors.newFixedThreadPool(threads)) {
            long chunks = (fileSize + CHUNK_SIZE - 1) / CHUNK_SIZE;

            for (long i = 0; i < chunks; i++) {
                final long offset = i * CHUNK_SIZE;
                final int size = (int) Math.min(CHUNK_SIZE, fileSize - offset);

                pool.submit(() -> {
                    byte[] buffer = new byte[size];

                    int bytesRead;
                    try {
                        bytesRead = src.pread(buffer, size, offset);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    int bytesWritten = 0;
                    try {
                        dst.pwrite(buffer, bytesRead, offset);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
