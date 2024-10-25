package com.tzh.wallpaperlib.util.download

import androidx.annotation.StringDef

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@MustBeDocumented
@StringDef(DownloadType.File, DownloadType.Image, DownloadType.MP3, DownloadType.MP4)
@Retention(AnnotationRetention.SOURCE)
annotation class DownloadType {
    companion object{
        /**
         * 文件
         */
        const val File = "File"

        /**
         * 图片
         */
        const val Image = "Image"

        /**
         * 音频
         */
        const val MP3 = "MP3"
        /**
         * 视频
         */
        const val MP4 = "MP4"
    }
}