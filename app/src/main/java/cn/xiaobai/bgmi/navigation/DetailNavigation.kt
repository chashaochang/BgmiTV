package cn.xiaobai.bgmi.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cn.xiaobai.bgmi.feature.detail.DetailRoute
import cn.xiaobai.bgmi.ui.BGmiAppState

const val VIDEO_ID = "videoId"
const val detailRoute = "detail_route"

fun NavController.navigateToDetail(id: Int) {
    this.navigate("$detailRoute/$id")
}

fun NavGraphBuilder.detailScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = "$detailRoute/{$VIDEO_ID}",
        arguments = listOf(
            navArgument(VIDEO_ID) {
                type = NavType.IntType
            }
        )
    ) {
        DetailRoute(onBackClick = onBackClick)
    }
}