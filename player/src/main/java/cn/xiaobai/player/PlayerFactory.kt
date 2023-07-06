package cn.xiaobai.player

import android.app.Application
import androidx.media3.exoplayer.ExoPlayer
import cn.xiaobai.player.domain.TLPlayer
import cn.xiaobai.player.exoplayer.ExoPlayerImpl
import java.lang.ref.WeakReference

object PlayerFactory {
    fun create(
        app: Application
    ): TLPlayer = ExoPlayerImpl(WeakReference(app), ExoPlayer.Builder(app).build())
}