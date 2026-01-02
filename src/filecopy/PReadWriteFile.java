package filecopy;

import java.io.IOException;

public interface PReadWriteFile {
    int pread(byte[] buffer, int size, long offset) throws IOException;
    int pwrite(byte[] buffer, int size, long offset) throws IOException;
}

