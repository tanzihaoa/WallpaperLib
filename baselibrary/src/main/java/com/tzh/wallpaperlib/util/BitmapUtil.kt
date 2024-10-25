package com.tzh.wallpaperlib.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.tzh.wallpaperlib.util.download.DownloadType
import com.tzh.wallpaperlib.util.download.FileDownloadUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object BitmapUtil {
    /**
     * res资源文件转bitmap
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    @JvmStatic
    fun resToBitmap(context: Context, resId: Int): Bitmap {
        val drawable: Drawable = context.resources.getDrawable(resId,null)
        val options : BitmapFactory.Options  =  BitmapFactory.Options()
        BitmapFactory.decodeResource(context.resources,resId,options)
        val bitmap: Bitmap = Bitmap.createBitmap(options.outWidth, options.outHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * 本地文件转bitmap
     */
    fun localFileToBitmap(file: File) : Bitmap {
        return BitmapFactory.decodeFile(file.path)
    }

    /**
     * url转bitmap
     */
    fun urlToBitmap(context: Context, imgUrl: String, listener: BitmapListener){
        val downloadUtil = FileDownloadUtil(context, DownloadType.Image)
        if(downloadUtil.isHaveFile(imgUrl)){
            val bitmap = localUrlToBitmap(downloadUtil.getPath(imgUrl))
            listener.sure(bitmap)
        }else{
            downloadUtil.onDownloadFile(imgUrl,object : FileDownloadUtil.OnDownloadListener() {
                override fun onSuccess(file: File) {
                    val bitmap = localFileToBitmap(file)
                    listener.sure(bitmap)
                }

                override fun onError(throwable: Throwable) {

                }
            })
        }
    }


    /**
     * 本地文件路径转bitmap
     */
    fun localUrlToBitmap(url : String) : Bitmap {
        return BitmapFactory.decodeFile(url)
    }

    /**
     * 为 Bitmap 设置切割圆角
     *
     * @param bitmap 原图片
     * @param cornerRadius     圆角 px
     * @return 按照参数切割圆角后的 Bitmap
     */
    @JvmStatic
    fun getRoundBitmap(context: Context,bitmap: Bitmap, cornerRadius: Float): Bitmap? {
        var output: Bitmap? = null
        output = try {
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_4444)
        } catch (error: OutOfMemoryError) {
            try {
                Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_4444)
            } catch (e: OutOfMemoryError) {
                return null
            }
        }
        val canvas = output?.let { Canvas(it) }
        val paint = Paint()
        paint.isAntiAlias = true
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        canvas?.drawARGB(0, 0, 0, 0)
        canvas?.drawRoundRect(rectF, DpToUtil.dip2px(context, cornerRadius)
            .toFloat(), DpToUtil.dip2px(context, cornerRadius).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas?.drawBitmap(bitmap, rect, rect, paint)
        return output?.let { getBitmapByBg(context,it, Color.parseColor("#00000000")) }
    }

    /**
     * bitmap转base64
     * */
    @JvmStatic
    fun bitmapToBase64(bitmap: Bitmap?): String? {
        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                baos.flush()
                baos.close()
                val bitmapBytes: ByteArray = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    @JvmStatic
    fun base64ToBitmap(base64Data: String?): Bitmap? {
        val bytes: ByteArray = Base64.decode(base64Data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /**
     * 修改bitmap背景颜色
     */
    private fun getBitmapByBg(context: Context,bitmap: Bitmap, color: Int): Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(ContextCompat.getColor(context,color))
        val paint = Paint()
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return newBitmap
    }

    /**
     * 修改bitmap背景颜色为透明
     */
    private fun getBitmapByBg(bitmap: Bitmap): Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.TRANSPARENT)
        val paint = Paint()
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return newBitmap
    }

    interface BitmapListener{
        fun sure(bitmap: Bitmap)
    }

    /**
     * 保存图片
     */
    fun saveUrl(context: Context, url : String){
        urlToBitmap(context,url,object : BitmapListener {
            override fun sure(bitmap: Bitmap) {
                saveBitmap(context,bitmap)
            }
        })
    }

    /**
     * 保存图片
     */
    fun saveBitmap(context: Context, bitmap: Bitmap) {
        val name =  System.currentTimeMillis().toString() +".jpg"
        if (Build.VERSION.SDK_INT >= 29) { //api版本  29 对应Android系统10.0
            val path = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                bitmap,
                name,
                ""
            )
            // 通知图库更新
            val scannerIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(path))
            context.sendBroadcast(scannerIntent)
            Toast.makeText(context, "已保存至手机相册", Toast.LENGTH_SHORT).show()
        } else {
            val fos: FileOutputStream
            try {
                val root = File(
                    Environment.getExternalStorageDirectory().toString(),
                    "MySaveImage"
                ) //此处自定义储存图片路径文件夹名称
                if (!root.exists()) {
                    root.mkdirs()
                }
                val file = File(
                    root, name
                )
                fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
                fos.flush()
                fos.close()
                // 最后通知图库更新
                context.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(File(file.path))
                    )
                )
                Toast.makeText(context, "已保存至手机相册", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}