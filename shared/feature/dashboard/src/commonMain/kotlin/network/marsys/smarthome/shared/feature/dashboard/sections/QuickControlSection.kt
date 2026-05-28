package network.marsys.smarthome.shared.feature.dashboard.sections

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.Res
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.quick_control_section_title
import network.marsys.smarthome.shared.feature.dashboard.sections.controls.GroupEntitiesButton
import org.jetbrains.compose.resources.stringResource

@Composable
fun ColumnScope.QuickControlSection(
    modifier: Modifier = Modifier,
) {
    var groupByType by remember { mutableStateOf(false) }

    SectionHeader(
        title = stringResource(Res.string.quick_control_section_title),
        modifier = modifier,
        right = {
            GroupEntitiesButton(
                groupByType = groupByType,
                onClick = {
                    groupByType = !groupByType
                },
            )
        },
    )
}
