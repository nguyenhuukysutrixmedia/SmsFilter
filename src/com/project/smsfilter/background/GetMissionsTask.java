package com.project.smsfilter.background;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.smsfilter.MyApplication;
import com.project.smsfilter.utilities.MyConstants;
import com.project.smsfilter.utilities.MyLog;
import com.project.smsfilter.utilities.MyUtils;

public class GetMissionsTask extends AsyncTask<Object, Integer, Integer> {

	private ProgressDialog mProgressDialog;
	private Activity mActivity;

	public GetMissionsTask(Activity activity, ProgressDialog progressDialog) {
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

		int mApiHandleCode = 0;

		// check network
		if (MyUtils.hasConnection(mActivity)) {

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

	@Override
	protected String makeRequest(Object... params) {

		String response = SfmApiRequest.getMissions(MyApplication.getApiToken());
		return response;
	}

	@Override
	protected void parseResponse(String response) {
		try {

			JSONObject jsonObject = new JSONObject(response);
			Boolean isValid = jsonObject.getBoolean(MyConstants.ApiJsonTag.STATUS);
			if (isValid) {

				MissionTableHelper missionTableHelper = new MissionTableHelper(mContext);
				missionTableHelper.deleteAll();

				String missionData = jsonObject.getString(MISSION_DATA);
				Gson gson = new Gson();
				List<MissionModel> listMissions = gson.fromJson(missionData, new TypeToken<List<MissionModel>>() {
				}.getType());

				for (MissionModel missionModel : listMissions) {

					checkEmptyTimeString(missionModel);

					// missionModel.changeToLocalTimeZone();
					missionTableHelper.insert(missionModel);

					// alarm mission
					MyAlarmManagerByTimer.alarm(missionModel);
					// MyAlarmManager.alarm(mContext, missionModel);
				}
				mApiHandleCode = OK;
			} else {
				mMessage = jsonObject.getString(MyConstants.ApiJsonTag.MESSAGE);
				mApiHandleCode = API_RESPONSE_ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			MyLog.eGeneral("ParseJson-GetMissionsTask: " + e);
			mApiHandleCode = PARSE_JSON_ERROR;
			MyLog.logToFile(e, response, "");
		}
	}

	private void checkEmptyTimeString(MissionModel missionModel) {

		if (EMPTY_TIME.equals(missionModel.getStartFin()))
			missionModel.setStartFin("");

		if (EMPTY_TIME.equals(missionModel.getStartTime()))
			missionModel.setStartTime("");

		if (EMPTY_TIME.equals(missionModel.getEndFin()))
			missionModel.setEndFin("");

		if (EMPTY_TIME.equals(missionModel.getEndTime()))
			missionModel.setEndTime("");

		if (EMPTY_TIME.equals(missionModel.getCreatedTime()))
			missionModel.setCreatedTime("");
	}

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private void initTempData() {

		MissionTableHelper missionTableHelper = new MissionTableHelper(mContext);
		missionTableHelper.deleteAll();
		for (int i = 0; i < 5; i++) {
			MissionModel missionModel = new MissionModel();
			missionModel.setMissionId("" + (i + 1));
			missionModel.setMissionName("Mission temp " + (i + 1));
			missionModel.setCreatedTime(MyDateHelper.getCurrentLocalDate());

			missionTableHelper.insert(missionModel);
		}
	}
}
