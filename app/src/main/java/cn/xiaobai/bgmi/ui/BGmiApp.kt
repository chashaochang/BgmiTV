package cn.xiaobai.bgmi.ui

import androidx.compose.runtime.Composable
import cn.xiaobai.bgmi.navigation.BGmiNavHost

@Composable
fun BGmiApp(appState: BGmiAppState = rememberBGmiAppState()) {
    BGmiNavHost(appState = appState)
}