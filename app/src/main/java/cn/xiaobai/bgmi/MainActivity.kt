package cn.xiaobai.bgmi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cn.xiaobai.bgmi.ui.BGmiApp
import cn.xiaobai.bgmi.ui.rememberBGmiAppState
import cn.xiaobai.bgmi.ui.theme.BGmiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appState = rememberBGmiAppState()
            BGmiTheme {
                BGmiApp(appState)
            }
        }
    }

}