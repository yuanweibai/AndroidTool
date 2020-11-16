package rango.kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.*
import rango.tool.androidtool.R
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

class KotlinTestActivity : AppCompatActivity() {

    private lateinit var msgText: TextView

    private val arr: IntArray = intArrayOf(2, 3, 4)

    private var name: String by Delegates.observable("dd") { property, oldValue, newValue ->
        run {
            Log.e("rango", "dd_$oldValue")
        }
    }


    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    val con = object : CoroutineContext {
        override fun <R> fold(initial: R, operation: (R, CoroutineContext.Element) -> R): R {
            TODO("Not yet implemented")
        }

        override fun <E : CoroutineContext.Element> get(key: CoroutineContext.Key<E>): E? {
            TODO("Not yet implemented")
        }

        override fun minusKey(key: CoroutineContext.Key<*>): CoroutineContext {
            TODO("Not yet implemented")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_test)




        msgText = findViewById(R.id.msg_text)
        Log.e("rango-onCreate", "threadName = " + Thread.currentThread().name)

        findViewById<View>(R.id.start_btn).setOnClickListener {
            startAction()
        }

        findViewById<View>(R.id.stop_btn).setOnClickListener(View.OnClickListener() {

        })
    }

    fun startAction() {
        coroutineScope.launch(Dispatchers.Main) {
            Log.e("rango-launch-1", "threadName = " + Thread.currentThread().name)
            val name = async { getName() }
            val age = async { getAge() }
            Log.e("rango-launch-2", "threadName = " + Thread.currentThread().name)
            msgText.text = (name.await() + age.await())
        }


    }

    fun stopAction() {
        coroutineScope.cancel()
    }

    suspend fun getName(): String {
        return withContext(Dispatchers.IO) {
            Log.e("rango-getName-1", "threadName = " + Thread.currentThread().name)
            delay(TimeUnit.SECONDS.toMillis(10))
            Log.e("rango-getName-2", "threadName = " + Thread.currentThread().name)
            "Name"
        }
    }

    suspend fun getAge(): Int {
        return withContext(Dispatchers.IO) {
            Log.e("rango-getAge-1", "threadName = " + Thread.currentThread().name)
            delay(TimeUnit.SECONDS.toMillis(8))
            Log.e("rango-getAge-2", "threadName = " + Thread.currentThread().name)
            89
        }
    }

    fun test() {
        val o = object {
            val x = 2
            val y = 9
        }

        Log.e("string", "${o.y}")

    }


}