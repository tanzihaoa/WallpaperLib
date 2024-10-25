package com.tzh.wallpaperlib.util.video

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.tzh.wallpaperlib.util.download.DownloadType
import com.tzh.wallpaperlib.util.download.FileDownloadUtil
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


object VideoUtil {
    fun saveVideo(context: Context,url : String){
        val downloadUtil = FileDownloadUtil(context, DownloadType.MP4)
        if(downloadUtil.isHaveFile(url)){
            val file = File(downloadUtil.getPath(url))
            saveFile(context,file)
        }else{
            downloadUtil.onDownloadFile(url,object : FileDownloadUtil.OnDownloadListener() {
                override fun onSuccess(file: File) {
                    saveFile(context,file)
                }

                override fun onError(throwable: Throwable) {

                }
            })
        }
    }

    fun saveFile(context: Context,file: File){
        saveVideoToSystemAlbum(context,file.absolutePath)
        Toast.makeText(context, "视频已保存至手机", Toast.LENGTH_SHORT).show()
    }

    /**
     * 将视频保存到系统图库
     *
     * @param videoFile
     * @param context
     */
    fun saveVideoToSystemAlbum(context: Context,videoFile: String): Boolean {
        return try {
            val localContentResolver = context.contentResolver
            val localContentValues =
                getVideoContentValues(File(videoFile), System.currentTimeMillis())
            val localUri = localContentResolver.insert(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                localContentValues
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && context.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.Q) {
                // 拷贝到指定uri,如果没有这步操作，android11不会在相册显示
                try {
                    val out = context.contentResolver.openOutputStream(localUri!!)
                    copyFile(videoFile, out)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri))
            //将该文件扫描到相册
            //MediaScannerConnection.scanFile(context, new String[] { videoFile }, null, null);
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 拷贝文件
     * @param oldPath
     * @param out
     * @return
     */
    fun copyFile(oldPath: String, out: OutputStream?): Boolean {
        try {
            var bytesum = 0
            var byteread = 0
            val oldfile = File(oldPath)
            if (oldfile.exists()) {
                // 读入原文件
                val inStream: InputStream = FileInputStream(oldPath)
                val buffer = ByteArray(1444)
                while (inStream.read(buffer).also { byteread = it } != -1) {
                    bytesum += byteread //字节数 文件大小
                    println(bytesum)
                    out!!.write(buffer, 0, byteread)
                }
                inStream.close()
                out!!.close()
                return true
            } else {
                Log.w("", String.format("文件(%s)不存在。", oldPath))
            }
        } catch (e: Exception) {
            Log.e("", "复制单个文件操作出错")
            e.printStackTrace()
        }
        return false
    }

    /**
     * 获取视频 contentValue
     * @param paramFile
     * @param paramLong
     * @return
     */
    fun getVideoContentValues(paramFile: File, paramLong: Long): ContentValues? {
        val localContentValues = ContentValues()
        localContentValues.put("title", paramFile.name)
        localContentValues.put("_display_name", paramFile.name)
        localContentValues.put("mime_type", "video/mp4")
        localContentValues.put("datetaken", java.lang.Long.valueOf(paramLong))
        localContentValues.put("date_modified", java.lang.Long.valueOf(paramLong))
        localContentValues.put("date_added", java.lang.Long.valueOf(paramLong))
        localContentValues.put("_data", paramFile.absolutePath)
        localContentValues.put("_size", java.lang.Long.valueOf(paramFile.length()))
        return localContentValues
    }
}