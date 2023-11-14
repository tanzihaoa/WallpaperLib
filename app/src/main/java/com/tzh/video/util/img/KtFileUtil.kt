package com.tzh.video.util.img

import android.content.Context
import com.tzh.mylibrary.util.AppPathManager
import com.tzh.video.base.MyApplication
import java.io.File
import java.util.Random

object KtFileUtil {

    /**
     * 获取 图片文件下载目录
     *
     */
    fun getImageCacheFolder(context : Context): File? {
        val file = context.getExternalFilesDir("image/")
        AppPathManager.ifFolderExit(file?.absolutePath)
        return file
    }

    /**
     * 获取 视频文件下载目录
     *
     */
    fun getVideoCacheFolder(context : Context): File? {
        val file = context.getExternalFilesDir("video/")
        AppPathManager.ifFolderExit(file?.absolutePath)
        return file
    }

    /**
     * 获取 视频路径文件
     *
     */
    fun getTextCacheFolder(context : Context): File? {
        val file = context.getExternalFilesDir("video/list.text")
        AppPathManager.ifFolderExit(file?.absolutePath)
        return file
    }

    /**
     * 获取一个随机的视频名
     */
    fun getRandomVideoName() : String{
        return getVideoCacheFolder(MyApplication.mContext)?.absolutePath + "/" + System.currentTimeMillis().toString() + Random(1000).nextInt().toString() + ".mp4"
    }
}