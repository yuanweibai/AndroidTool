package rango.kotlin.coroutines

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import rango.tool.androidtool.R
import rango.tool.androidtool.base.BaseActivity
import kotlinx.android.synthetic.main.activity_coroutines_layout.*
import kotlinx.coroutines.*

class CoroutinesActivity : BaseActivity() {

    companion object {
        const val TAG = "CoroutinesActivity"
    }

    private val coroutinesScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines_layout)

        sequenceBtn.setOnClickListener {
            sequenceRequest()
        }

        parallelBtn.setOnClickListener {
            parallelRequest()
        }

    }

    private fun sequenceRequest() {
        Log.e(TAG, "requestData-Thread: " + Thread.currentThread().name)
        val job = coroutinesScope.launch {
            Log.e(TAG, "launch-Thread: " + Thread.currentThread().name)
            val token = getToken()
            Log.e(TAG, "getToken end.... token = $token")
            val name = getName(token)
            Log.e(TAG, "getName end.... name = $name")
            sequenceMsgText.text = name
        }
    }

    private suspend fun getToken(): String {
        return withContext(Dispatchers.IO) {
            delay(3000)
            Log.e(TAG, "getToken-Thread: " + Thread.currentThread().name)
            "token"

        }
    }

    private suspend fun getName(token: String): String = withContext(Dispatchers.IO) {
        Thread.sleep(5000)
        Log.e(TAG, "getName-Thread: " + Thread.currentThread().name)
        "$token-name"
    }

    @SuppressLint("SetTextI18n")
    private fun parallelRequest() {
        Log.e(TAG, "parallelRequest start....")

        coroutinesScope.launch {
            Log.e(TAG, "parallel starting....")
            val imageUrl = async { getImageUrl() }
            val age = async { getAge() }
            val address = async { getAddress() }

            Log.e(TAG, "parallel image start....")
            val url = imageUrl.await()
            Log.e(TAG, "parallel image end.... url = $url")

            Log.e(TAG, "parallel age start....")
            val ageResult = age.await()
            Log.e(TAG, "parallel age end.... ageResult = $ageResult")

            Log.e(TAG, "parallel address start....")
            val addressResult = address.await()
            Log.e(TAG, "parallel address end.... address = $addressResult")


            Log.e(TAG, "parallel set text....")
            parallelMsgText.text = "${imageUrl.await()} - ${age.await()} - ${address.await()}"
            Log.e(TAG, "parallel set text end....")
        }
        Log.e(TAG, "parallelRequest end....")
    }

    private suspend fun getImageUrl(): String {
        return withContext(Dispatchers.IO) {
            Thread.sleep(8000)
            "imageUrl"
        }
    }

    private suspend fun getAge(): Int = withContext(Dispatchers.IO) {
        Thread.sleep(10000)
        90
    }

    private suspend fun getAddress(): String = withContext(Dispatchers.IO) {
        delay(15000)
        "Address"
    }
}