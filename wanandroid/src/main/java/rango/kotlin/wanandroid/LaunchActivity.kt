package rango.kotlin.wanandroid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.wa_activity_launch_layout.*

class LaunchActivity : AppCompatActivity() {

    private val launchViewModel: LaunchViewModel by lazy {
        ViewModelProvider(this).get(LaunchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (launchViewModel.isComeToPrivacyPage()) {
            startLoginPage()
        } else {
            launchViewModel.recordComeToPrivacyPage()
            setContentView(R.layout.wa_activity_launch_layout)
            initPrivacyView()
        }
    }

    private fun initPrivacyView() {
        launchBtn.setOnClickListener{
            launchViewModel.saveUserPrivacyStatus(true)
            startLoginPage()
        }
    }

    private fun startLoginPage(){
        val intent = Intent(this, WALoginActivity::class.java)
        startActivity(intent)
    }
}
