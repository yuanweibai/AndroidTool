package rango.kotlin.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import com.tencent.bugly.crashreport.CrashReport
import rango.tool.androidtool.R

object SoundManager {

    const val TAG = "SoundManager"

    private var backgroundMusicMediaPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null
    fun init(context: Context?) {

        audioManager = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        backgroundMusicMediaPlayer = MediaPlayer.create(context, R.raw.background_music)
        if (backgroundMusicMediaPlayer == null) {
            CrashReport.postCatchedException(NullPointerException("backgroundMusicMediaPlayer is null in the SoundManager!!!"))
            return
        }
        backgroundMusicMediaPlayer!!.isLooping = true

        val volume = 0.8f
        backgroundMusicMediaPlayer!!.setVolume(volume, volume)
    }

    private val mAudioFocusChange = AudioManager.OnAudioFocusChangeListener { focusChange ->
        if (backgroundMusicMediaPlayer == null) {
            return@OnAudioFocusChangeListener
        }
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> backgroundMusicMediaPlayer!!.stop()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> backgroundMusicMediaPlayer!!.pause()
            AudioManager.AUDIOFOCUS_GAIN -> backgroundMusicMediaPlayer!!.start()
        }
    }

    fun playBackgroundMusic() {
        val volume = audioManager?.getStreamVolume(AudioManager.STREAM_SYSTEM)
        Log.e(TAG, "system_volume = $volume")
        audioManager?.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_PLAY_SOUND)

        if (backgroundMusicMediaPlayer != null && !backgroundMusicMediaPlayer!!.isPlaying) {
            backgroundMusicMediaPlayer!!.start()
        }
    }

    fun pauseBackgroundMusic() {
        if (backgroundMusicMediaPlayer != null && backgroundMusicMediaPlayer!!.isPlaying) {
            backgroundMusicMediaPlayer!!.pause()
        }
    }
}