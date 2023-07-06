package cn.xiaobai.bgmi.feature.detail

import android.view.KeyEvent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.tv.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.IconButtonDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import cn.xiaobai.bgmi.R
import cn.xiaobai.bgmi.core.network.retrofit.BGmiBaseUrl
import cn.xiaobai.bgmi.ui.theme.colorBackground
import cn.xiaobai.bgmi.ui.widget.FocusableSurface
import cn.xiaobai.bgmi.ui.widget.FullScreenPlayerView
import cn.xiaobai.player.control.VideoListControl
import cn.xiaobai.player.control.VideoListState
import cn.xiaobai.player.control.rememberPlayerMenuState
import cn.xiaobai.player.control.rememberVideoListState
import cn.xiaobai.player.control.rememberVideoPlayerState

@Composable
fun DetailRoute(
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    DetailScreen(viewModel = viewModel, onBackClick = onBackClick)
}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun DetailScreen(
    viewModel: DetailViewModel,
    onBackClick: () -> Unit
) {
    val dataList = viewModel.dataList.collectAsState().value
    val playList = viewModel.playList.collectAsState().value
    val videoId = viewModel.videoId.collectAsState().value
    var isFullScreen by remember {
        mutableStateOf(false)
    }
    if (dataList.isNotEmpty()) {
        val video = dataList.find { e -> e.id == videoId }
        video?.let {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorBackground)
                    .padding(if (isFullScreen) 0.dp else 24.dp),
                shape = if (isFullScreen) RoundedCornerShape(0.dp) else RoundedCornerShape(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorBackground)
                ) {
                    val videoListState = rememberVideoListState(videoList = playList)
                    Row {
                        val coroutineScope = rememberCoroutineScope()
                        val videoPlayerState =
                            rememberVideoPlayerState(hideSeconds = 4, coroutineScope)
                        val playerMenuState =
                            rememberPlayerMenuState(hideSeconds = 0, coroutineScope)
                        FocusableSurface(
                            isFullScreen = isFullScreen,
                            onClick = {
                                if (!isFullScreen) {
                                    isFullScreen = true
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(16 / 9f)
                                .onKeyEvent {
                                    if (isFullScreen) {
                                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_DOWN && !playerMenuState.isDisplayed && !videoPlayerState.isDisplayed) {
                                            playerMenuState.isDisplayed = true
                                            videoPlayerState.isDisplayed = false
                                            return@onKeyEvent true
                                        } else if ((it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_LEFT || it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) && !playerMenuState.isDisplayed && !videoPlayerState.isDisplayed) {
                                            videoPlayerState.isDisplayed = true
                                            playerMenuState.isDisplayed = false
                                            return@onKeyEvent true
                                        } else {
                                            return@onKeyEvent false
                                        }
                                    } else {
                                        return@onKeyEvent false
                                    }
                                },
                        ) {
                            videoPlayerState.isDisplayed = false
                            FullScreenPlayerView(
                                modifier = Modifier.fillMaxSize(),
                                viewModel,
                                isFullScreen,
                                videoPlayerState,
                                playerMenuState,
                                videoListState
                            )
                        }
                        AnimatedVisibility(
                            modifier = Modifier.weight(0.5f),
                            visible = !isFullScreen
                        ) {
                            Row {
                                Spacer(modifier = Modifier.width(20.dp))
                                Column {
                                    Text(
                                        text = video.name,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = if (video.episode == 0) "暂无更新" else "更新至" + video.episode + "集",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    IconButton(
                                        onClick = {
                                            isFullScreen = true
                                        },
                                        modifier = Modifier.size(80.dp),
                                        shape = IconButtonDefaults.shape(
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(24.dp),
                                                painter = painterResource(id = R.mipmap.fullscreen),
                                                contentDescription = "全屏"
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = "全屏",
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    AnimatedVisibility(visible = !isFullScreen) {
                        Column(
                            modifier = Modifier.focusGroup()
                        ) {
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(text = "剧集列表", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(10.dp))
                            VideoListControl(
                                videoListState = videoListState,
                                onItemClick = {
                                    viewModel.playVideo(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getIndex()
    }
    BackHandler(onBack = {
        if (isFullScreen) {
            isFullScreen = false
        } else {
            onBackClick()
        }
    })
}