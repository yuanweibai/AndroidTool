package rango.tool.androidtool.list.activity

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import rango.tool.androidtool.R
import rango.tool.androidtool.base.BaseActivity

class GoodRecyclerActivity : BaseActivity() {

    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_layout)
        recyclerView = findViewById(R.id.recycler_view)
    }
}