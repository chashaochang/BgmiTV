package cn.xiaobai.player.control

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class VideoListState internal constructor(
    var selectedIndex:Int = 0,
    var videoList:List<Map.Entry<String, Map<String, String>>>
){

}

@Composable
fun rememberVideoListState(
    selectedIndex:Int = 0,
    videoList:List<Map.Entry<String, Map<String, String>>>
)= remember { VideoListState(selectedIndex,videoList) }
