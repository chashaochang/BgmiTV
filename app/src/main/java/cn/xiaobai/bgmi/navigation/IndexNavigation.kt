package cn.xiaobai.bgmi.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import cn.xiaobai.bgmi.feature.index.IndexRoute

const val indexNavigationRoute = "index_route"

fun NavController.navigateToIndex(navOptions: NavOptions? = null) {
    this.navigate(indexNavigationRoute, navOptions)
}

fun NavGraphBuilder.indexScreen(onItemClick: (Int) -> Unit) {
    composable(
        route = indexNavigationRoute,
    ) {
        IndexRoute(onItemClick = onItemClick)
    }
}