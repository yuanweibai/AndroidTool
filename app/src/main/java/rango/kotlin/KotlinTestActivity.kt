package rango.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_kotlin_test.*
import kotlinx.coroutines.*
import rango.kotlin.utils.*
import rango.tool.androidtool.R
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

class KotlinTestActivity : AppCompatActivity() {

    private lateinit var msgText: TextView

    private val arr: IntArray = intArrayOf(2, 3, 4)

    private val list = mutableListOf<String>()

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

    private var mills = 0L

    private val first: Double = 0.63
    private val second: Double = 0.62

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_test)


        list.add("ddd")
        mills = System.currentTimeMillis()

//        val jpgBitmap = BitmapFactory.decodeResource(resources, R.drawable.wallpaper_2)
//        Log.e("rango-bitmap", "big_time = ${System.currentTimeMillis() - mills}")
//
//        val options = BitmapFactory.Options()
//        options.inPreferredConfig = Bitmap.Config.RGB_565
//        mills = System.currentTimeMillis()
//        val webpBitmap = BitmapFactory.decodeResource(resources, R.drawable.wallpaper_2_test, options)
//        Log.e("rango-bitmap", "small_time = ${System.currentTimeMillis() - mills}")
//
//        Log.e("rango-bitmap", "jpgBitmap.allocationByteCount = ${jpgBitmap.allocationByteCount}，jpgBitmap.byteCount = ${jpgBitmap.byteCount}, width = ${jpgBitmap.width}, height = ${jpgBitmap.height},config = ${jpgBitmap.config.name}")
//        Log.e("rango-bitmap", "webpBitmap.allocationByteCount = ${webpBitmap.allocationByteCount}，webpBitmap.byteCount = ${webpBitmap.byteCount}, width = ${webpBitmap.width},height = ${webpBitmap.height},config = ${webpBitmap.config.name}")


        Bitmaps.getScaledBitmap(R.drawable.wallpaper_2)
//        Permissions.requestStoragePermission(this)
////        FileUtils.saveBitmap(jpgBitmap, "ddd.jpg", Bitmap.CompressFormat.JPEG)
////        FileUtils.saveBitmap(webpBitmap, "ccc.webp", Bitmap.CompressFormat.WEBP)

        msgText = findViewById(R.id.msg_text)
        Log.e("rango-onCreate", "threadName = " + Thread.currentThread().name)

        start_btn.setOnClickListener {

        }
        findViewById<View>(R.id.start_btn).setOnClickListener {
            val result = first - second
            Log.e("rango", "result = $result，first = $first,second = $second")
        }



        findViewById<View>(R.id.stop_btn).setOnClickListener {
            val bitmap = Bitmaps.createBitmapFromBase64(FileUtils.readImageBase64FromAssets())
            testImageView.setImageBitmap(bitmap)
        }




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