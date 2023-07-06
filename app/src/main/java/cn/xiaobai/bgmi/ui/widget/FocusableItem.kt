package cn.xiaobai.bgmi.ui.widget

import androidx.compose.foundation.BorderStroke
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

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FocusableItem(
    onClick:() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (BoxScope.(Boolean) -> Unit)
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Surface(modifier = modifier
        .fillMaxWidth(),
        onClick = onClick,
        interactionSource = interactionSource,
        scale = ClickableSurfaceDefaults.scale(),
        colors = ClickableSurfaceDefaults.colors(
            focusedContainerColor = Color.White
        ),
        glow = ClickableSurfaceDefaults.glow(
            focusedGlow = Glow(
                elevation = 10.dp,
                elevationColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            )
        ),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.White
                ),
                shape = RoundedCornerShape(4.dp)
            )
        ),
        shape = ClickableSurfaceDefaults.shape(
            shape = RoundedCornerShape(4.dp)
        )
    ) {
        content(interactionSource.collectIsFocusedAsState().value)
    }
}