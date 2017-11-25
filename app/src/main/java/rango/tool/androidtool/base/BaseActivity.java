package rango.tool.androidtool.base;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by baiyuanwei on 17/11/15.
 */

public class BaseActivity extends AppCompatActivity {

    protected void initFragment(int id, Fragment fragment) {
        replaceFragment(id, fragment);
    }

    protected void replaceFragment(int id, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment);
        ft.commitAllowingStateLoss();
    }

    public void startActivity(Class cl) {
        Intent intent = new Intent(this, cl);
        startActivity(intent);
    }
}
