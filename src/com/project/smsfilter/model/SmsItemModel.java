package com.project.smsfilter.model;

import java.io.Serializable;

public class SmsItemModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private SmsTestModel smsModel;
	private boolean isChecked;
	
	public SmsItemModel(){
		smsModel = new SmsTestModel();
		isChecked = false;
	}

	public SmsTestModel getSmsModel() {
		return smsModel;
	}
	public void setSmsModel(SmsTestModel smsModel) {
		this.smsModel = smsModel;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isSelected) {
		this.isChecked = isSelected;
	}

}
