package rango.kotlin.wanandroid.main

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.wa_fragment_home_layout.*
import rango.kotlin.wanandroid.R
import rango.kotlin.wanandroid.common.list.BaseItemData

class WAHomeFragment : Fragment() {

    private val homeRecyclerAdapter = HomeRecyclerAdapter()

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.wa_fragment_home_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        homeRecyclerView.layoutManager = LinearLayoutManager(context)
        homeRecyclerView.adapter = homeRecyclerAdapter
        homeRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = 36
            }
        })

        homeViewModel.homeLiveData.observe(viewLifecycleOwner, Observer { articleListBean ->

            if (articleListBean == null) {
                return@Observer
            }

            val list: MutableList<BaseItemData<*>> = ArrayList()
            articleListBean.datas.forEach {
                val flag = if (it.isFresh) {
                    "新"
                } else {
                    ""
                }
                val titleData = ArticleTitleData(it.shareUser, flag, it.title)
                list.add(BaseItemData(titleData))
            }
            homeRecyclerAdapter.update(list)
        })

        homeViewModel.requestHomeData(0)
    }
}