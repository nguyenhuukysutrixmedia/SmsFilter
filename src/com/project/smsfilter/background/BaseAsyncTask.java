package com.project.smsfilter.background;

import perlmine.sfm.controllerspdc.eventhandle.OnApiRequestListener;
import perlmine.sfm.controllerspdc.gui.custom.MyProgressDialog;
import perlmine.sfm.controllerspdc.utils.MyConstants.ApiHandleCode;
import perlmine.sfm.controllerspdc.utils.MyConstants.ApiJsonTag;
import perlmine.sfm.controllerspdc.utils.MyConstants.ApiResponseCode;
import perlmine.sfm.controllerspdc.utils.MyUtils;
import android.content.Context;
import android.os.AsyncTask;

public abstract class BaseAsyncTask extends AsyncTask<Object, Integer, Integer>
		implements
			ApiHandleCode,
			ApiResponseCode,
			ApiJsonTag {

	protected Context mContext;
	protected OnApiRequestListener mOnApiRequestListener;
	protected MyProgressDialog mProgressDialog;
	protected boolean isCancelProgressAble;

	//
	protected int mApiResponseCode;
	protected int mApiHandleCode;
	protected String mMessage;

	public BaseAsyncTask(Context context, MyProgressDialog progressDialog, boolean cancelProgressAble,
			OnApiRequestListener onApiRequestListener) {
		mContext = context;
		mProgressDialog = progressDialog;
		mOnApiRequestListener = onApiRequestListener;
		this.isCancelProgressAble = cancelProgressAble;
	}
	@Override
	protected void onPreExecute() {
		if (mProgressDialog != null && !mProgressDialog.isShowing())
			mProgressDialog.show();
		super.onPreExecute();
	}

	abstract protected String makeRequest(Object... params);
	abstract protected void parseResponse(String response);

	@Override
	protected Integer doInBackground(Object... params) {

		mApiHandleCode = OK;
		// check network
		if (MyUtils.hasConnection(mContext)) {

			// make request to api server
			String response = makeRequest(params);
			// parse response
			if (response != null && !response.isEmpty()) {
				parseResponse(response);
			} else {
				mApiHandleCode = NULL_OR_EMPTY_RESPONSE_ERROR;
			}
		} else {
			mApiHandleCode = NO_NETWORK;
		}
		return mApiHandleCode;
	}

	@Override
	protected void onPostExecute(Integer result) {

		if (mProgressDialog != null && (isCancelProgressAble || result != OK))
			mProgressDialog.dismiss();

		if (mOnApiRequestListener != null) {
			// send back to GUI through handle
			mOnApiRequestListener.onDone(mApiHandleCode, mApiResponseCode, mMessage);
		}

		super.onPostExecute(result);
	}

	public void setCancelProgressAble(boolean cancelProgressAble) {
		this.isCancelProgressAble = cancelProgressAble;
	}

	public void setOnApiRequestListener(OnApiRequestListener mOnListenApiRequest) {
		this.mOnApiRequestListener = mOnListenApiRequest;
	}

}
