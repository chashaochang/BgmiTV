package cn.xiaobai.player.control

import android.view.KeyEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import kotlinx.coroutines.delay

@OptIn(
    ExperimentalTvMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun PlayerMenu(
    modifier: Modifier = Modifier,
    state: PlayerMenuState = rememberPlayerMenuState(coroutineScope = rememberCoroutineScope()),
    videoListState: VideoListState,
    onVideoChange: (Int) -> Unit,
    onVideoSpeedChange: (Float) -> Unit,
    onMenuTop:()->Unit
) {
    val focusManager = LocalFocusManager.current
    val (first, second, third) = remember { FocusRequester.createRefs() }
    LaunchedEffect(state.isDisplayed) {
        if (state.isDisplayed) {
            delay(300)
            first.requestFocus()
        }
    }
    val hasNext = videoListState.selectedIndex < videoListState.videoList.size - 1
    AnimatedVisibility(
        modifier = modifier.fillMaxWidth(),
        visible = state.isDisplayed,
        enter = expandVertically { it },
        exit = shrinkVertically { it }
    ) {
        TvLazyColumn(
            modifier = modifier
                .height(170.dp)
                .fillMaxWidth()
                .focusable(false)
                .background(color = Color(0, 0, 0, 100))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                FocusedShowMenu(
                    modifier = Modifier
                        .padding(0.dp)
                        .focusRequester(first)
                        .focusProperties {
                            next = second
                            down = second
                        }
                        .onKeyEvent {
                            if (it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN && it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                state.isDisplayed = false
                                onMenuTop()
                            }
                            false
                        },
                    title = "常用功能",
                ) {
                    TvLazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (hasNext) {
                            item {
                                MenuButton(
                                    modifier = Modifier,
                                    onClick = {
                                        videoListState.selectedIndex++
                                        onVideoChange(videoListState.selectedIndex)
                                        state.isDisplayed = false

                                    }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(4.dp)
                                    ) {
                                        Text(
                                            text = "下一集",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "第" + videoListState.videoList[videoListState.selectedIndex + 1].key + "集",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            MenuButton(
                                modifier = Modifier,
                                onClick = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                    focusManager.moveFocus(FocusDirection.Down)
                                }) {
                                Column(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .focusable(false)
                                ) {
                                    Text(
                                        text = "倍速播放",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row {
                                        Text(
                                            text = "1.0倍",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Icon(
                                            Icons.Rounded.KeyboardArrowRight,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                FocusedShowMenu(
                    modifier = Modifier
                        .focusRequester(second)
                        .focusProperties {
                            next = third
                            down = third
                        },
                    title = "剧集列表",
                ) {
                    VideoListControl(videoListState, onItemClick = {})
                }
            }
            item {
                FocusedShowMenu(
                    modifier = Modifier
                        .padding(0.dp)
                        .focusRequester(third),
                    title = "倍速播放",
                ) {
                    var selectIndex by remember {
                        mutableIntStateOf(0)
                    }
                    val speedList = listOf(1.0f, 1.5f, 2.0f)
                    TvLazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(speedList) { index, item ->
                            MenuButton(
                                modifier = Modifier.padding(0.dp),
                                onClick = {
                                    selectIndex = index
                                    onVideoSpeedChange(speedList[index])
                                }) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    if (index == selectIndex) {
                                        Icon(
                                            modifier = Modifier.size(12.dp),
                                            imageVector = Icons.Rounded.CheckCircle,
                                            contentDescription = ""
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = item.toString() + "倍",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FocusedShowMenu(
    modifier: Modifier,
    title: String,
    content: @Composable BoxScope.() -> Unit
) {
    var hasFocus by remember {
        mutableStateOf(false)
    }
    Column(
        modifier
            .fillMaxWidth()
            .onFocusChanged {
                //焦点在子控件
                hasFocus = it.hasFocus
            }
            .focusGroup()
    ) {
        Text(
            text = title,
            style = if (hasFocus) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = if (hasFocus) Modifier
                .wrapContentHeight()
                .fillMaxWidth() else Modifier
                .fillMaxWidth()
                .height(1.dp)
                .alpha(0f)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MenuButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (RowScope.() -> Unit)
) {
    Button(
        modifier = modifier
            .size(120.dp, 66.dp)
            .padding(0.dp),
        onClick = onClick,
        scale = ButtonDefaults.scale(
            focusedScale = 1f
        ),
        shape = ButtonDefaults.shape(
            shape = RoundedCornerShape(4.dp)
        ),
    ) {
        content()
    }
}