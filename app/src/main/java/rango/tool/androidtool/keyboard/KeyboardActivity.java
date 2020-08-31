package rango.tool.androidtool.keyboard;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import rango.tool.androidtool.R;

public class KeyboardActivity extends AppCompatActivity {

    private final String LAST_KEYBOARD_HEIGHT = "last_keyboard_height";

    private View mMainContainer;
    private KeyBackObservableEditText mInput;
    private View mAdditionBtn;
    private View mPlaceHolder;

    private Rect tmp = new Rect();
    private int mScreenHeight;

    private boolean mPendingShowPlaceHolder;

    private final int DISTANCE_SLOP = 180;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        initLayout();

        mInput.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // Keyboard -> PlaceHolder
                if (mPendingShowPlaceHolder) {
                    // 在设置mPendingShowPlaceHolder时已经调用了隐藏Keyboard的方法，直到Keyboard隐藏前都取消重给
                    if (isKeyboardVisible()) {
                        return false;
                    } else { // 键盘已隐藏，显示PlaceHolder
                        showPlaceHolder();
                        mPendingShowPlaceHolder = false;
                        return false;
                    }
                } else {
                    if (isPlaceHolderVisible() && isKeyboardVisible()) {
                        hidePlaceHolder();
                        return false;
                    }
                }
                return true;
            }
        });

        mAdditionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 除非软键盘和PlaceHolder全隐藏时直接显示PlaceHolder，其他情况此处处理软键盘，onPreDrawListener处理PlaceHolder
                if (isPlaceHolderVisible()) { // PlaceHolder -> Keyboard
                    showSoftInput(mInput);
                } else if (isKeyboardVisible()) { // Keyboard -> PlaceHolder
                    mPendingShowPlaceHolder = true;
                    hideSoftInput(mInput);
                } else { // Just show PlaceHolder
                    showPlaceHolder();
                }
            }
        });
    }

    private void showPlaceHolder() {
        if (mPlaceHolder.getVisibility() != View.VISIBLE) {
            mPlaceHolder.setVisibility(View.VISIBLE);
        }
    }

    private void hidePlaceHolder() {
        if (mPlaceHolder.getVisibility() != View.GONE) {
            mPlaceHolder.setVisibility(View.GONE);
        }
    }

    public void showSoftInput(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean isPlaceHolderVisible() {
        // 忽略INVISIBLE状态
        return mPlaceHolder.getVisibility() == View.VISIBLE;
    }

    private boolean isKeyboardVisible() {
        return (getDistanceFromInputToBottom() > DISTANCE_SLOP && !isPlaceHolderVisible())
                || (getDistanceFromInputToBottom() > (mPlaceHolder.getHeight() + DISTANCE_SLOP) && isPlaceHolderVisible());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mScreenHeight <= 0) {
            mMainContainer.getGlobalVisibleRect(tmp);
            mScreenHeight = tmp.bottom;
        }
    }

    /**
     * 输入框的下边距离屏幕的距离
     */
    private int getDistanceFromInputToBottom() {
        return mScreenHeight - getInputBottom();
    }

    /**
     * 输入框下边的位置
     */
    private int getInputBottom() {
        mInput.getGlobalVisibleRect(tmp);
        return tmp.bottom;
    }

    private void initLayout() {
        mMainContainer = findView(R.id.main_container);
        ListView list = findView(R.id.list);
        mInput = findView(R.id.input);
        mAdditionBtn = findView(R.id.addition);
        mPlaceHolder = findView(R.id.placeholder);

        int defaultHeight = dp2px(this, 240);
        int height = getPreferences(MODE_PRIVATE).getInt(LAST_KEYBOARD_HEIGHT, defaultHeight);
//        ViewGroup.LayoutParams params = mPlaceHolder.getLayoutParams();
//        if (params != null) {
//            params.height = height;
//            mPlaceHolder.setLayoutParams(params);
//        }

        mInput.setOnBackPressedListener(new KeyBackObservableEditText.OnBackPressedListener() {

            @Override
            public boolean onBackPressed() {
                if (isPlaceHolderVisible()) {
                    hidePlaceHolder();
                    return true;
                } else if (isKeyboardVisible()) {
                    hideSoftInput(mInput);
                    return true;
                }
                return false;
            }
        });

        list.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 30;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView txt = new TextView(KeyboardActivity.this);
                txt.setPadding(32, 32, 32, 32);
                txt.setText("Text " + position);
                return txt;
            }
        });
    }

    public int dp2px(Context context, float dpValue) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                context.getResources().getDisplayMetrics()) + 0.5);
    }

    @SuppressWarnings("unchecked")
    private <T> T findView(@IdRes int resId) {
        return (T) findViewById(resId);
    }
}
