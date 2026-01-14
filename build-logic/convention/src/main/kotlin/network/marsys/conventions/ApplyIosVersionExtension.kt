package network.marsys.conventions

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class ApplyIosVersionExtension @Inject constructor(
    objects: ObjectFactory
) {
    val marketingVersion: Property<String> = objects.property(String::class.java)
    val currentVersion: Property<Int> = objects.property(Int::class.java)
}
