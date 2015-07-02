package com.project.smsfilter.background;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.project.smsfilter.gui.SettingActivity;
import com.project.smsfilter.updateserver.ApiRequest;
import com.project.smsfilter.utilities.MyUtils;

public class UpdateKeywordAsyncTask extends AsyncTask<Object, Integer, Integer> {

	private Activity mActivity;
	private ProgressDialog mProgressDialog;
	private ApiResponseCode mApiResponseCode;

	public UpdateKeywordAsyncTask(Activity activity, ProgressDialog progressDialog) {
		mActivity = activity;
		mProgressDialog = progressDialog;
	}

	@Override
	protected void onPreExecute() {
		if (mProgressDialog != null && !mProgressDialog.isShowing())
			mProgressDialog.show();
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(Object... params) {

		mApiResponseCode = ApiResponseCode.OK;
		// check network
		if (MyUtils.hasConnection(mActivity)) {

			// make request to api server
			String response = makeRequest(params);
			// parse response
			if (response != null && !response.isEmpty()) {
				if ("TIME_OUT".equals(response)) {
					mApiResponseCode = ApiResponseCode.TIME_OUT;
				} else {
					parseResponse(response);
				}
			} else {
				mApiResponseCode = ApiResponseCode.FAIL;
			}
		} else {
			mApiResponseCode = ApiResponseCode.NO_NETWORK;
		}

		return 0;
	}

	private void parseResponse(String response) {
		// TODO Auto-generated method stub

	}

	private String makeRequest(Object[] params) {
		return ApiRequest.getDataFromServer();
	}

	@Override
	protected void onPostExecute(Integer result) {

		if (mProgressDialog != null)
			mProgressDialog.dismiss();

		((SettingActivity) mActivity).updateDone(mApiResponseCode);

		super.onPostExecute(result);
	}

}
