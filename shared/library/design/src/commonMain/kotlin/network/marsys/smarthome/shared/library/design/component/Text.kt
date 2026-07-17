package network.marsys.smarthome.shared.library.design.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.apply
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.composeunstyled.LocalTextStyle
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import com.composeunstyled.Text as UnstyledText

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign = TextAlign.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    fontSize: TextUnit = style.fontSize,
    letterSpacing: TextUnit = style.letterSpacing,
    fontWeight: FontWeight? = style.fontWeight,
    color: Color = Color.Unspecified,
    fontFamily: FontFamily? = style.fontFamily,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    autoSize: TextAutoSize? = null,
) = UnstyledText(
    text = text,
    modifier = modifier,
    style = style,
    textAlign = textAlign,
    lineHeight = lineHeight,
    fontSize = fontSize,
    letterSpacing = letterSpacing,
    fontWeight = fontWeight,
    color = color.takeOrElse { LocalContentColor.current },
    fontFamily = fontFamily,
    singleLine = singleLine,
    minLines = minLines,
    maxLines = maxLines,
    onTextLayout = onTextLayout,
    overflow = overflow,
    autoSize = autoSize,
)

@Composable
@OptIn(ExperimentalFoundationStyleApi::class)
fun Text(
    text: String,
    style: Style,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val styleState = rememberUpdatedStyleState(interactionSource)
    val mergedStyle = TextDefaults.base then style

    BasicText(
        text = text,
        modifier = modifier
            .styleable(styleState, mergedStyle),
        minLines = minLines,
        maxLines = maxLines,
    )
}

@OptIn(ExperimentalFoundationStyleApi::class)
object TextDefaults {
    internal val base = Style {
        contentColor(LocalContentColor.currentValue)
        fontSize(14.sp)
        fontWeight(FontWeight.Normal)
    }

    val title = Style {
        lineHeight(32.sp)
        fontSize(24.sp)

        apply(TextStyles.bold)
    }

    val sectionHeader = Style {
        lineHeight(28.sp)
        fontSize(18.sp)

        apply(TextStyles.semiBold)
    }

    val header = Style {
        lineHeight(24.sp)
        fontSize(16.sp)

        apply(TextStyles.semiBold)
    }

    val description = Style {
        contentColor(LocalColorScheme.currentValue[ColorKeyToken.TextSecondary])
        lineHeight(20.sp)
        fontSize(14.sp)
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
object TextStyles {
    val semiBold = Style {
        fontWeight(FontWeight.W600)
    }

    val bold = Style {
        fontWeight(FontWeight.W700)
    }

    val centered = Style {
        textAlign(TextAlign.Center)
    }
}
