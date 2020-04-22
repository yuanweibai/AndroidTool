package rango.tool.androidtool.accessibility;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.market.MarketTools;

public class AccessibilityActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accessibility_layout);

        findViewById(R.id.get_acc_permission_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAcc();
            }
        });

        findViewById(R.id.start_acc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.app_detail_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAppDetail();
            }
        });
    }

    private void gotoAcc() {
        if (!AccessibilityUtils.isAccessibilityGranted()) {
            AccessibilityUtils.gotoAcc();
        } else {
            Toast.makeText(this, "Accessibility 权限已开启", Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoAppDetail() {
        MarketTools.getTools().startMarket(ToolApplication.getContext(), "com.colorphone.smooth.dialer.cn");
    }
}
