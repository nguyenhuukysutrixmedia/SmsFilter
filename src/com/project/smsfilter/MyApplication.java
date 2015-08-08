package com.project.smsfilter;

import android.app.Application;

import com.project.smsfilter.BayesClassifier.Classifier;

public class MyApplication extends Application {

	public static Classifier mClassifier;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

}
