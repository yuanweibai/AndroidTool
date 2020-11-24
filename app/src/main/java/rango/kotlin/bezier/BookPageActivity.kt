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

//        bookTextView.text = getTestData()
    }

    private fun getTestData(): String {
        val list = BookLoaderManager.getBookContent()
        val buffer = StringBuffer()
        list.forEach {
            buffer.append(it)
            buffer.append("\n")
        }
        return buffer.toString()
    }
}