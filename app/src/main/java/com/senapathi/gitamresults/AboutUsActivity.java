package com.senapathi.gitamresults;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import butterknife.OnClick;

/**
 * Created by Senapathi on 13-06-2016.
 */
public class AboutUsActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.about_us_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableBackButton();

    }

    @OnClick(R.id.more)
    public void startMoreDetails() {
        enableBackButton();
        new LibsBuilder()
                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                //start the activity
                .start(this);
    }

    @OnClick(R.id.version_number)
    public void versionToast() {
        Toast.makeText(this, getResources().getString(R.string.version_number), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.dev)
    public void devToast() {
        Toast.makeText(this, getResources().getString(R.string.dev_name), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        disableBackButton();
    }
}
