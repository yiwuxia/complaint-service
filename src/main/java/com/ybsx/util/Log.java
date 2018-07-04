package com.jsm.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 * Title: LOG 日志记录
 * </p>
 * <p>
 * Description: 此类主要用来记录系统中发生的重大事件，以及由于程序本身所产生的错误信息
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author lqf
 * @version 1.0
 */
// //////////////////////////////////////////////////////////////////////////////////////////
//
// /////////////////////////////////////////////////////////////////////////////////////////
public class Log {

	/**
	 * 用来记录系统重大事件
	 * 
	 * @param msg
	 *            　重大事件
	 * @param fileName
	 *            　日志文件的路径及名称
	 */
	public synchronized static void printEvent(String msg, String fileName) {
		msg = new String("时间：" + Time.getTime(Time.YYMMDDhhmmss) + " 事件消息:  " + msg);
		if (fileName != null)
			printToFile(msg, fileName);
		else
			print(msg);
		return;
	}

	/**
	 * 记录应程序本身发生的错误，主要给程序员观察。
	 * 
	 * @param e
	 *            　　一个Ｅｘｃｅｐｔｉｏｎ
	 * @param mobile
	 *            　用户手机号码
	 * @param msg
	 *            　　　用户发送的消息
	 * @param fileName
	 *            　日志文件的路径及名称
	 */
	public synchronized static void printError(Throwable e, String mobile, String msg, String fileName) {
		StringBuffer errors = new StringBuffer(100);
		errors.append("时间：");
		errors.append(Time.getTime(Time.YYMMDDhhmmssxxx));
		errors.append("　手机号码:");
		errors.append(mobile);
		errors.append("　消息：");
		errors.append(msg);
		errors.append(" Exception: ");
		if (fileName != null) {
			printToFile(errors.toString().trim(), fileName);
			try {
				e.printStackTrace(new PrintWriter(new FileWriter(fileName, true), true));//
			}
			catch (Exception ex) {
			}
		} else
			print(errors.toString().trim());
		return;
	}

	public synchronized static void writeFile(String msg, String fileName) {
		if (fileName != null)
			printToFile(msg, fileName, false);
		else
			print(msg);
		return;
	}

	/**
	 * 把错误消息打印到屏幕上
	 * 
	 * @param msg
	 *            　错误消息
	 */
	private static void print(String msg) {
		System.out.println(msg);
	}

	/**
	 * 把消息打印到指定文件
	 * 
	 * @param msg
	 *            　错误消息
	 * @param fileName
	 *            　指定的文件
	 */

	private static void printToFile(String msg, String fileName) // 打印到文件中
	{
		BufferedWriter mBufWriter = null;
		try {
			if (!createFile(fileName))
				return;
			FileWriter fileWriter = new FileWriter(fileName, true);
			mBufWriter = new BufferedWriter(fileWriter);

			mBufWriter.write(msg);
			mBufWriter.newLine();

			mBufWriter.flush();
			mBufWriter.close();
		}
		catch (Throwable e) {
			try {
				mBufWriter.close();
			}
			catch (Throwable t) {
			}
			;
		}
		return;
	}

	/**
	 * 用来创建文件和文件夹
	 * 
	 * @param fileName
	 *            　文件或文件夹名称
	 * @return
	 * @throws IOException
	 *             　
	 * @throws Exception
	 */

	private static boolean createFile(String fileName) throws IOException, Exception {
		File file = new File(fileName);
		if (file.exists()) /* does file exist? If so, can it be written to */
		{
			if (file.canWrite() == false)
				return false;
		} else {
			String path = null; /* Does not exist. Create the directories */

			int firstSlash = fileName.indexOf(File.separatorChar);
			int finalSlash = fileName.lastIndexOf(File.separatorChar);

			if (finalSlash == 0) { /* error, not valid path */
			} else if (finalSlash == 1) /* UNIX root dir */
			{
				path = File.separator;
			} else if (firstSlash == finalSlash) { /*
													 * for example c:\ Then make
													 * sure slash is part of
													 * path
													 */
				path = fileName.substring(0, finalSlash + 1);
			} else {
				path = fileName.substring(0, finalSlash);
			}

			File dir = new File(path);
			dir.mkdirs();
		}
		return true;
	}

	private static void printToFile(String msg, String fileName, boolean flag) // 打印到文件中
	{
		BufferedWriter mBufWriter = null;
		try {
			if (!createFile(fileName))
				return;
			FileWriter fileWriter = new FileWriter(fileName, flag);
			mBufWriter = new BufferedWriter(fileWriter);

			mBufWriter.write(msg);
			mBufWriter.newLine();

			mBufWriter.flush();
			mBufWriter.close();
		}
		catch (Throwable e) {
			try {
				mBufWriter.close();
			}
			catch (Throwable t) {
			}
			;
		}
		return;
	}

}
