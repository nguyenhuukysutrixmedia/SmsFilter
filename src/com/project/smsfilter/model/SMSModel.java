package com.project.smsfilter.model;

import java.io.Serializable;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.project.smsfilter.sms.Defines.SmsType;

public class SMSModel implements Serializable, SmsType{

	private static final long serialVersionUID = 1L;

	public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
	public static final String DATE_FORMAT_DATABASE = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_SHORT_FORMAT = "yyyy-MMM-dd";

	private int uid;
	private long id;
	private String phoneNumber;
	private Date createTime;
	private String content;
	private int type;
	private String state;
	private Boolean isSpam;
	private String formatContent;

	public SMSModel() {
		uid = -1;
		id = -1;
		phoneNumber = "";
		createTime = new Date();
		content = "";
		type = MESSAGE_TYPE_ALL;
		state = "";
		isSpam = false;
		formatContent = "";
	}

	public SMSModel(SmsTestModel smsTestModel) {
		uid = -1;
		id = smsTestModel.getId();
		phoneNumber = smsTestModel.getPhoneNumber();
		content = smsTestModel.getContent();
		type = smsTestModel.getType();
		state = smsTestModel.getState();
		isSpam = smsTestModel.isSpam();
		formatContent = smsTestModel.getFormatContent();
	}

	@Override
	public String toString() {
		return String
				.format("uid: %s - phoneNumber: %s - createTime: %s - content: %s - type: %s - state: %s - isSpam: %s - formatContent: %s ",
						uid, phoneNumber, getCreateTimeString(), content, type, state, isSpam, formatContent);
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int id) {
		this.uid = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		if (phoneNumber == null)
			phoneNumber = "";
		this.phoneNumber = phoneNumber;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateTimeString() {
		String createTimeString = "";
		if (createTime != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATABASE, Locale.getDefault());
			createTimeString = sdf.format(createTime);
		}
		return createTimeString;
	}

	public String getCreateTimeShortString() {
		String createTimeString = "";
		if (createTime != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_SHORT_FORMAT, Locale.getDefault());
			createTimeString = sdf.format(createTime);
		}
		return createTimeString;
	}

	public void setCreateTime(String createTimeString) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
		try {
			this.createTime = sdf.parse(createTimeString);
		} catch (Exception e) {
			// this.createTime = new Date();
			this.createTime = null;
			// e.printStackTrace();
			// MyLog.eLog(String.format("Parse create time - %s - error: %s", createTimeString, e.toString()));
		}
	}

	public void setCreateTimeDatabase(String createTimeString) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATABASE, Locale.getDefault());
		try {
			this.createTime = sdf.parse(createTimeString);
		} catch (Exception e) {
			// this.createTime = new Date();
			this.createTime = null;
			// e.printStackTrace();
			// MyLog.eLog(String.format("Parse create time - %s - error: %s", createTimeString, e.toString()));
		}
	}

	public String getContent() {
		return content.toLowerCase(Locale.getDefault());
	}

	public void setContent(String content) {
		if (content == null)
			content = "";
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		if (state == null)
			state = "";
		this.state = state;
	}

	public Boolean isSpam() {
		return isSpam;
	}

	public void setSpam(Boolean isSpam) {
		this.isSpam = isSpam;
	}

	public String getFormatContent() {
		if (content == null)
			return "";
		formatContent = Normalizer.normalize(content, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
				.toLowerCase(Locale.getDefault());
		return formatContent;
	}

	public void setFormatContent(String formatContent) {
		if (formatContent == null)
			formatContent = "";
		this.formatContent = formatContent;
	}

}
