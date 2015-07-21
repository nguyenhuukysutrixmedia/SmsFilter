package com.project.smsfilter.gui;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.smsfilter.R;
import com.project.smsfilter.utilities.MyToast;
import com.project.smsfilter.utilities.MyUtils;

public class NewSmsActivity extends Activity implements OnClickListener {

	private static final int REQUEST_CONTACT_NUMBER = 101;

	private EditText edtEditor, edtPhoneNumber;
	private TextView tvTextNumber;
	private Button btnSend;
	private MyToast myToast;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_sms);
		mContext = this;

		initView();
		initSendToContact();
	}

	private void initView() {

		//
		myToast = new MyToast(this);

		//
		btnSend = (Button) findViewById(R.id.btn_send);
		btnSend.setOnClickListener(this);
		findViewById(R.id.btn_contact).setOnClickListener(this);

		//
		edtPhoneNumber = (EditText) findViewById(R.id.edt_to_number);

		//
		tvTextNumber = (TextView) findViewById(R.id.tv_new_sms_activity_text_number);

		//
		edtEditor = (EditText) findViewById(R.id.edt_editor);
		edtEditor.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tvTextNumber.setText(edtEditor.getText().toString().length() + "");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}

	/**
	 * 
	 */
	private void initSendToContact() {
		try {
			// In the activity
			Intent intent = getIntent();
			if (intent != null && intent.getData() != null) {
				String phoneNumber = intent.getData().getSchemeSpecificPart();// The result 999 9999 9999
				edtPhoneNumber.setText(phoneNumber);
				edtEditor.requestFocus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_send:
			if (validate()) {
				sendMessage();
			}
			break;

		case R.id.btn_contact:
			Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Phone.CONTENT_URI);
			startActivityForResult(contactPickerIntent, REQUEST_CONTACT_NUMBER);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @return
	 */
	private boolean validate() {

		if (MyUtils.validatePhoneNumber(edtPhoneNumber.getText().toString().trim())) {
			if (edtEditor.getText().toString().isEmpty()) {
				myToast.showToast(getString(R.string.message_content_empty));
				edtEditor.requestFocus();
				return false;
			}
			return true;
		} else {
			myToast.showToast(getString(R.string.phone_number_wrong_format));
			edtPhoneNumber.requestFocus();
			return false;
		}
	}

	/**
	 * 
	 */
	private void sendMessage() {
		MyUtils.requestKeyBoard(mContext, edtEditor, false);
		
		// try {
		// SmsManager smsManager = SmsManager.getDefault();
		// smsManager.sendTextMessage(edtToNumber.getText().toString().trim(), null, edtEditor.getText().toString(),
		// null, null);
		// myToast.showToast(getString(R.string.message_sent));
		// } catch (Exception e) {
		// myToast.showToast(getString(R.string.message_sent_fail));
		// }

		String phoneNo = edtPhoneNumber.getText().toString().trim();
		String msg = edtEditor.getText().toString();

		try {

			String SENT = "sent";
			String DELIVERED = "delivered";

			Intent sentIntent = new Intent(SENT);
			/* Create Pending Intents */
			PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, sentIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			Intent deliveryIntent = new Intent(DELIVERED);

			PendingIntent deliverPI = PendingIntent.getBroadcast(getApplicationContext(), 0, deliveryIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			/* Register for SMS send action */
			registerReceiver(new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					String result = "";

					switch (getResultCode()) {

					case Activity.RESULT_OK:
						result = "Transmission successful";
						finish();
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						result = "Transmission failed";
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						result = "Radio off";
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						result = "No PDU defined";
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						result = "No service";
						break;
					}

					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
				}

			}, new IntentFilter(SENT));
			/* Register for Delivery event */
			registerReceiver(new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					Toast.makeText(getApplicationContext(), "Deliverd", Toast.LENGTH_LONG).show();
				}

			}, new IntentFilter(DELIVERED));

			/* Send SMS */
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNo, null, msg, sentPI, deliverPI);
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
			ex.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (data != null && requestCode == REQUEST_CONTACT_NUMBER) {
				Uri uriOfPhoneNumberRecord = data.getData();
				String idOfPhoneRecord = uriOfPhoneNumberRecord.getLastPathSegment();
				Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, new String[] { Phone.NUMBER },
						Phone._ID + "=?", new String[] { idOfPhoneRecord }, null);
				if (cursor != null) {
					if (cursor.getCount() > 0) {
						cursor.moveToFirst();
						String formattedPhoneNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
						edtPhoneNumber.setText(formattedPhoneNumber);
						edtEditor.requestFocus();
						Log.d("TestActivity", String.format("The selected phone number is: %s", formattedPhoneNumber));
					}
					cursor.close();
				}
			} else {
				Log.w("TestActivity", "WARNING: Corrupted request response");
			}
		} else if (resultCode == RESULT_CANCELED) {
			Log.i("TestActivity", "Popup canceled by user.");
		} else {
			Log.w("TestActivity", "WARNING: Unknown resultCode");
		}
	}
}
