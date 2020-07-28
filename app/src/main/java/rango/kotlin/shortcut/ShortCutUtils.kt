package rango.kotlin.shortcut

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.content.pm.ShortcutInfoCompat
import android.support.v4.content.pm.ShortcutManagerCompat
import android.support.v4.graphics.drawable.IconCompat
import android.util.Log
import rango.tool.androidtool.R
import rango.tool.androidtool.ToolApplication
import rango.tool.androidtool.game.hero.GameHeroActivity

object ShortCutUtils {

    fun addShortCut(): Unit {
        val shortCutIntent = Intent(ToolApplication.getContext(), GameHeroActivity::class.java)
                .apply { action = Intent.ACTION_VIEW }

        val iconCompat: IconCompat = IconCompat.createWithBitmap(BitmapFactory.decodeResource(ToolApplication.getContext().resources, R.drawable.solon))

        val shortCutInfo: ShortcutInfoCompat = ShortcutInfoCompat.Builder(ToolApplication.getContext(), "dddd")
                .setIcon(iconCompat)
                .setShortLabel("jjjjjjj")
                .setIntent(shortCutIntent)
                .build()
        val result = ShortcutManagerCompat.requestPinShortcut(ToolApplication.getContext(), shortCutInfo, null)
        Log.e("rango", "result = $result");
    }

}