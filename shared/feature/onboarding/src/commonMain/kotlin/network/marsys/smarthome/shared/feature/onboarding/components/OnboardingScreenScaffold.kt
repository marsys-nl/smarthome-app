package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import kotlin.math.max

private const val CONTENT_WIDTH_FRACTION = .95f

private val CONTENT_TOP_PADDING = 20.dp
private val CONTENT_VERTICAL_PADDING = 24.dp
private val PLACEABLE_HORIZONTAL_PADDING = 40.dp

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
        val maxWidthConstraint = when {
            maxHeight < maxWidth && maxWidth > 800.dp -> 500.dp
            else -> 400.dp
        }

        centeredSlot.invoke(this)

        val density = LocalDensity.current
        val systemBarInsets = WindowInsets.systemBars.asPaddingValues()
        val verticalPaddingPx = with(density) {
            val padding = systemBarInsets.calculateTopPadding() +
                systemBarInsets.calculateBottomPadding() +
                CONTENT_TOP_PADDING

            padding.roundToPx()
        }

        val viewportHeightPx = with(density) { maxHeight.roundToPx() }

        val scrollState = rememberScrollState()

        SubcomposeLayout(
            modifier = Modifier
                .widthIn(max = maxWidthConstraint)
                .fillMaxWidth()
                .verticalScroll(scrollState),
        ) { constraints ->
            val childConstraints = Constraints(maxWidth = constraints.maxWidth)
            val availableHeight = viewportHeightPx - verticalPaddingPx

            val headerPlaceables = subcompose("header") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PLACEABLE_HORIZONTAL_PADDING),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = header,
                )
            }.map { it.measure(childConstraints) }

            val contentPlaceables = subcompose("content") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(CONTENT_WIDTH_FRACTION)
                        .padding(vertical = CONTENT_VERTICAL_PADDING)
                        .padding(horizontal = PLACEABLE_HORIZONTAL_PADDING),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = content,
                )
            }.map { it.measure(childConstraints) }

            val footerPlaceables = subcompose("footer") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PLACEABLE_HORIZONTAL_PADDING),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = footer,
                )
            }.map { it.measure(childConstraints) }

            val headerHeight = headerPlaceables.sumOf { it.height }
            val contentHeight = contentPlaceables.sumOf { it.height }
            val footerHeight = footerPlaceables.sumOf { it.height }
            val totalContentHeight = headerHeight + contentHeight + footerHeight

            val needsScroll = totalContentHeight > availableHeight

            val topInsetPx = with(density) {
                (systemBarInsets.calculateTopPadding() + CONTENT_TOP_PADDING).roundToPx()
            }
            val bottomInsetPx = with(density) {
                systemBarInsets.calculateBottomPadding().roundToPx()
            }

            if (needsScroll) {
                val scrollableLayoutHeight = topInsetPx + totalContentHeight + bottomInsetPx
                val contentOffsetY = topInsetPx + headerHeight
                val footerOffsetY = topInsetPx + headerHeight + contentHeight

                layout(constraints.maxWidth, scrollableLayoutHeight) {
                    headerPlaceables.forEach {
                        it.placeRelative(x = 0, y = topInsetPx)
                    }

                    contentPlaceables.forEach {
                        val x = (constraints.maxWidth - it.width) / 2
                        it.placeRelative(x = x, y = contentOffsetY)
                    }

                    footerPlaceables.forEach {
                        it.placeRelative(x = 0, y = footerOffsetY)
                    }
                }
            } else {
                layout(constraints.maxWidth, viewportHeightPx) {
                    val footerOffsetY = viewportHeightPx - bottomInsetPx - footerHeight
                    val contentSpaceAvailable = footerOffsetY - (topInsetPx + headerHeight)
                    val contentSpaceOffset = max(0, (contentSpaceAvailable - contentHeight) / 2)
                    val contentOffsetY = topInsetPx + headerHeight + contentSpaceOffset

                    headerPlaceables.forEach {
                        it.placeRelative(x = 0, y = topInsetPx)
                    }

                    contentPlaceables.forEach {
                        val x = (constraints.maxWidth - it.width) / 2
                        it.placeRelative(x = x, y = contentOffsetY)
                    }

                    footerPlaceables.forEach {
                        it.placeRelative(x = 0, y = footerOffsetY)
                    }
                }
            }
        }
    }
}
