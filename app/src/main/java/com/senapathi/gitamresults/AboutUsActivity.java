package com.senapathi.gitamresults;

import android.os.Bundle;
import android.support.annotation.Nullable;

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
    }

    @OnClick(R.id.more)
    public void startMoreDetails(){
        new LibsBuilder()
                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                //start the activity
                .start(this);
    }
}
