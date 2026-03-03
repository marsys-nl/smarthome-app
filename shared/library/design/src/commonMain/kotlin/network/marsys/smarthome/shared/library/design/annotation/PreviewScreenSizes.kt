package network.marsys.smarthome.shared.library.design.annotation

import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Devices.FOLDABLE
import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Phone",
    group = "Screen sizes",
    device = PHONE,
    locale = "en",
    showSystemUi = true,
)
@Preview(
    name = "Phone - Landscape",
    group = "Screen sizes",
    device = "$PHONE,orientation=landscape,dpi=420",
    locale = "en",
    showSystemUi = true,
)
@Preview(
    name = "Unfolded Foldable",
    group = "Screen sizes",
    device = FOLDABLE,
    locale = "en",
    showSystemUi = true,
)
@Preview(
    name = "Tablet",
    group = "Screen sizes",
    device = TABLET,
    locale = "en",
    showSystemUi = true,
)
@Preview(
    name = "Tablet - Landscape",
    group = "Screen sizes",
    device = "$TABLET,dpi=240,orientation=landscape",
    locale = "en",
    showSystemUi = true,
)
@Preview(
    name = "Desktop",
    group = "Screen sizes",
    device = "$DESKTOP,orientation=landscape",
    locale = "en",
    showSystemUi = true,
)
annotation class PreviewScreenSizes
