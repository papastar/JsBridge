package com.github.lzyzsd.jsbridge.example;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.Button;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

public class MainActivity extends Activity implements OnClickListener {

	private final String TAG = "MainActivity";

	BridgeWebView webView;

	Button button;

	int RESULT_CODE = 0;

	ValueCallback<Uri> mUploadMessage;



    static class User {
        String name;
        String phone;
        String userId;
        String token;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        webView = (BridgeWebView) findViewById(R.id.webView);

		button = (Button) findViewById(R.id.button);

		button.setOnClickListener(this);

		webView.setDefaultHandler(new DefaultHandler());

		webView.setWebChromeClient(new WebChromeClient() {

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
				this.openFileChooser(uploadMsg);
			}

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
				this.openFileChooser(uploadMsg);
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				mUploadMessage = uploadMsg;
				pickFile();
			}
		});

		webView.loadUrl("file:///android_asset/demo.html");
        setBridgeHandle();

//		webView.registerHandler("submitFromWeb", new BridgeHandler() {
//
//			@Override
//			public void handler(String data, CallBackFunction function) {
//				Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
//                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
//			}
//
//		});



//        webView.callHandler("functionInJs", new Gson().toJson(user), new CallBackFunction() {
//            @Override
//            public void onCallBack(String data) {
//
//            }
//        });

        webView.send("hello");

	}

	private void setBridgeHandle(){
        //界面跳转
        webView.registerHandler("route", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                WebViewResult result = new WebViewResult();
                try {
                    //Router.getInstance().openUrl(data);
                    Uri uri = Uri.parse(data);
                    if(TextUtils.equals(uri.getScheme(),"zze")){
                        String lastPath = uri.getLastPathSegment();
                    }
                    result.setCode(WebViewResult.CODE_SUCCESS);
                }catch (Exception e){
                    result.setCode(WebViewResult.CODE_FAIL);
                }finally {
                    function.onCallBack(result.getResult());
                }
                //Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                //function.onCallBack("submitFromWeb exe, response data 中文 from Java");
            }

        });
        //分享
        webView.registerHandler("share", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                WebViewResult result = new WebViewResult();
                try {
                    String url = JSONUtils.getString(data,"title","");
                    String desc = JSONUtils.getString(data,"desc","");
                    String link = JSONUtils.getString(data,"link","");
                    String imgurl = JSONUtils.getString(data,"imgurl","");
                    doShare(url,desc,link,imgurl);
                    result.setCode(WebViewResult.CODE_SUCCESS);
                }catch (Exception e){
                    result.setCode(WebViewResult.CODE_FAIL);
                }finally {
                    function.onCallBack(result.getResult());
                }
            }

        });
        //获取数据
        webView.registerHandler("getdata", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                WebViewResult<User> result = new WebViewResult<>();
                try {
                    String type = JSONUtils.getString(data,"type","");
                    if(TextUtils.equals("1",type)){
                        User user = new User();
                        user.name = "Papa";
                        user.phone = "134XXXXXXXX";
                        user.token = "755fgw4twtr1g5gewq";
                        user.userId = "5678995";
                        result.setData(user);
                    }
                    result.setCode(WebViewResult.CODE_SUCCESS);
                }catch (Exception e){
                    result.setCode(WebViewResult.CODE_FAIL);
                }finally {
                    function.onCallBack(result.getResult());
                }
            }

        });



    }

    private void doShare(String title,String desc,String link,String imgurl){
        //doShare;
    }


	public void pickFile() {
		Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
		chooserIntent.setType("image/*");
		startActivityForResult(chooserIntent, RESULT_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == RESULT_CODE) {
			if (null == mUploadMessage){
				return;
			}
			Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}

	@Override
	public void onClick(View v) {
		if (button.equals(v)) {
            webView.callHandler("functionInJs", "data from Java", new CallBackFunction() {

				@Override
				public void onCallBack(String data) {
					// TODO Auto-generated method stub
					Log.i(TAG, "reponse data from js " + data);
				}

			});
		}

	}

}
