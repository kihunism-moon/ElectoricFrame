package com.example.electoricframe

import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity: AppCompatActivity() {

    private val photoList = mutableListOf<Uri>()
    private var currentPosition = 0

    private var timer: Timer? = null

    private val photoImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.photoImageView)
    }

    private val backgroundPhotoImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.backgroundPhotoImageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoframe)

        Log.d("PhotoFrame", "onCreate!!! timer cancel")

        getPhotoUriFromIntent()
    }

    private fun getPhotoUriFromIntent() {
        val size = intent.getIntExtra("photoListSize", 0)
        for(i in 0..size) {
            intent.getStringExtra("photo$i")?.let {
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer() {
        timer = timer(period = 5 * 1000) {
            runOnUiThread{
                Log.d("PhotoFrame", "5초 지나감")

//                mainThread로 전환 -> runOnUiThread 코드
                val current = currentPosition
                val next = if(photoList.size <= currentPosition + 1) 0
                                else currentPosition + 1

                backgroundPhotoImageView.setImageURI(photoList[current])

//                alpha값은 투명도 -> 0f이면 보이지 않게 된다.
                photoImageView.alpha = 0f
                photoImageView.setImageURI(photoList[next])
                photoImageView.animate().alpha(1.0f).setDuration(1000)
                    .start()

                currentPosition = next

            }
        }
    }

    override fun onStop() {
        super.onStop()

        Log.d("PhotoFrame", "onStop!!! timer cancel")

        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()
        Log.d("PhotoFrame", "onStart!!! timer cancel")
        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PhotoFrame", "onDestroy!!! timer cancel")
        timer?.cancel()
    }

}