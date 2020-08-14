package ru.w2tksoft.cp.control;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class ControlIO {
    static private byte[] inputStreamToDirectByteBufferIfHasNotArray(InputStream is, byte[] bytes,
                                                                     int capacity, int offset) throws IOException {
        for (;;) {
            byte[] newBytes;
            if (bytes != null) {
                newBytes = Arrays.copyOfRange(bytes, offset, capacity);
            } else {
                newBytes = new byte[capacity];
            }
            int cnt = is.read(bytes, 0, capacity);
            if (cnt < capacity) {
                return newBytes;
            }
            capacity *= 2;
            bytes = newBytes;
        }
    }

    static public ByteBuffer inputStreamToDirectByteBuffer(InputStream is, int capacity) throws IOException{
        ByteBuffer data = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
        byte[] prevBytes = null;
        int prevOffset = 0;
        for(;;) {
            if (data.hasArray()) {
                assert(data.hasArray());
                byte[] bytes = data.array();
                int cnt = is.read(bytes, data.arrayOffset(), capacity);
                if (cnt < capacity)
                    break;
                capacity *= 2;
                ByteBuffer newData = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
                data.flip();
                newData.put(data);
                prevBytes = bytes;
                prevOffset = data.arrayOffset();
                data = newData;
            }
        }
        data.flip();
        return data;
         /*   else
                inputStreamToDirectByteBufferIfHasNotArray(is, prevBytes, prevOffset, capacity);


            byte[] bytes = new byte[capacity];
            for(;;) {
                int cnt = is.read(bytes, 0, capacity);
                if (cnt < capacity) {

                    break;
                }
                capacity *= 2;
                bytes = Arrays.copyOf(bytes, capacity);
            }
        } catch (IOException exc) {
            throw new ControlException("Sound load: bytes read error: " + exc.getMessage(), exc);
        }*/
    }
}
