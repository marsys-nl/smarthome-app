package network.marsys.smarthome.shared.library.design.annotation

import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Devices.FOLDABLE
import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Phone",
    device = PHONE,
    showSystemUi = true,
)
@Preview(
    name = "Phone - Landscape",
    device = "$PHONE,orientation=landscape,dpi=420",
    showSystemUi = true,
)
@Preview(
    name = "Unfolded Foldable",
    device = FOLDABLE,
    showSystemUi = true,
)
@Preview(
    name = "Tablet",
    device = TABLET,
    showSystemUi = true,
)
@Preview(
    name = "Tablet - Landscape",
    device = "$TABLET,dpi=240,orientation=landscape",
    showSystemUi = true,
)
@Preview(
    name = "Desktop",
    device = "$DESKTOP,orientation=landscape",
    showSystemUi = true,
)
annotation class PreviewScreenSizes
