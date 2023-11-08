package com.tzh.myapplication.activity

import android.content.Context
import android.content.Intent
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivitySpliceVideoBinding

class SpliceVideoActivity : AppBaseActivity<ActivitySpliceVideoBinding>(R.layout.activity_splice_video) {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context,SpliceVideoActivity::class.java))
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }
}