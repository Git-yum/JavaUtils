package com.fileio;

import java.io.File;

/**
 * 	删除指定文件,包含子文件
 * @author Gze
 *
 */
public class Deletes {
	public static void main(String[] args) {
		File file = new File("down");
		deletes(file);
	}
	
	/**
	 * 	删除指定文件及其子文件
	 * 	如果传入文件不存在也不会报错
	 * @param file	传入的文件
	 */
	public static void deletes(File file) {
		//文件存在先执行删除空目录和子文件,再判断
		//删除成功返回true,删除失败返回false
		if(file.exists() && !file.delete()) {
			//遍历所有子文件
			for(File f:file.listFiles()) {
				//递归删除子文件
				deletes(f);
			}
			//删除当前目录
			file.delete();
		}
	}
}
