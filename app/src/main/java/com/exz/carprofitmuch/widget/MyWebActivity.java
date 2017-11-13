//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.exz.carprofitmuch.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.exz.carprofitmuch.R;
import com.szw.framelibrary.base.BaseActivity;

import im.delight.android.webview.AdvancedWebView;
import im.delight.android.webview.AdvancedWebView.Listener;

public class MyWebActivity extends BaseActivity implements Listener {
    AdvancedWebView mWebView;
    TextView mTitle;
    ImageView mLeftImg;
    Toolbar toolbar;
    ProgressBar mProgressBar;
    public static String Intent_Url = "info";
    public static String Intent_Title = "name";
    private boolean isAnimStart = false;
    private int currentProgress;

    public MyWebActivity() {
    }

    public boolean initToolbar() {
        this.mTitle.setTextSize(18.0F);
        this.mTitle.setTextColor(ContextCompat.getColor(this, R.color.color_toolBar_title));
        this.mTitle.setMaxEms(7);
        this.toolbar.setContentInsetsAbsolute(0, 0);
        this.mLeftImg.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if(MyWebActivity.this.mWebView.onBackPressed()) {
                    MyWebActivity.this.finish();
                }

            }
        });
        this.mTitle.setText(this.getIntent().getStringExtra(Intent_Title));
        this.setSupportActionBar(this.toolbar);
        return false;
    }

    public int setInflateId() {
        return R.layout.activity_my_web;
    }

    public void init() {
        this.mWebView = (AdvancedWebView)this.findViewById(R.id.mWebView);
        this.mTitle = (TextView)this.findViewById(R.id.mTitle);
        this.mLeftImg = (ImageView)this.findViewById(R.id.mLeftImg);
        this.toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        this.mProgressBar = (ProgressBar)this.findViewById(R.id.progressBar);
        this.mWebView.setListener(this, this);
        this.mWebView.loadUrl(this.getIntent().getStringExtra(Intent_Url));
        this.mWebView.getSettings().setJavaScriptEnabled(true);
//启用数据库
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();

//启用地理定位
        webSettings.setGeolocationEnabled(true);
//设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);

//最重要的方法，一定要设置，这就是出不来的主要原因

        webSettings.setDomStorageEnabled(true);


        this.mWebView.setBackgroundColor(ContextCompat.getColor(this.mContext, R.color.app_bg));
        this.mWebView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("http://3gimg.qq.com/lightmap/components/locationPicker2/back.html?name=")) {
                    Intent intent = getIntent();
                    intent.putExtra("url",url);
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                }
                MyWebActivity.this.mProgressBar.setVisibility(View.VISIBLE);
                MyWebActivity.this.mProgressBar.setAlpha(1.0F);
            }

            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

        });
        this.mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                MyWebActivity.this.currentProgress = MyWebActivity.this.mProgressBar.getProgress();
                if(newProgress >= 100 && !MyWebActivity.this.isAnimStart) {
                    MyWebActivity.this.isAnimStart = true;
                    MyWebActivity.this.mProgressBar.setProgress(newProgress);
                    MyWebActivity.this.startDismissAnimation(MyWebActivity.this.mProgressBar.getProgress());
                } else {
                    MyWebActivity.this.startProgressAnimation(newProgress);
                }


            }
            //配置权限（同样在WebChromeClient中实现）
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
    }


    private void startProgressAnimation(int newProgress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(this.mProgressBar, "progress", this.currentProgress, newProgress);
        animator.setDuration(300L);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    private void startDismissAnimation(final int progress) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(this.mProgressBar, "alpha", 1.0F, 0.0F);
        anim.setDuration(1500L);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                int offset = 100 - progress;
                MyWebActivity.this.mProgressBar.setProgress((int)((float)progress + (float)offset * fraction));
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                MyWebActivity.this.mProgressBar.setProgress(0);
                MyWebActivity.this.mProgressBar.setVisibility(View.GONE);
                MyWebActivity.this.isAnimStart = false;
            }
        });
        anim.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mWebView.onPause();
    }

    public void onDestroy() {
        this.mWebView.onDestroy();
        super.onDestroy();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        this.mWebView.onActivityResult(requestCode, resultCode, intent);
    }

    public void onBackPressed() {
        if(this.mWebView.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void onPageStarted(String url, Bitmap favicon) {
    }

    public void onPageFinished(String url) {
    }

    public void onPageError(int errorCode, String description, String failingUrl) {
    }

    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
    }

    public void onExternalPageRequest(String url) {
    }
}
