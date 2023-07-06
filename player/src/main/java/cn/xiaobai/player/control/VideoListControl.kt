package cn.xiaobai.player.control

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import cn.xiaobai.player.R

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun VideoListControl(
    videoListState: VideoListState,
    onItemClick: (String) -> Unit,
) {
    TvLazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(videoListState.videoList) { index, item ->
            Button(
                modifier = Modifier.width(80.dp),
                shape = ButtonDefaults.shape(
                    shape = RoundedCornerShape(4.dp)
                ),
                scale = ButtonDefaults.scale(
                    focusedScale = 1f
                ),
                onClick = {
                    onItemClick(item.value.getValue("path"))
                    videoListState.selectedIndex = index
                }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (videoListState.selectedIndex == index) {
                        Icon(
                            modifier = Modifier.size(12.dp),
                            painter = painterResource(id = R.mipmap.playing),
                            contentDescription = "播放中",
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(text = item.key)
                }
            }
        }
    }
}