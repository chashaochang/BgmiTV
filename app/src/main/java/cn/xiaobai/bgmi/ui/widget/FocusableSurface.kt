package cn.xiaobai.bgmi.ui.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Glow
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FocusableSurface(
    isFullScreen: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (BoxScope.() -> Unit)
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .indication(interactionSource, LocalIndication.current)
            .focusGroup()
        ,
        onClick = onClick,
        interactionSource = interactionSource,
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1f),
        colors = ClickableSurfaceDefaults.colors(
            focusedContainerColor = Color.White
        ),
        glow = ClickableSurfaceDefaults.glow(
            focusedGlow = Glow(
                elevation = 20.dp,
                elevationColor = MaterialTheme.colorScheme.surface.copy(alpha = 1f)
            )
        ),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(
                    width = if(isFullScreen) 0.dp else 2.dp,
                    color = Color.White
                ),
                shape = RoundedCornerShape(if(isFullScreen) 0.dp else 4.dp)
            )
        ),
        shape = ClickableSurfaceDefaults.shape(
            shape = RoundedCornerShape(if(isFullScreen) 0.dp else 4.dp)
        )
    ) {
        content()
    }
}