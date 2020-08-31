package rango.tool.androidtool.base;

import android.content.Intent;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;


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
}
