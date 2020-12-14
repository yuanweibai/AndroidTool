package rango.kotlin.wanandroid.home

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
import rango.kotlin.wanandroid.common.list.BaseItemType
import rango.kotlin.wanandroid.common.list.OnLoadMoreListener
import rango.kotlin.wanandroid.common.utils.Threads

class WAHomeFragment : Fragment() {

    companion object {
        fun createInstance(): WAHomeFragment {
            return WAHomeFragment()
        }
    }

    private val homeRecyclerAdapter = HomeRecyclerAdapter()

    private var isLoadMore = false

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
                val position = parent.getChildAdapterPosition(view)
                if (position > 0) {
                    outRect.top = 36
                }
            }
        })

        homeRecyclerView.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                isLoadMore = true
                homeRecyclerAdapter.update(homeRecyclerAdapter.itemCount - 1, BaseItemData(HomeLoadMoreData(HomeLoadMoreData.STATUS_LOADING), BaseItemType.TYPE_COMMON_LOAD_MORE_ITEM))
                Threads.postMain(Runnable {
                    homeViewModel.loadMoreHomeData()
                }, 3000)

            }
        })

        homeViewModel.homeLiveData.observe(viewLifecycleOwner, Observer { articleListBean ->

            if (articleListBean == null) {
                return@Observer
            }

            loadingStatusText.visibility = View.GONE
            homeRecyclerView.visibility = View.VISIBLE

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
            val firstPage = 1
            if (articleListBean.curPage > firstPage) {
                homeRecyclerView.onLoadMoreFinished()
                homeRecyclerAdapter.append(homeRecyclerAdapter.itemCount - 1, list)
            } else {
                list.add(BaseItemData(HomeLoadMoreData(HomeLoadMoreData.STATUS_LOADING), BaseItemType.TYPE_COMMON_LOAD_MORE_ITEM))
                homeRecyclerAdapter.update(list)
            }
        })

        homeViewModel.requestErrorLiveData.observe(viewLifecycleOwner, Observer {
            if (isLoadMore) {
                homeRecyclerView.onLoadMoreFinished()
                homeRecyclerAdapter.update(homeRecyclerAdapter.itemCount - 1, BaseItemData(HomeLoadMoreData(HomeLoadMoreData.STATUS_LOADING_FAILURE, Runnable {
                    homeViewModel.loadMoreHomeData()
                }), BaseItemType.TYPE_COMMON_LOAD_MORE_ITEM))
            } else {
                loadingStatusText.text = "加载失败"
            }
        })

        loadingStatusText.visibility = View.VISIBLE
        homeRecyclerView.visibility = View.GONE
        loadingStatusText.text = "加载中..........."
        isLoadMore = false
        homeViewModel.refreshHomeData()
    }
}