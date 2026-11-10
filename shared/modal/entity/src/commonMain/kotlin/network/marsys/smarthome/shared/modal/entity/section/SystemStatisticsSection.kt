package network.marsys.smarthome.shared.modal.entity.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import network.marsys.smarthome.domain.entity.System
import network.marsys.smarthome.shared.domain.entity.FixedUnits
import network.marsys.smarthome.shared.domain.entity.TimeAgoKind
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.IconCard
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Clock
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.i18n.timeAgo
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@Composable
fun SystemStatisticsSection(
    state: System.State.Known,
    modifier: Modifier = Modifier,
) {
    val uptime by produceState(
        initialValue = Clock.System.now() - state.startedAt.application,
        key1 = state.startedAt.application,
    ) {
        while (true) {
            value = Clock.System.now() - state.startedAt.application
            delay(1.seconds)
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement
            .spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconCard(
            icon = Icons.Clock,
            colors = CardDefaults.colors(),
            size = 36.dp,
        )

        Text(
            text = "Uptime",
            lineHeight = 20.sp,
            fontSize = 14.sp,
            color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
        )

        Text(
            text = timeAgo(
                duration = uptime,
                display = timeAgoDisplay.displayFor(duration = uptime),
                kind = TimeAgoKind.Raw,
            ),
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.End,
            lineHeight = 20.sp,
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
        )
    }
}

private val timeAgoDisplay = FixedUnits(
    units = listOf(
        DurationUnit.DAYS,
        DurationUnit.HOURS,
        DurationUnit.MINUTES,
        DurationUnit.SECONDS,
    ),
)
