package cn.xiaobai.bgmi.core.network

import cn.xiaobai.bgmi.bean.BgmiIndexData

interface BGmiNetworkDataSource {

    suspend fun getIndex(): BgmiIndexData?
}