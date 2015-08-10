package com.project.smsfilter.model;

import java.io.Serializable;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.project.smsfilter.sms.Defines.SmsType;
import com.project.smsfilter.sms.MySMSUtils;
import com.project.smsfilter.utilities.MyConstants;

public class SmsTestModel implements Serializable, SmsType {

	private static final long serialVersionUID = 1L;

	private int uid;
	private long id;
	private long threadId;
	private String phoneNumber;
	private String phoneName;
	private long createTime;
	private String content;
	private int type;
	private String state;
	private Boolean isSpam;
	private String formatContent;
	private boolean isReviewed;

	public SmsTestModel() {
		uid = -1;
		id = -1;
		phoneNumber = "";
		phoneName = "";
		createTime = 0;
		content = "";
		type = MESSAGE_TYPE_ALL;
		state = "";
		isSpam = false;
		formatContent = "";
		isReviewed = false;
	}

	@Override
	public String toString() {
		return String
				.format(Locale.US,
						"uid: %s - id: %d - thread_id: %d - phoneNumber: %s - phoneName: %s - createTime: %s - content: %s - type: %s - state: %s - isSpam: %s - formatContent: %s ",
						uid, id, threadId, phoneNumber, phoneName, getCreateTimeString(MyConstants.DATE_FORMAT),
						content, type, state, isSpam, formatContent);
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

	public String getPhoneName() {
		return phoneName;
	}

	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public void setCreateTime(String createTimeString, String templateFormat) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(templateFormat, Locale.getDefault());
			createTime = sdf.parse(createTimeString).getTime();
		} catch (Exception e) {
			e.printStackTrace();
			createTime = 0;
		}
	}

	public String getCreateTimeString(String templateFormat) {
		Date date = new Date(createTime);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(templateFormat, Locale.getDefault());
			return sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getLowerCaseContent() {
		return content.toLowerCase(Locale.getDefault());
	}

	public String getContent() {
		return content;
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
		if (!MySMSUtils.isInbox(type)) {
			isReviewed = true;
		}
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

	public boolean isReviewed() {
		return isReviewed;
	}

	public void setReviewed(boolean userReviewed) {
		this.isReviewed = userReviewed;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

}
