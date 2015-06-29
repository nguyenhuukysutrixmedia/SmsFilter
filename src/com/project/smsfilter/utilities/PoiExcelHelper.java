package com.project.smsfilter.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import android.content.Context;
import android.content.res.AssetManager;

public class PoiExcelHelper {

	public static String FILE_NAME = "SMS.xls";

	public static void copyTemplateSmsData(Context context) {

		long startTime = System.currentTimeMillis();

		AssetManager assetManager = context.getAssets();
		try {
			// FileInputStream fileInputStream = new FileInputStream(assetManager.open(FILE_NAME));
			HSSFWorkbook workbook = new HSSFWorkbook(assetManager.open(FILE_NAME));
			HSSFSheet worksheet = workbook.getSheet("SMS");

			int rows = worksheet.getPhysicalNumberOfRows(); // No of rows
			int cols = worksheet.getRow(0).getPhysicalNumberOfCells();// No of columns

			HSSFRow row;
			HSSFCell cell;
			for (int r = 0; r < rows; r++) {
				row = worksheet.getRow(r);
				if (row != null) {
					// for (int c = 0; c < cols; c++) {
					// cell = row.getCell( c);
					// if (cell != null) {
					// // Your code here
					// }
					// }
					try {
						long id = Long.parseLong(row.getCell(0).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			HSSFRow row1 = worksheet.getRow(0);
			HSSFCell cellA1 = row1.getCell((short) 0);
			String a1Val = cellA1.getStringCellValue();
			HSSFCell cellB1 = row1.getCell((short) 1);
			String b1Val = cellB1.getStringCellValue();
			HSSFCell cellC1 = row1.getCell((short) 2);
			boolean c1Val = cellC1.getBooleanCellValue();
			HSSFCell cellD1 = row1.getCell((short) 3);
			Date d1Val = cellD1.getDateCellValue();

			System.out.println("A1: " + a1Val);
			System.out.println("B1: " + b1Val);
			System.out.println("C1: " + c1Val);
			System.out.println("D1: " + d1Val);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		MyLog.iLog(String.format(Locale.getDefault(), "Copy Template Sms Data time: %d ms", System.currentTimeMillis()
				- startTime));
	}
}
