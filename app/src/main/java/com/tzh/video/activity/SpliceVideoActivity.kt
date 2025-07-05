package com.tzh.video.activity

import android.Manifest
import android.content.Intent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.tzh.video.R
import com.tzh.video.base.AppBaseActivity
import com.tzh.video.databinding.ActivitySpliceVideoBinding
import com.tzh.video.dialog.VideoHintDialog
import com.tzh.wallpaperlib.util.OnPermissionCallBackListener
import com.tzh.wallpaperlib.util.PermissionXUtil


class SpliceVideoActivity : AppBaseActivity<ActivitySpliceVideoBinding>(R.layout.activity_splice_video) {

    companion object{
        fun start(activity : AppCompatActivity){
            PermissionXUtil.requestAnyPermission(activity, mutableListOf<String>().apply {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            },object : OnPermissionCallBackListener {
                override fun onAgree() {
                    activity.startActivity(Intent(activity,SpliceVideoActivity::class.java))
                }

                override fun onDisAgree() {

                }
            })
        }
    }

    val mDialog by lazy {
        VideoHintDialog(this)
    }


    override fun initView() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding.activity = this
    }


    override fun initData() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}