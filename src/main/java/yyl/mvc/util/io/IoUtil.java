package yyl.mvc.util.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

public class IoUtil {

	private static final int DEFAULT_BUFFER_SIZE = 0x10000; //65536
	
	 /**
     * 将文本写入到输出流中
     * @param text 文本
     * @param output 输出流中
     * @param encoding 字符编码
     * @return 写入的字节数
     */
    public static int write(String text, OutputStream output, Charset encoding) throws IOException {
        int length = 0;
        if (text != null) {
            byte[] data = text.getBytes(encoding);
            length = data.length;
            output.write(data);
        }
        return length;
    }

	public static void copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
	}

	public static void copy(Reader input, Writer output) throws IOException {
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
	}

	public static void copy(InputStream input, Writer output, String encoding) throws IOException {
		copy(new InputStreamReader(input, Charset.forName(encoding)), output);
	}

	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}
}
