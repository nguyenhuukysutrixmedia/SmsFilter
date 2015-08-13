package com.project.smsfilter.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import android.content.Context;
import android.content.res.AssetManager;

import com.project.smsfilter.model.SMSModel;

public class ReadExcelHelper {

	public static String FILE_NAME = "SMS.xls";

	public static ArrayList<SMSModel> parseSmsAssetData(Context context) {

		ArrayList<SMSModel> lstSms = new ArrayList<SMSModel>();
		long startTime = System.currentTimeMillis();

		AssetManager assetManager = context.getAssets();
		HSSFWorkbook workbook = null;
		try {
			// FileInputStream fileInputStream = new FileInputStream(assetManager.open(FILE_NAME));
			workbook = new HSSFWorkbook(assetManager.open(FILE_NAME));
			HSSFSheet worksheet = workbook.getSheetAt(0);

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = worksheet.iterator();
			// for title
			if (rowIterator.hasNext())
				rowIterator.next();
			//
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				try {
					
					SMSModel model = new SMSModel();
					Cell cellID = row.getCell(0);
					Cell cellContent = row.getCell(1);
					Cell cellSpam = row.getCell(2);
					// model.setId((long) cellID.getNumericCellValue());
					model.setContent(cellContent.getStringCellValue());
					model.setSpam(cellSpam.getBooleanCellValue());
					lstSms.add(model);
				} catch (Exception e) {
					e.printStackTrace();
					MyLog.eLog("Read excell row error: " + e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		MyLog.iLog(String.format(Locale.getDefault(), "parseSmsAssetData time: %d ms", System.currentTimeMillis()
				- startTime));
		return lstSms;
	}
}
