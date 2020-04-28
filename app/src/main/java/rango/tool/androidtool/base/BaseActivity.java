package rango.tool.androidtool.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {

    private static final String TAG_DIALOG_FRAGMENT = "dialog_fragment";

    protected void initFragment(int id, Fragment fragment) {
        replaceFragment(id, fragment);
    }

    protected void replaceFragment(int id, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment);
//        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    public void startActivity(Class cl) {
        Intent intent = new Intent(this, cl);
        startActivity(intent);
    }

    public void showDialogFragment(DialogFragment dialogFragment) {
        if (isFinishing()) {
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getCurrentDialogFragmentByTag(TAG_DIALOG_FRAGMENT);
        if (prev != null) {
            ft.remove(prev);
        }
        dialogFragment.show(ft, TAG_DIALOG_FRAGMENT);
    }

    public Fragment getCurrentDialogFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1f;
        resources.updateConfiguration(configuration, null);
        return resources;
    }
}
