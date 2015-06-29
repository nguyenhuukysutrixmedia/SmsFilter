package com.project.smsfilter.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.res.AssetManager;

import com.project.smsfilter.model.SMSModel;

public class CsvHelper {

	public static String FILE_NAME = "SMS.txt";

	public static void copyTemplateSmsData(Context context) {
		long startTime = System.currentTimeMillis();

		AssetManager assetManager = context.getAssets();
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(FILE_NAME);
				String fileName = context.getFilesDir().getAbsoluteFile() + File.separator + FILE_NAME;
				out = new FileOutputStream(fileName);
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
				MyPreferenceUtils.setInited(context,true);
			} catch (Exception e) {
				e.printStackTrace();
				MyLog.eLog("copyTemplateSmsData error: " + e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MyLog.eLog("copyTemplateSmsData error: " + e);
		}
		MyLog.iLog(String.format(Locale.getDefault(), "Copy Template Sms Data time: %d ms", System.currentTimeMillis()
				- startTime));
	}

	public static void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	public static ArrayList<SMSModel> parseSmsAssetData(Context context) {

		long startTime = System.currentTimeMillis();

		ArrayList<SMSModel> lstSms = new ArrayList<SMSModel>();
		final String SeparateBy = "\t";
		try {
			String fileName = context.getFilesDir().getAbsoluteFile() + File.separator + FILE_NAME;
			BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
			String line;
			while (null != (line = reader.readLine())) {
				SMSModel model = new SMSModel();
				String[] arr = line.split(SeparateBy);
				try {
					model.setId(Long.parseLong(arr[0]));
				} catch (Exception e) {
				}
				try {
					model.setPhoneNumber(arr[1]);
				} catch (Exception e) {
				}
				try {
					model.setCreateTime(arr[2]);
				} catch (Exception e) {
				}
				try {
					model.setContent(arr[3]);
				} catch (Exception e) {
				}
				try {
					model.setFormatContent(arr[4]);
				} catch (Exception e) {
				}
				try {
					model.setType(arr[5]);
				} catch (Exception e) {
				}
				try {
					model.setState(arr[6]);
				} catch (Exception e) {
				}
				try {
					model.setSpam(Integer.parseInt(arr[7]) > 0);
				} catch (Exception e) {
				}
				lstSms.add(model);
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			MyLog.eLog("parseSmsAssetData error parse line: " + e);
		}

		MyLog.iLog(String.format(Locale.getDefault(), "Parse Sms Asset Data size: %d - time: %d ms", lstSms.size(),
				System.currentTimeMillis() - startTime));
		return lstSms;
	}

}
