package rango.kotlin.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import rango.tool.androidtool.R
import rango.tool.androidtool.experiments.activity.AnyThingActivity
import rango.tool.androidtool.game.hero.GameHeroActivity

class MyAppWidget : AppWidgetProvider() {

    companion object {
        const val TAG: String = "MyAppWidget"
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.e(TAG, "onUpdate()")

        appWidgetIds?.forEach { id ->
            val intent: PendingIntent = Intent(context, GameHeroActivity::class.java)
                    .let { PendingIntent.getActivity(context, 0, it, 0) }

            val testIntent: PendingIntent = Intent(context, AnyThingActivity::class.java)
                    .let { PendingIntent.getActivity(context, 0, it, 0) }
            val views: RemoteViews = RemoteViews(context?.packageName, R.layout.widget_layout)
                    .apply {
                        setOnClickPendingIntent(R.id.title_view, intent)
                        setOnClickPendingIntent(R.id.test_btn, testIntent)
                    }

            appWidgetManager?.updateAppWidget(id, views)
        }
    }

    override fun onAppWidgetOptionsChanged(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int, newOptions: Bundle?) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        Log.e(TAG, "onAppWidgetOptionsChanged()")
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        Log.e(TAG, "onDeleted()")
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Log.e(TAG, "onEnabled()")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.e(TAG, "onDisabled()")
    }

}