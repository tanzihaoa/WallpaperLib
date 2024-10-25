package com.tzh.wallpaperlib.service

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.service.wallpaper.WallpaperService
import android.text.TextUtils
import android.view.SurfaceHolder
import java.io.IOException

class VideoWallpaper : WallpaperService() {

    /**
     * 设置壁纸
     * @param context
     */
    companion object {
        var sVideoPath : String = ""
        var isVolume = true
        fun setToWallPaper(context: Context, videoPath: String, isVolumes: Boolean) {
            sVideoPath = videoPath
            isVolume = isVolumes
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(context, VideoWallpaper::class.java)
            )
            context.startActivity(intent)
        }
    }

    override fun onCreateEngine(): Engine {
        return VideoWallpaperEngine()
    }

    internal inner class VideoWallpaperEngine : Engine() {
        private var mMediaPlayer: MediaPlayer? = null
        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)

        }

        override fun onDestroy() {
            super.onDestroy()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            if (visible) {
                mMediaPlayer?.start()
            } else {
                mMediaPlayer?.pause()
            }
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            if (TextUtils.isEmpty(sVideoPath)) {
                throw NullPointerException("videoPath must not be null ")
            } else {
                mMediaPlayer = MediaPlayer()
                mMediaPlayer?.setSurface(holder.surface)
                try {
                    mMediaPlayer?.setDataSource(sVideoPath)
                    mMediaPlayer?.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
                    mMediaPlayer?.isLooping = true
                    if (isVolume) {
                        mMediaPlayer?.setVolume(1.0f, 1.0f)
                    } else {
                        mMediaPlayer?.setVolume(0f, 0f)
                    }
                    mMediaPlayer?.prepare()
                    mMediaPlayer?.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            if (mMediaPlayer != null) {
                mMediaPlayer!!.release()
                mMediaPlayer = null
            }
        }
    }
}