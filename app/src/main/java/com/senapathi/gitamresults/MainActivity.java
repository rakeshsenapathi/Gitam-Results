package com.senapathi.gitamresults;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import Utils.NetworkUtil;
import butterknife.Bind;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends BaseActivity {

    @Bind(R.id.webView)
    protected WebView webView;
    private String homeURL = "https://eweb.gitam.edu/mobile/Pages/NewGrdcrdInput1.aspx";

    private static final int PERMISSION_REQUEST_CODE = 200;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!checkPermission()) {
            requestPermission();
        }

        getSupportActionBar().setTitle("Gitam Results");
        webView.loadUrl(homeURL);
        final Random rand = new Random();
        if (NetworkUtil.isOnline(this)) {
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    findViewById(R.id.share_btn).setVisibility(View.VISIBLE);
                    if (progressDialog == null)
                        progressDialog = ProgressDialog.show(MainActivity.this, null, "Loading..");
                    super.onPageStarted(view, url, favicon);
                }

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (Uri.parse(url).getHost().equals("eweb.gitam.edu")) {
                        return false;
                    }
                    return true;
                }


                @Override
                public void onPageFinished(final WebView view, String url) {
                    // TODO Auto-generated method stub
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    webView.setDrawingCacheEnabled(true);
                    findViewById(R.id.share_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!checkPermission()) {
                                Toast.makeText(getApplicationContext(), "Storage Permission Required", Toast.LENGTH_SHORT).show();
                                requestPermission();
                            }
                            webView.measure(View.MeasureSpec.makeMeasureSpec(
                                    View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                            webView.layout(0, 0, webView.getMeasuredWidth(),
                                    webView.getMeasuredHeight());
                            webView.setDrawingCacheEnabled(true);
                            webView.buildDrawingCache();
                            Bitmap bm = Bitmap.createBitmap(webView.getMeasuredWidth(),
                                    webView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

                            Canvas bigcanvas = new Canvas(bm);
                            Paint paint = new Paint();
                            int iHeight = bm.getHeight();
                            bigcanvas.drawBitmap(bm, 0, iHeight, paint);
                            webView.draw(bigcanvas);
                            if (bm != null) {
                                try {
                                    File folder = new File(Environment.getExternalStorageDirectory()
                                            .toString() + "/GitamResults/Images");
                                    folder.mkdirs();
                                    String extStorageDirectory = folder.toString();
                                    File file = new File(extStorageDirectory, rand.nextInt(10000) + ".png");
                                    OutputStream fOut = null;
                                    fOut = new FileOutputStream(file);

                                    Toast.makeText(getApplicationContext(), "Saved to Images", Toast.LENGTH_SHORT).show();

                                    bm.compress(Bitmap.CompressFormat.PNG, 50, fOut);
                                    Uri ImageURI = Uri.fromFile(file);
                                    Intent shareImageIntent = new Intent(Intent.ACTION_SEND);
                                    shareImageIntent.setType("image/png");
                                    shareImageIntent.putExtra(Intent.EXTRA_STREAM, ImageURI);
                                    startActivity(Intent.createChooser(shareImageIntent, "Share image using"));
                                    fOut.flush();
                                    fOut.close();
                                    bm.recycle();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        }
                    });
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


    //Permissions Section

    //Checks for permission
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    //Requests for permission
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    //
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (writeGranted && readGranted) {
                        Log.e("info", "Permissions Granted");
                    } else {
                        Log.e("info", "Permissions not granted");
                    }
                }
                break;
        }
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
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
