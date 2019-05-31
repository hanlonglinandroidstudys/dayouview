package com.example.myapplication.utils;


import android.app.Application;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.internal.XWalkSettingsInternal;
import org.xwalk.core.internal.XWalkViewBridge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * WebView统一设置工具类
 *
 * @author 韩龙林
 * @date 2019/5/29 13:54
 */
public class WebViewUtils {


    /**
     * 统一设置XWalkView
     *
     * @param mXwalkView  WebView对象
     * @param url         加载URL
     * @param application Application
     */
    public static void setXWalkView(XWalkView mXwalkView, String url, Application application, Handler handler) {

        //TODO mXwalkView长按崩溃  暂时这么解决
        mXwalkView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });

        //添加对javascript支持
        XWalkPreferences.setValue("enable-javascript", true);
        //开启调式,支持谷歌浏览器调式
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        //置是否允许通过file url加载的Javascript可以访问其他的源,包括其他的文件和http,https等其他的源
        XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
        //JAVASCRIPT_CAN_OPEN_WINDOW
        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
        // enable multiple windows.
        XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);
        // enable multiple windows.
        XWalkPreferences.setValue(XWalkPreferences.PROFILE_NAME, true);

        mXwalkView.addJavascriptInterface(new JsInterface(application, handler), "OpenManagerInterface");

        mXwalkView.setUIClient(new MyUIClient(mXwalkView));
        try {
            Method _getBridge = XWalkView.class.getDeclaredMethod("getBridge");
            _getBridge.setAccessible(true);
            XWalkViewBridge xWalkViewBridge = null;
            xWalkViewBridge = (XWalkViewBridge) _getBridge.invoke(mXwalkView);
            XWalkSettingsInternal xWalkSettings = xWalkViewBridge.getSettings();
            xWalkSettings.setJavaScriptEnabled(true);

            xWalkSettings.setAllowFileAccess(true);
            xWalkSettings.setAllowFileAccessFromFileURLs(false);
            xWalkSettings.setAllowUniversalAccessFromFileURLs(false);
            xWalkSettings.setAllowScriptsToCloseWindows(true);
            xWalkSettings.setAppCacheEnabled(true);
            xWalkSettings.setDatabaseEnabled(true);
            xWalkSettings.setDomStorageEnabled(true);
            xWalkSettings.setSaveFormData(true);
            xWalkSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            xWalkSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            xWalkSettings.setLoadsImagesAutomatically(true);
            String user = mXwalkView.getSettings().getUserAgentString() + ";webview_android;youpie";
            xWalkSettings.setUserAgentString(user);

            mXwalkView.load(url, null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static class MyUIClient extends XWalkUIClient {
        MyUIClient(XWalkView view) {
            super(view);
        }

        @Override
        public void onPageLoadStarted(XWalkView view, String url) {
            super.onPageLoadStarted(view, url);
        }

        @Override
        public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
            //取消计时
            super.onPageLoadStopped(view, url, status);
        }

        @Override
        public void openFileChooser(XWalkView view, ValueCallback<Uri> uploadFile, String acceptType, String capture) {
            super.openFileChooser(view, uploadFile, acceptType, capture);
        }
    }

    private static class JsInterface {
        Application application;
        Handler handler;

        public JsInterface() {

        }

        public JsInterface(Application application, Handler handler) {
            this.application = application;
            this.handler = handler;
        }

        @JavascriptInterface
        public void previewDoc(String docUrl) {
            //DownLoadUtils.downFile(docUrl, handler);
            Log.e("TAG", "开始下载--->" + docUrl);
        }

        @JavascriptInterface
        public String getAccountInfo() {
//            String info = LoginUtils.getAccountInfo();
//            return info;
            return "";
        }

        @JavascriptInterface
        public String getEquipInfo() {
//            String info = LoginUtils.getEquipInfo();
//            return info;
            return "";
        }

        @JavascriptInterface
        public String getToken() {
//            return E2EAppClientNewContext.getToken();
            return "";
        }

        @JavascriptInterface
        public void notifyException(String errorCode, String msg) {
            try {
                throw new Exception("错误码:" + errorCode + "错误信息:" + msg);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



}
