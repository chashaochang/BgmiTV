package cn.xiaobai.bgmi.feature.index

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.xiaobai.bgmi.bean.Data
import cn.xiaobai.bgmi.core.network.BGmiDispatchers
import cn.xiaobai.bgmi.core.network.BGmiNetworkDataSource
import cn.xiaobai.bgmi.core.network.Dispatcher
import cn.xiaobai.bgmi.core.network.retrofit.RetrofitBGmiNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dataSource: RetrofitBGmiNetwork,
    @Dispatcher(BGmiDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    var dataList: MutableStateFlow<List<Data>> = MutableStateFlow(ArrayList())

    suspend fun getIndex() {
        launch(dataSource.getIndex()) {
            if (it != null) {
                dataList.value = it.data
            }
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