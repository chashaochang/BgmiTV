package cn.xiaobai.player.control

import androidx.annotation.IntRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PlayerMenuState internal constructor(
    val hideSeconds: Int = 3,
    val coroutineScope: CoroutineScope,
) {
    private var isInit = false
    var isDisplayed by mutableStateOf(false)
    private val countDownTimer = MutableStateFlow(value = hideSeconds)

    private fun init() {
        coroutineScope.launch {
            countDownTimer.collectLatest { time ->
                if (time > 0) {
                    isDisplayed = true
                    delay(1000)
                    countDownTimer.emit(countDownTimer.value - 1)
                } else {
                    isDisplayed = false
                }
            }
        }
    }

    fun showMenu(seconds: Int = hideSeconds) {
        if (!isInit) {
            init()
        } else {
            coroutineScope.launch {
                countDownTimer.emit(seconds)
            }
        }
    }
}

/**
 * Create and remember a [PlayerControlsState] instance. Useful when trying to control the state of
 * the [PlayerControls]-related composable.
 * @return A remembered instance of [PlayerControlsState].
 * @param hideSeconds How many seconds should the controls be visible before being hidden.
 * */
@Composable
fun rememberPlayerMenuState(
    @IntRange(from = 0) hideSeconds: Int = 2,
    coroutineScope: CoroutineScope
) =
    remember { PlayerMenuState(hideSeconds = hideSeconds, coroutineScope = coroutineScope) }