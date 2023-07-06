package cn.xiaobai.player.domain.state

interface PlayerStateListener {
    fun on(state: PlayerState)
}