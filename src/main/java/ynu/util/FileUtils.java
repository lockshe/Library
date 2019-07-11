package ynu.util;

import java.io.File;

public class FileUtils {
	
	public static final String separator=File.separator;
	
	public static boolean deleteFile(String filename) {
		String path="D:"+FileUtils.separator+"server"+FileUtils.separator+filename;
		File file =new File(path);
		return file.delete();
	}
	
	/*
	public static void main(String[] args) {
		String filename="565656.docx";
		System.out.print(deleteFile(filename));
	}
	*/

}
