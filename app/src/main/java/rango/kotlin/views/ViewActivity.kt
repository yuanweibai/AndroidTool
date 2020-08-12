package rango.kotlin.views

import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v4.view.animation.PathInterpolatorCompat
import rango.tool.androidtool.R
import rango.tool.androidtool.base.BaseActivity

class ViewActivity : BaseActivity() {

    private lateinit var bladeFlashBtn: BladeFlashImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_views)

        bladeFlashBtn = findViewById(R.id.spin_button)
    }

    private var startLightAnimator: ValueAnimator? = null
    private fun startLightAnim() {
        if (startLightAnimator == null) {
            startLightAnimator = ValueAnimator.ofFloat(0f, 1f)
            startLightAnimator?.duration = 1770
            startLightAnimator?.interpolator = PathInterpolatorCompat.create(0.54f, 0.33f, 0.26f, 0.81f)
            startLightAnimator?.addUpdateListener {
                val value = it.animatedValue as Float
                bladeFlashBtn.setFlashTranslationProgress(value)
            }
            startLightAnimator?.repeatCount = ValueAnimator.INFINITE
        }

        startLightAnimator?.start()
    }


    override fun onResume() {
        super.onResume()

        startLightAnim()
    }

    override fun onPause() {
        super.onPause()

        startLightAnimator?.cancel()
    }
}