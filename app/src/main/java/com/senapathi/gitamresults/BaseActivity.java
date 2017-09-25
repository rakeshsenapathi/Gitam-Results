package com.senapathi.gitamresults;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.webkit.WebView;

import butterknife.ButterKnife;

/**
 * Created by Senapathi on 12-06-2016.
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }

    protected abstract int getLayoutId();

    public void enableBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void disableBackButton(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


}
