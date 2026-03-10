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
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.SubcomposeMeasureScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import kotlin.math.max

private const val CONTENT_WIDTH_FRACTION = .95f

private val CONTENT_TOP_PADDING = 20.dp
private val CONTENT_VERTICAL_PADDING = 24.dp
private val PLACEABLE_HORIZONTAL_PADDING = 40.dp

@Composable
@Suppress("LongMethod")
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

        val maxWidthConstraint = when {
            maxHeight < maxWidth && maxWidth > 800.dp -> 500.dp
            else -> 400.dp
        }

        val density = LocalDensity.current
        val systemBarInsets = WindowInsets.systemBars.asPaddingValues()

        val topInsetPx = with(density) {
            (systemBarInsets.calculateTopPadding() + CONTENT_TOP_PADDING)
                .roundToPx()
        }

        val bottomInsetPx = with(density) {
            systemBarInsets.calculateBottomPadding()
                .roundToPx()
        }

        val viewportHeightPx = with(density) { maxHeight.roundToPx() }
        val availableHeight = viewportHeightPx - topInsetPx - bottomInsetPx

        SubcomposeLayout(
            modifier = Modifier
                .widthIn(max = maxWidthConstraint)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) { constraints ->
            val childConstraints = Constraints(maxWidth = constraints.maxWidth)

            val headerPlaceables = measureSlotInColumn(
                identifier = "header",
                constraints = childConstraints,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PLACEABLE_HORIZONTAL_PADDING),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = header,
            )

            val contentPlaceables = measureSlotInColumn(
                identifier = "content",
                constraints = childConstraints,
                modifier = Modifier
                    .fillMaxWidth(CONTENT_WIDTH_FRACTION)
                    .padding(vertical = CONTENT_VERTICAL_PADDING)
                    .padding(horizontal = PLACEABLE_HORIZONTAL_PADDING),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content,
            )

            val footerPlaceables = measureSlotInColumn(
                identifier = "footer",
                constraints = childConstraints,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PLACEABLE_HORIZONTAL_PADDING),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = footer,
            )

            val headerHeight = headerPlaceables.sumOf { it.height }
            val contentHeight = contentPlaceables.sumOf { it.height }
            val footerHeight = footerPlaceables.sumOf { it.height }

            val layoutWidth = constraints.maxWidth
            val layoutHeight = headerHeight + contentHeight + footerHeight
            val needsScroll = layoutHeight > availableHeight

            if (needsScroll) {
                val scrollableLayoutHeight = topInsetPx + layoutHeight + bottomInsetPx
                val contentOffsetY = topInsetPx + headerHeight
                val footerOffsetY = topInsetPx + headerHeight + contentHeight

                layoutOnboardingScaffold(
                    screen = IntSize(width = layoutWidth, height = scrollableLayoutHeight),
                    items = mapOf(
                        headerPlaceables to fullWidthOffset(offsetY = topInsetPx),
                        contentPlaceables to centeredWidthOffset(offsetY = contentOffsetY),
                        footerPlaceables to fullWidthOffset(offsetY = footerOffsetY),
                    ),
                )
            } else {
                val footerOffsetY = viewportHeightPx - bottomInsetPx - footerHeight
                val contentSpaceAvailable = footerOffsetY - (topInsetPx + headerHeight)
                val contentSpaceOffset = max(0, (contentSpaceAvailable - contentHeight) / 2)
                val contentOffsetY = topInsetPx + headerHeight + contentSpaceOffset

                layoutOnboardingScaffold(
                    screen = IntSize(width = layoutWidth, height = viewportHeightPx),
                    items = mapOf(
                        headerPlaceables to fullWidthOffset(offsetY = topInsetPx),
                        contentPlaceables to centeredWidthOffset(offsetY = contentOffsetY),
                        footerPlaceables to fullWidthOffset(offsetY = footerOffsetY),
                    ),
                )
            }
        }
    }
}

private fun SubcomposeMeasureScope.measureSlotInColumn(
    identifier: String,
    constraints: Constraints,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
): List<Placeable> = subcompose(identifier) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content,
    )
}.map {
    it.measure(constraints)
}

private fun fullWidthOffset(
    offsetY: Int = 0,
): (Placeable) -> IntOffset = {
    IntOffset(x = 0, y = offsetY)
}

private fun centeredWidthOffset(
    offsetY: Int = 0,
): (Placeable) -> IntOffset = {
    IntOffset(x = 0, y = offsetY)
}

private fun MeasureScope.layoutOnboardingScaffold(
    screen: IntSize,
    items: Map<List<Placeable>, (Placeable) -> IntOffset>,
) = layout(screen.width, screen.height) {
    items.forEach { section ->
        section.key.forEach { placeable ->
            placeable.placeRelative(
                position = section.value.invoke(placeable),
            )
        }
    }
}
