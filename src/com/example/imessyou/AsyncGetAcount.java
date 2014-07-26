//package com.example.imessyou;
//
//import org.apache.http.HttpStatus;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.res.Resources;
//import android.os.AsyncTask;
//import android.util.Log;
//
//public class AsyncGetAcount extends AsyncTask<Void, Void, Void> {
//
//	private static final String TAG = AsyncGetAcount.class.getName();
//	private Activity activity = null;
//	private JSONParser jsonParser;
//
//	public ProgressDialog pDialog;
//	private Resources res;
//
//	private String url;
//	private String errorMsg = null;
//	private boolean success = false;
//	private int error = 1;
//	private int statusCode;
//	private String status = null;
//	private String response = null;
//	private String extra = null;
//	private Dialog mDialog = null;
//
//	public AsyncGetAcount(Activity mActivity) {
//		this.activity = mActivity;
//
//		if (null != this.activity) {
//			this.pDialog = new ProgressDialog(this.activity);
//			this.res = this.activity.getResources();
//			this.jsonParser = new JSONParser(this.activity, this.res);
//		}
//	}
//
//	@SuppressLint("SimpleDateFormat")
//	@Override
//	protected void onPostExecute(Void result) {
//		super.onPostExecute(result);
//
//		if ((null != this.pDialog) && (true == this.pDialog.isShowing())) {
//			pDialog.dismiss();
//		}
//
//		if (null != this.activity) {
//			if (statusCode != HttpStatus.SC_OK) {
//
//			} else {
//
//			}
//		}
//	}
//
//	@Override
//	protected void onPreExecute() {
//		super.onPreExecute();
//		Log.e(TAG, "check dialog");
//		if (null != this.pDialog) {
//			Log.e(TAG, "dialog showed");
//			pDialog.setMessage(res.getString(R.string.prog_loading_info));
//			pDialog.setCancelable(false);
//			pDialog.setIndeterminate(false);
//			pDialog.show();
//		}
//	}
//
//	@Override
//	protected void onCancelled(Void result) {
//		super.onCancelled(result);
//		Log.e(TAG, "onCancelled was called");
//	}
//
//	@Override
//	protected Void doInBackground(Void... arg0) {
//
//		JSONObject jObject = new JSONObject();
//
//		if (null != this.activity) {
//
//			try {
//
//				jObject.putOpt("sessionId", ProjectVariables.SESSION_ID);
//
//				JSONObject jsonResponse = null;
//
//				if (!isCancelled()) {
//
//					JSONObject jsonResponse1 = jsonParser.makeHttpRequest(
//							AppConstants.GET_USERINFO, "POST", jObject);
//					jsonResponse = new JSONObject(jsonResponse1.toString());
//				}
//
//				if (null == jsonResponse) {
//					success = false;
//					error = 1;
//					return null;
//				}
//				if (null != jsonResponse) {
//					statusCode = jsonResponse.getInt("statusCode");
//					response = jsonResponse.getString("response");
//
//					if (statusCode == 200) {
//						Log.e(TAG, response);
//						JSONObject object = new JSONObject(response);
//						status = object.getString("status");
//
//						if (status.equalsIgnoreCase("OK")) {
//							String data = object.getString("data");
//							JSONObject dBase = new JSONObject(data);
//
//							Log.e(TAG, dBase.toString());
//
//							ProjectVariables.FIRSTNAME = dBase
//									.getString("firstName");
//							ProjectVariables.LASTNAME = dBase
//									.getString("lastName");
//							ProjectVariables.EMAIL = dBase.getString("email");
//							ProjectVariables.PHONE_NO = dBase
//									.getString("mobileNumber");
//
//						}
//
//					}
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return null;
//	}
//
//}