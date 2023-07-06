package cn.xiaobai.bgmi.feature.index

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.tv.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvGridItemSpan
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.items
import androidx.tv.material3.MaterialTheme
import cn.xiaobai.bgmi.bean.Data
import cn.xiaobai.bgmi.core.network.retrofit.BGmiBaseUrl
import cn.xiaobai.bgmi.ui.theme.colorBackground
import cn.xiaobai.bgmi.ui.widget.FocusableItem
import coil.compose.AsyncImage

@Composable
fun IndexRoute(
    onItemClick: (Int) -> Unit,
    viewModel: IndexViewModel = hiltViewModel(),
) {
    IndexScreen(indexViewModel = viewModel, onItemClick = onItemClick)
}

@Composable
internal fun IndexScreen(
    indexViewModel: IndexViewModel,
    onItemClick: (Int) -> Unit,
) {
    val dataList = indexViewModel.dataList.collectAsState().value
    TvLazyVerticalGrid(
        modifier = Modifier.fillMaxSize().background(colorBackground),
        columns = TvGridCells.Fixed(8),
        contentPadding = PaddingValues(24.dp)
    ) {
        item(span = {
            TvGridItemSpan(8)
        }) {
            GridHeader()
        }
        items(dataList) {
            VideoCard(it, onClick = onItemClick)
        }
    }
    LaunchedEffect(Unit) {
        indexViewModel.getIndex()
    }
}

@Composable
fun GridHeader() {
    Text(
        text = "我的订阅",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 24.dp, start = 8.dp)
    )
}

@Composable
fun VideoCard(
    data: Data,
    onClick: (Int) -> Unit,
) {
    FocusableItem(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = {
            onClick(data.id)
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.BottomCenter) {
                AsyncImage(
                    model = BGmiBaseUrl + data.cover,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4 / 5f)
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0, 0, 0, 200))
                        .padding(0.dp, 4.dp),
                    text = if (data.episode == 0) "暂无更新" else "更新至" + data.episode + "集",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            Text(
                modifier = Modifier.padding(0.dp, 4.dp),
                text = data.name,
                maxLines = 1,
                color = if (it) Color.Black else Color.White,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}