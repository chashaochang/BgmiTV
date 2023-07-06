package cn.xiaobai.bgmi.feature.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import cn.xiaobai.bgmi.bean.Data
import cn.xiaobai.bgmi.core.network.BGmiDispatchers.IO
import cn.xiaobai.bgmi.core.network.BGmiNetworkDataSource
import cn.xiaobai.bgmi.core.network.Dispatcher
import cn.xiaobai.bgmi.core.network.retrofit.BGmiBaseUrl
import cn.xiaobai.bgmi.core.network.retrofit.RetrofitBGmiNetwork
import cn.xiaobai.bgmi.navigation.VIDEO_ID
import cn.xiaobai.player.domain.TLPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dataSource: RetrofitBGmiNetwork,
    val player: TLPlayer,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val videoId = savedStateHandle.getStateFlow<Int?>(key = VIDEO_ID, null)
    var dataList: MutableStateFlow<List<Data>> = MutableStateFlow(ArrayList())
    val playList: MutableStateFlow<List<Map.Entry<String, Map<String, String>>>> =
        MutableStateFlow(ArrayList())

    fun playVideo(path: String) {
        player.prepare("$BGmiBaseUrl/bangumi$path",true)
        Log.i("TAG", "playVideo: $path")
        player.play()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    suspend fun getIndex() {
        try {
            launch(dataSource.getIndex()) {
                if (it != null) {
                    dataList.value = it.data
                    val video = dataList.value.find { e -> e.id == videoId.value }
                    video?.let {
                        playList.value =
                            video.player.entries.sortedBy { entry -> entry.key.toInt() }.toList()
//                        playList.value.forEach {
//                            player.a.addMediaItem(
//                                MediaItem.fromUri(
//                                    "$BGmiBaseUrl/bangumi/" + it.value.getValue(
//                                        "path"
//                                    )
//                                )
//                            )
//                        }
                        playVideo(
                            playList.value[0].value.getValue(
                                "path"
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("getIndex", e.toString())
//            if (e is ApiException) {
//                if (e.code != 4004)
//                    alertState.value = e.message
//            } else {
//                alertState.value = e.message.toString()
//            }
        }
    }

    fun <T> launch(
        value: T?,
        action: suspend (value: T?) -> Unit
    ) {
        viewModelScope.launch {
            flow {
                emit(value)
            }
                .flowOn(ioDispatcher)
                .collectLatest {
                    action(value)
                }
        }
    }

}