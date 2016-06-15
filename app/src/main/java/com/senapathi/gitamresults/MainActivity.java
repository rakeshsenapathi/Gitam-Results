package com.senapathi.gitamresults;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Gitam Results");

        if (NetworkUtil.isOnline(this)) {


            findViewById(R.id.share_btn).setVisibility(View.VISIBLE);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    if (progressDialog == null)
                        progressDialog = ProgressDialog.show(MainActivity.this, null, "Loading..");
                    super.onPageStarted(view, url, favicon);
                }

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // do your handling codes here, which url is the requested url
                    // probably you need to open that url rather than redirect:
                    UserURL = url;
                    findViewById(R.id.share_btn).setVisibility(View.GONE);
                    if (Uri.parse(url).getHost().equals("eweb.gitam.edu")) {
                        return false;
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }


                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    super.onPageFinished(view, url);

                }

            });
            webView.loadUrl(homeURL);


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
        startActivity(Intent.createChooser(i, "Share HomePage"));
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

                        alertDialog();

                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about_us:
                //newGame();
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
