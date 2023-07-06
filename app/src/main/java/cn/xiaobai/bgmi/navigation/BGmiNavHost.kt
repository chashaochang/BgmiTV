package cn.xiaobai.bgmi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import cn.xiaobai.bgmi.ui.BGmiAppState

@Composable
fun BGmiNavHost(
    appState: BGmiAppState,
    modifier: Modifier = Modifier,
    startDestination: String = indexNavigationRoute
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        indexScreen(
            onItemClick = {
                navController.navigateToDetail(it)
            }
        )
        detailScreen(onBackClick = navController::popBackStack)
    }
}