package network.marsys.smarthome.shared.feature.dashboard.sections

import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.GridConfigurationScope
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalGridApi::class)
internal val dashboardSectionGridConfig: GridConfigurationScope.() -> Unit = {
    val maxWidthDp = constraints.maxWidth.toDp()
    val columnsCount = (maxWidthDp / 150.dp).toInt()
        .coerceIn(1..4)

    repeat(columnsCount) {
        column(minmax(150.dp, 1.fr))
    }

    gap(16.dp)
}
