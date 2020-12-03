package rango.kotlin

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_all_test.*
import rango.kotlin.bezier.BezierActivity
import rango.kotlin.coroutines.CoroutinesActivity
import rango.kotlin.motion.MotionActivity
import rango.kotlin.mytest.share.Person
import rango.tool.androidtool.R
import rango.tool.androidtool.base.BaseActivity

class AllTestActivity : BaseActivity() {

    private var name = ""
    private var person: Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_test)

        coroutinesBtn.setOnClickListener {
            startActivity(CoroutinesActivity::class.java)
        }

        kotlinTestBtn.setOnClickListener {
            startActivity(KotlinTestActivity::class.java)
        }

        bezierBtn.setOnClickListener {
            startActivity(BezierActivity::class.java)
        }

        motionBtn.setOnClickListener {
            startActivity(MotionActivity::class.java)
        }
    }

}