package filecopy;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

@SuppressWarnings("resource")
public class FileChannelPReadWrite implements PReadWriteFile {

    private final FileChannel channel;

    public FileChannelPReadWrite(File file, boolean write) throws IOException {
        if (write) {
            this.channel = new RandomAccessFile(file, "rw").getChannel();
        } else {
            this.channel = new RandomAccessFile(file, "r").getChannel();
        }
    }

    @Override
    public int pread(byte[] buffer, int size, long offset) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, size);
        return channel.read(byteBuffer, offset);
    }

    @Override
    public int pwrite(byte[] buffer, int size, long offset) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, size);
        return channel.write(byteBuffer, offset);
    }

    public void close() throws IOException {
        channel.close();
    }
}

