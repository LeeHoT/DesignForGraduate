package com.qingcity.base.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 
 * @author leehotin
 * @Date 2017年2月6日 下午6:00:48
 * @Description Excel工具类，没有硬编码，修改时不要添加硬编码代码
 */
public class FileUtil {

	private static final String JSON_PATH = "src/main/resources/config/properties/json/";
	private static final String EXCEL_PATH = "src/main/resources/config/properties/excel/";

	private static final int BUFFER_SIZE = 1024;

	public static String getJsonPath() {
		return JSON_PATH;
	}

	public static String getExcelPath() {
		return EXCEL_PATH;
	}

	private FileUtil() {
	};

	/**
	 * 读取文件内容，，主要用于读取json文件
	 * 
	 * @param Path
	 *            文件路径
	 * @return
	 */
	public static String ReadFile(String path) {
		BufferedReader reader = null;
		String laststr = "";
		try (FileInputStream fileInputStream = new FileInputStream(path);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8")) {
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}

	/**
	 * 获取excel 单元格中的内容
	 * 
	 * @param cell
	 *            单元格对象
	 * @return 返回当前单元格里的内容。并转换成字符串的形式
	 */
	private static String getCellValue(Cell cell) {
		String value = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				if (HSSFDateUtil.isCellDateFormatted(cell)) { // 日期类型
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
					value = sdf.format(date);
				} else {
					Integer data = (int) cell.getNumericCellValue();
					value = data.toString();
				}
				break;
			case Cell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				Boolean data = cell.getBooleanCellValue();
				value = data.toString();
				break;
			case Cell.CELL_TYPE_ERROR:
				System.out.println("单元格内容出现错误");
				break;
			case Cell.CELL_TYPE_FORMULA:
				value = String.valueOf(cell.getNumericCellValue());
				if (value.equals("NaN")) {// 如果获取的数据值非法,就将其装换为对应的字符串
					value = cell.getStringCellValue().toString();
				}
				break;
			case Cell.CELL_TYPE_BLANK:
				System.out.println("单元格内容 为空值 ");
				break;
			default:
				value = cell.getStringCellValue().toString();
				break;
			}
		}
		return value;
	}

	/**
	 * 将json 字符串写入文件 写入是未换行。所有数据均在一行。手动阅读不方便
	 * 
	 * @param str
	 *            json 字符串
	 * @param fileName
	 *            新建文件的文件名，含有后缀
	 * @throws IOException
	 */
	public static void string2JsonFile(String str, String fileName) throws IOException {
		String path = JSON_PATH + fileName;
		try (OutputStream os = new FileOutputStream(path);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
			bw.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
	}

	/**
	 * 将inputStream 读出byte数组中
	 * 
	 * @param inStream
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[BUFFER_SIZE];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	public static final String readInputStream2String(InputStream in) throws UnsupportedEncodingException, IOException {
		if (in == null)
			return "";
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[BUFFER_SIZE];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n, "UTF-8"));
		}
		return out.toString();
	}

}