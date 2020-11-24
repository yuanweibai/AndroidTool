package rango.kotlin.bezier

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_book_page.*
import rango.tool.androidtool.R
import rango.tool.androidtool.base.BaseActivity

class BookPageActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_page)

        bookPageView.setOnClickListener {
            settingGroup.visibility = View.VISIBLE
        }

        maskView.setOnClickListener {
            settingGroup.visibility = View.GONE
        }
    }
}