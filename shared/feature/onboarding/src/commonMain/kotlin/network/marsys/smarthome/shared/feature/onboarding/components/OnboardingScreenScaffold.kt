package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

@Composable
fun OnboardingScreenScaffold(
    modifier: Modifier = Modifier,
    backgroundColor: Color = LocalColorScheme.current[ColorKeyToken.BackgroundPrimary],
    centeredSlot: @Composable BoxScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(all = 40.dp)
                .widthIn(max = 400.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            content.invoke(this)
        }

        centeredSlot.invoke(this)
    }
}
