package com.cadyd.app.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.ui.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by SCH-1 on 2016/7/28.
 */
public class AboutsActivity extends BaseActivity {

    @Bind(R.id.about_us_content)
    TextView content;

    @Bind(R.id.about_us_version)
    TextView version;
    public static String filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_abouts_us_layout, getString(R.string.about_us_title), true);
        filepath =   this.getCacheDir().getAbsolutePath();
        initData();
    }

    private void initData() {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);
            String versionName = pi.versionName;
            version.setText("当前版本 " + versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        content.setText(R.string.about_us_str);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        finishActivity();
    }
}
