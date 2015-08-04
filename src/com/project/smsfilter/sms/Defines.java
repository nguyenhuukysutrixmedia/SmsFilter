package com.project.smsfilter.sms;

import android.net.Uri;

public class Defines {

	public static interface SmsUri {
		
		public static final Uri INBOX = Uri.parse("content://sms/inbox");
		public static final Uri FAILED = Uri.parse("content://sms/failed");
		public static final Uri QUEUED = Uri.parse("content://sms/queued");
		public static final Uri SENT = Uri.parse("content://sms/sent");
		public static final Uri DRAFT = Uri.parse("content://sms/draft");
		public static final Uri OUTBOX = Uri.parse("content://sms/outbox");
		public static final Uri UNDELIVERED = Uri.parse("content://sms/undelivered");
		public static final Uri ALL = Uri.parse("content://sms/");
		public static final Uri CONVERSATIONS = Uri.parse("content://sms/conversations");
	}

	public static interface SmsColumn {

		public static final String _ID = "_id";
		public static final String THREAD_ID = "thread_id";
		public static final String ADDRESS = "address";
		public static final String BODY = "body";
		public static final String DATE = "date";
		public static final String READ = "read";
		public static final String DATE_SENT = "date_sent";
		public static final String STATUS = "status";
		public static final String TYPE = "type";
		public static final String SEEN = "seen";
		public static final String PERSON = "person";
	}

	public static interface SmsType {
		public static final int MESSAGE_TYPE_ALL = 0;
		public static final int MESSAGE_TYPE_INBOX = 1;
		public static final int MESSAGE_TYPE_SENT = 2;
		public static final int MESSAGE_TYPE_DRAFT = 3;
		public static final int MESSAGE_TYPE_OUTBOX = 4;
		public static final int MESSAGE_TYPE_FAILED = 5; // for failed outgoing messages
		public static final int MESSAGE_TYPE_QUEUED = 6; // for messages to send later
	}

}
