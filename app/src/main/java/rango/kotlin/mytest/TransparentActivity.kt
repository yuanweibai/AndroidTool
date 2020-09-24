package rango.kotlin.mytest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.telecom.CallAudioState
import android.util.Log
import android.view.*
import android.view.accessibility.AccessibilityEvent
import rango.kotlin.sound.SoundManager
import rango.tool.androidtool.R
import rango.tool.androidtool.ToolApplication
import rango.tool.androidtool.base.BaseActivity

class TransparentActivity : BaseActivity() {

    companion object {

        var isStart = false
        var activity: Activity? = null

        @JvmStatic
        fun start() {
            if (isStart) {
                return
            }
            isStart = true
            SoundManager.playBackgroundMusic()
            val context = ToolApplication.getContext()

            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.flags = Intent.FLAG_ACTIVITY_NO_USER_ACTION or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setClass(context, TransparentActivity::class.java)
            context.startActivity(intent)
        }

        @JvmStatic
        fun stop() {
            activity?.finish()
            activity = null

        }


    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.e("rango", "dispatchTouchEvent")
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activity = this
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                or WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES
                or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
//        window.callback = object : Window.Callback{
//            override fun onActionModeFinished(mode: ActionMode?) {
//                Log.e("rango","onActionModeFinished")
//            }
//
//            override fun onCreatePanelView(featureId: Int): View? {
//                Log.e("rango","onCreatePanelView")
//                return null
//            }
//
//            override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
//                Log.e("rango","dispatchTouchEvent")
//                return false
//            }
//
//            override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean {
//                Log.e("rango","onCreatePanelMenu")
//                return false
//            }
//
//            override fun onWindowStartingActionMode(callback: ActionMode.Callback?): ActionMode? {
//                Log.e("rango","onWindowStartingActionMode")
//                return null
//            }
//
//            override fun onWindowStartingActionMode(callback: ActionMode.Callback?, type: Int): ActionMode? {
//                Log.e("rango","onWindowStartingActionMode")
//                return null
//            }
//
//            override fun onAttachedToWindow() {
//                Log.e("rango","onAttachedToWindow")
//            }
//
//            override fun dispatchGenericMotionEvent(event: MotionEvent?): Boolean {
//                Log.e("rango","dispatchGenericMotionEvent")
//                return false
//            }
//
//            override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean {
//                Log.e("rango","dispatchPopulateAccessibilityEvent")
//                return false
//            }
//
//            override fun dispatchTrackballEvent(event: MotionEvent?): Boolean {
//                Log.e("rango","dispatchTrackballEvent")
//                return false
//            }
//
//            override fun dispatchKeyShortcutEvent(event: KeyEvent?): Boolean {
//                Log.e("rango","dispatchKeyShortcutEvent")
//                return false
//            }
//
//            override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
//                Log.e("rango","dispatchKeyEvent")
//                return false
//            }
//
//            override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
//                Log.e("rango","onMenuOpened")
//                return false
//            }
//
//            override fun onPanelClosed(featureId: Int, menu: Menu) {
//                Log.e("rango","onPanelClosed")
//            }
//
//            override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
//                Log.e("rango","onMenuItemSelected")
//                return false
//            }
//
//            override fun onDetachedFromWindow() {
//                Log.e("rango","onDetachedFromWindow")
//            }
//
//            override fun onPreparePanel(featureId: Int, view: View?, menu: Menu): Boolean {
//                Log.e("rango","onPreparePanel")
//                return false
//            }
//
//            override fun onWindowAttributesChanged(attrs: WindowManager.LayoutParams?) {
//                Log.e("rango","onWindowAttributesChanged")
//            }
//
//            override fun onWindowFocusChanged(hasFocus: Boolean) {
//                Log.e("rango","onWindowFocusChanged")
//            }
//
//            override fun onContentChanged() {
//                Log.e("rango","onContentChanged")
//            }
//
//            override fun onSearchRequested(): Boolean {
//                Log.e("rango","onSearchRequested")
//                return false
//            }
//
//            override fun onSearchRequested(searchEvent: SearchEvent?): Boolean {
//                Log.e("rango","onSearchRequested")
//                return false
//            }
//
//            override fun onActionModeStarted(mode: ActionMode?) {
//                Log.e("rango","onActionModeStarted")
//            }
//        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transparent_layout)

    }


    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        isStart = false
        SoundManager.pauseBackgroundMusic()
    }
}