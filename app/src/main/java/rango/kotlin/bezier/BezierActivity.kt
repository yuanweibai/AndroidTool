package rango.kotlin.bezier

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_bezier.*
import rango.tool.androidtool.R
import rango.tool.androidtool.base.BaseActivity

class BezierActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bezier)

        bezier2Btn.setOnClickListener {
            bezierView.setBezierSteps(BezierView.TWO_STEPS)
        }

        bezier3Btn.setOnClickListener {
            bezierView.setBezierSteps(BezierView.THREE_STEPS)
        }

        bezierBtn.setOnClickListener {
            heartGroup.visibility = View.GONE
            bezierGroup.visibility = View.VISIBLE
        }

        heartStartBtn.setOnClickListener {
            heartView.start()
        }

        heartBtn.setOnClickListener {
            bezierGroup.visibility = View.GONE
            heartGroup.visibility = View.VISIBLE
        }
    }
}