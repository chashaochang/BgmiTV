package cn.xiaobai.bgmi.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cn.xiaobai.bgmi.navigation.navigateToDetail
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberBGmiAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): BGmiAppState {
    return remember(
        navController,
        coroutineScope
    ) {
        BGmiAppState(
            navController, coroutineScope ,false
        )
    }
}

@Stable
class BGmiAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    var isFullScreen: Boolean,
) {
      fun exitFullScreen(){
          isFullScreen = false
      }
}