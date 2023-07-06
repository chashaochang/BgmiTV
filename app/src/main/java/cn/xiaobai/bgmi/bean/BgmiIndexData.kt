package cn.xiaobai.bgmi.bean

data class BgmiIndexData(
    val danmaku_api: String,
    val `data`: List<Data>,
    val frontend_version: List<String>,
    val lang: String,
    val latest_version: String,
    val status: String,
    val version: String
)

data class Data(
    val bangumi_name: String,
    val cover: String,
    val episode: Int,
    val id: Int,
    val name: String,
    val player: Map<String, Map<String, String>>,
    val status: Int,
    val update_time: String,
    val updated_time: Int
)