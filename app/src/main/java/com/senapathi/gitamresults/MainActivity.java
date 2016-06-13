package com.senapathi.gitamresults;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import Utils.NetworkUtil;
import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.webView)
    protected WebView webView;

    private String UserURL = "";

    private String homeURL = "https://eweb.gitam.edu/mobile/Pages/NewGrdcrdInput1.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Gitam Results");

        if (NetworkUtil.isOnline(this)) {
            webView.loadUrl(homeURL);
            findViewById(R.id.share_btn).setVisibility(View.VISIBLE);
            webView.setWebViewClient(new WebViewClient() {

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // do your handling codes here, which url is the requested url
                    // probably you need to open that url rather than redirect:
                    view.loadUrl(url);
                    UserURL = url;
                    findViewById(R.id.share_btn).setVisibility(View.GONE);
                    return false; // then it is not handled by default action
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
                    super.onPageFinished(view, url);
                }

            });
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, NoInternet.class);
            startActivity(intent);
            finish();
        }

    }


    @OnClick(R.id.share_btn)
    public void shareAction() {
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Results");
        i.putExtra(android.content.Intent.EXTRA_TEXT, homeURL);
        startActivity(Intent.createChooser(i, "Share via"));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                        findViewById(R.id.share_btn).setVisibility(View.VISIBLE);
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


}
