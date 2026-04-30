package network.marsys.smarthome.shared.library.network

import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single { HttpClient(engineFactory = httpClientEngine()) }
}
