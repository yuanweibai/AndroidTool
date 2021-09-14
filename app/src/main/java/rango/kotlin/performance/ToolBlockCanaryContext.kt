package rango.kotlin.performance

import android.content.Context
import android.util.Log
import com.github.moduth.blockcanary.BlockCanaryContext
import com.github.moduth.blockcanary.internal.BlockInfo

class ToolBlockCanaryContext() : BlockCanaryContext() {

    override fun provideBlockThreshold(): Int {
        return 100
    }

    override fun displayNotification(): Boolean {
        return true
    }

    override fun onBlock(context: Context?, blockInfo: BlockInfo?) {
        Log.e("rango-block",blockInfo?.toString())
    }

}