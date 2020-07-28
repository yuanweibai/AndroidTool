package rango.kotlin.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.RemoteViews
import android.widget.TextView
import rango.kotlin.shortcut.ShortCutUtils
import rango.tool.androidtool.BuildConfig
import rango.tool.androidtool.R
import rango.tool.androidtool.ToolApplication
import rango.tool.androidtool.base.BaseActivity
import rango.tool.androidtool.viewpager.ViewPagerActivity

class WidgetActivity : BaseActivity() {

    var addWidgetBtn: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_layout)
        addWidgetBtn = findViewById(R.id.add_widget_btn)

        addWidgetBtn?.setOnClickListener {
            val appWidgetManager: AppWidgetManager = ToolApplication.getContext().getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager
            val myProvider = ComponentName(ToolApplication.getContext(), MyAppWidget::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (appWidgetManager.isRequestPinAppWidgetSupported) {
                    val pendingIntent: PendingIntent = Intent(this, ViewPagerActivity::class.java)
                            .let { PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT) }
                    appWidgetManager.requestPinAppWidget(myProvider, null, null)
                }
            }
        }

        findViewById<View>(R.id.update_widget_btn).apply {
            setOnClickListener {
                val myProvider = ComponentName(ToolApplication.getContext(), MyAppWidget::class.java)
                val views: RemoteViews = RemoteViews(ToolApplication.getContext().packageName, R.layout.widget_layout)
                        .apply {
                            setTextViewText(R.id.title_view, "hhhhhh")
                        }
                AppWidgetManager.getInstance(ToolApplication.getContext()).updateAppWidget(myProvider, views)
            }
        }
        findViewById<View>(R.id.add_shortcut_btn).apply {
            setOnClickListener {
                ShortCutUtils.addShortCut()
            }
        }

    }
}