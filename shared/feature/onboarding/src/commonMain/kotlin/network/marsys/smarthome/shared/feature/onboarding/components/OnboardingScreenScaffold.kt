package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

@Composable
fun OnboardingScreenScaffold(
    modifier: Modifier = Modifier,
    backgroundColor: Color = LocalColorScheme.current[ColorKeyToken.BackgroundPrimary],
    centeredSlot: @Composable BoxWithConstraintsScope.() -> Unit = {},
    header: @Composable ColumnScope.() -> Unit = {},
    footer: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {},
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.TopCenter,
    ) {
        centeredSlot.invoke(this)

        SubcomposeLayout(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth(),
        ) { constraints ->
            val measureConstraints = Constraints(maxWidth = constraints.maxWidth)

            val headerPlaceables = subcompose("header") {
                Column(content = header)
            }.map { it.measure(measureConstraints) }

            val contentPlaceables = subcompose("content") {
                Column(content = content)
            }.map { it.measure(measureConstraints) }

            val footerPlaceables = subcompose("footer") {
                Column(content = footer)
            }.map { it.measure(measureConstraints) }

            val headerHeight = headerPlaceables.sumOf { it.height }
            val contentHeight = contentPlaceables.sumOf { it.height }
            val footerHeight = footerPlaceables.sumOf { it.height }
            val totalHeight = headerHeight + contentHeight + footerHeight

            val needsScroll = totalHeight > constraints.maxHeight

            val measurables = subcompose("final") {
                if (needsScroll) {
                    ScrollableLayout(
                        header = header,
                        footer = footer,
                        content = content,
                    )
                } else {
                    FittedLayout(
                        header = header,
                        footer = footer,
                        content = content,
                    )
                }
            }

            val placeables = measurables
                .map { it.measure(constraints) }

            layout(constraints.maxWidth, constraints.maxHeight) {
                placeables.forEach {
                    it.placeRelative(x = 0, y = 0)
                }
            }
        }
    }
}

@Composable
private fun ScrollableLayout(
    header: @Composable ColumnScope.() -> Unit,
    footer: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .safeDrawingPadding()
            .padding(top = 20.dp)
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        header.invoke(this)
        content.invoke(this)
        footer.invoke(this)
    }
}

@Composable
private fun FittedLayout(
    header: @Composable ColumnScope.() -> Unit,
    footer: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .padding(top = 20.dp)
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        header.invoke(this)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            content.invoke(this@Column)
        }

        footer.invoke(this)
    }
}
