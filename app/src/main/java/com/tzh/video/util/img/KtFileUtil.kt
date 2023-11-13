package com.tzh.video.util.img

import android.content.Context
import com.tzh.mylibrary.util.AppPathManager
import java.io.File

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
}