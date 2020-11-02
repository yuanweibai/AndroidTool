package rango.kotlin.wanandroid

import androidx.lifecycle.ViewModel
import rango.kotlin.wanandroid.common.utils.PreferencesUtil
import javax.crypto.KeyAgreement

class LaunchViewModel : ViewModel() {

    companion object {
        private const val USER_PRIVACY_STATUS = "user_privacy_status"
        private const val USER_EVER_COME_TO_PRIVACY_PAGE = "user_ever_come_to_privacy_page"
    }

    fun saveUserPrivacyStatus(agreement: Boolean) {
        PreferencesUtil.putBoolean(USER_PRIVACY_STATUS, agreement)
    }

    fun getUserPrivacyStatus(): Boolean {
        return PreferencesUtil.getBoolean(USER_PRIVACY_STATUS, false)
    }

    fun recordComeToPrivacyPage() {
        PreferencesUtil.putBoolean(USER_EVER_COME_TO_PRIVACY_PAGE, true)
    }

    fun isComeToPrivacyPage(): Boolean {
        return PreferencesUtil.getBoolean(USER_EVER_COME_TO_PRIVACY_PAGE, false)
    }


}