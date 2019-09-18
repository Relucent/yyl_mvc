package yyl.mvc.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 */
public class FileUtil {

    // ===================================Fields==============================================
    /** 文件分隔符 */
    public static final String FILE_SEPARATOR = "/";
    /** 1KB */
    public static final long ONE_KB = 1024L;
    /** 1MB */
    public static final long ONE_MB = ONE_KB * ONE_KB;
    /** 1GB */
    public static final long ONE_GB = ONE_KB * ONE_MB;

    // ===================================Methods=============================================

	/**
	 * 获得文件相对路径
	 * @param root 根目录
	 * @param path 文件路径
	 * @return 拆分后的相对路径
	 */
	public static String relative(String root, String path) {
		root = separatorsToUnix(root);
		path = separatorsToUnix(path);
		if (path.indexOf(root) == 0) {
			return new String(path.substring(root.length()));
		}
		return path;
	}

	/**
	 * 删除文件(如果是文件夹，将会递归删除)
	 * @param file 文件
	 */
	public static void delete(File file) {
		if (file != null && file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					delete(files[i]);
				}
			}
			file.delete();
		}
	}

	/**
	 * 规范化文件路径[文件分割符使用正斜杠(/)]
	 * @param path 规范化文件路径
	 * @return 符合规范的文件路径
	 */
	public static String separatorsToUnix(String path) {
		if ((path == null) || (path.indexOf('\\') == -1)) {
			return path;
		}
		return path.replace('\\', '/');
	}

	/**
	 * 打开文件流
	 * @param file 文件
	 * @return 文件流
	 */
	public static FileInputStream openInputStream(File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if (file.canRead() == false) {
				throw new IOException("File '" + file + "' cannot be read");
			}
		} else {
			throw new FileNotFoundException("File '" + file + "' does not exist");
		}
		return new FileInputStream(file);
	}

	/**
	 * 开放文件权限读写权限(rw/rw/rw)
	 * @param file 文件
	 */
	public static void chmod666(File file) {
		if (file.exists()) {
			file.setReadable(true, false);// 读
			file.setWritable(true, false);// 写
			// file.setExecutable(true, false);//运行
		}
	}

	/**
	 * 将文件压缩并写入流中
	 * @param file 文件
	 * @param output 输出流
	 */
	public static void writeToZipOutputStream(File file, OutputStream output) throws Exception {
		ZipOutputStream out = new ZipOutputStream(output);
		writeToZipOutputStream(out, file, "");
		out.flush();
	}

	/**
	 * 将文件压缩并写入流中
	 * @param file 文件
	 * @param output 输出流
	 * @param path ZIP层级目录
	 */
	private static void writeToZipOutputStream(ZipOutputStream output, File file, String path) throws Exception {
		if (file.isDirectory()) {
			output.putNextEntry(new ZipEntry(path + "/"));
			path = path.isEmpty() ? "" : path + "/";
			for (File f : file.listFiles()) {
				writeToZipOutputStream(output, f, path + f.getName());
			}
		} else if (file.isFile()) {
			output.putNextEntry(new ZipEntry(path));
			InputStream input = null;
			try {
				input = openInputStream(file);
				IoUtil.copy(input, output);
			} finally {
				IoUtil.closeQuietly(input);
			}
		}

	}
}
