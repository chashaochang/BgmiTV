package cn.xiaobai.bgmi.ui.widget

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.PlaybackParameters
import androidx.media3.ui.PlayerView
import cn.xiaobai.bgmi.feature.detail.DetailViewModel
import cn.xiaobai.player.control.PlayerControls
import cn.xiaobai.player.control.PlayerControlsState
import cn.xiaobai.player.control.PlayerMenu
import cn.xiaobai.player.control.PlayerMenuState
import cn.xiaobai.player.control.VideoListState
import cn.xiaobai.player.domain.TLPlayer
import kotlinx.coroutines.delay

@Composable
fun FullScreenPlayerView(
    modifier: Modifier,
    viewModel: DetailViewModel,
    isFullScreen: Boolean,
    videoPlayerState: PlayerControlsState,
    playerMenuState: PlayerMenuState,
    videoListState:VideoListState
) {
    val focusRequester = remember {
        FocusRequester()
    }
    val player = viewModel.player
    var contentCurrentPosition: Long by remember { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(300)
            contentCurrentPosition = player.exoPlayer().currentPosition
        }
    }
    LaunchedEffect(isFullScreen){
        //focusRequester.freeFocus()
        playerMenuState.isDisplayed = false
    }
    Box(
        //modifier = modifier.focusRequester(focusRequester),
        contentAlignment = Alignment.BottomCenter
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    useController = false
                    this.player = player.exoPlayer()
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
        )
        if(isFullScreen){
            PlayerControls(
                modifier = Modifier.align(Alignment.BottomCenter),
                isPlaying = player.exoPlayer().isPlaying,
                onPlayPauseToggle = { shouldPlay ->
                    if (shouldPlay) {
                        player.play()
                    } else {
                        player.pause()
                    }
                },
                contentProgressInMillis = contentCurrentPosition,
                contentDurationInMillis = player.exoPlayer().duration,
                state = videoPlayerState,
                onSeek = { seekProgress ->
                    player.seekTo(player.exoPlayer().duration.times(seekProgress).toLong())
                }
            )
            PlayerMenu(
                modifier = Modifier.align(Alignment.BottomCenter),
                state = playerMenuState,
                videoListState = videoListState,
                onVideoChange = {
                    viewModel.playVideo(videoListState.videoList[it].value.getValue("path"))
                },
                onVideoSpeedChange = {
                    val playbackParameters = PlaybackParameters(it, 1.0f)
                    viewModel.player.exoPlayer().playbackParameters = playbackParameters
                },
                onMenuTop = {
                    videoPlayerState.isDisplayed = true
                }
            )
        }
    }
}