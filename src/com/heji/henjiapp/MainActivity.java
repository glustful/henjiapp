package com.heji.henjiapp;

import android.net.http.SslError;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

	private WebView mWebView;
	private ProgressBar pb;
	private String url = "http://www.hsh101.com/m/";
	
	@Override
	public void onBackPressed() {
			
			if(mWebView!=null && mWebView.canGoBack()){
				
				mWebView.goBack();
				
			}else{
				
			super.onBackPressed();
			}
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mWebView = (WebView) super.findViewById(R.id.recommend_web_view);
		pb = (ProgressBar) findViewById(R.id.progressBar);
		initWebView();
		 
		mWebView.loadUrl(url);
		
		
	}
	
	

	private void initWebView() {
		
		mWebView.setWebChromeClient(new MyWebChromeClient());

		mWebView.setWebViewClient(new MyWebViewClient());
		
		// Configure the webview
		WebSettings s = mWebView.getSettings();
		s.setBuiltInZoomControls(false);
		s.setDefaultTextEncodingName("gbk");
		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		s.setUseWideViewPort(true);
		s.setLoadWithOverviewMode(true);
		s.setSavePassword(true);
		s.setSaveFormData(true);
		s.setJavaScriptEnabled(true);
		
		s.setCacheMode(WebSettings.LOAD_NO_CACHE);
		s.setGeolocationEnabled(true);
		s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");

		s.setDomStorageEnabled(true);
		
	}
	
	long preTime = 0;
	

	private class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
			if(newProgress >= 50){
				pb.setVisibility(View.GONE);
			}
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			
		}
		
		/**
		 * ����Ĭ�ϵ�window.alertչʾ���棬����title����ʾΪ��������file:////��
		 */
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {

			final AlertDialog.Builder builder = new AlertDialog.Builder(
					view.getContext());

			builder.setTitle("�Ի���").setMessage(message)
					.setPositiveButton("ȷ��", null);

			// ����Ҫ�󶨰����¼�
			// ����keycode����84֮��İ���
			builder.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + event);
					return true;
				}
			});
			// ��ֹ��Ӧ��back�����¼�
			builder.setCancelable(false);
			AlertDialog dialog = builder.create();
			dialog.show();
			result.confirm();// ��Ϊû�а��¼�����Ҫǿ��confirm,����ҳ�������ʾ�������ݡ�
			return true;

		}

		public boolean onJsBeforeUnload(WebView view, String url,
				String message, JsResult result) {
			return super.onJsBeforeUnload(view, url, message, result);
		}

		/**
		 * ����Ĭ�ϵ�window.confirmչʾ���棬����title����ʾΪ��������file:////��
		 */
		public boolean onJsConfirm(WebView view, String url, String message,
				final JsResult result) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					view.getContext());

			builder.setTitle("�Ի���")
					.setMessage(message)
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									result.confirm();
								}
							})
					.setNeutralButton("ȡ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									result.cancel();
								}
							});
			builder.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					result.cancel();
				}
			});

			// ����keycode����84֮��İ��������ⰴ�����¶Ի�����Ϣ��ҳ���޷��ٵ����Ի��������
			builder.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					Log.v("onJsConfirm", "keyCode==" + keyCode + "event="
							+ event);
					return true;
				}
			});
			// ��ֹ��Ӧ��back�����¼�
			builder.setCancelable(false);
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;

		}

		/**
		 * ����Ĭ�ϵ�window.promptչʾ���棬����title����ʾΪ��������file:////��
		 * window.prompt('����������������ַ', '618119.com');
		 */
		public boolean onJsPrompt(WebView view, String url, String message,
				String defaultValue, final JsPromptResult result) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					view.getContext());

			builder.setTitle("�Ի���").setMessage(message);

			final EditText et = new EditText(view.getContext());
			et.setSingleLine();
			et.setText(defaultValue);
			builder.setView(et)
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									result.confirm(et.getText().toString());
								}

							})
					.setNeutralButton("ȡ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									result.cancel();
								}
							});

			// ����keycode����84֮��İ��������ⰴ�����¶Ի�����Ϣ��ҳ���޷��ٵ����Ի��������
			builder.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					Log.v("onJsPrompt", "keyCode==" + keyCode + "event="
							+ event);
					return true;
				}
			});

			// ��ֹ��Ӧ��back�����¼�
			builder.setCancelable(false);
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;

		}

	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			super.onPageStarted(view, url, favicon);
			pb.setVisibility(View.VISIBLE);
			
		}
		
	

		@Override
		public void onPageFinished(WebView view, String url) {
			
			super.onPageFinished(view, url);
			
		}


		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {

			handler.proceed();
		}
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			view.loadUrl(url);
			return false;
		}
	}

}
