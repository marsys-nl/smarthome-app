package network.marsys.smarthome.shared.data.connection

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import network.marsys.smarthome.shared.domain.connection.ValidateBackendUriUseCase
import network.marsys.smarthome.shared.library.core.Result

val validateBackendUriUseCaseTest by testSuite(
    displayName = "Validate backend uri use case tests",
) {
    test(name = "Should return success if the uri returns a success response") {
        val client = mockClient { HttpStatusCode.OK }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com", apiKey = null))
            .isA<Result.Success<Unit>>()
    }

    test(name = "Should return failure if the uri has an unsupported scheme") {
        val client = mockClient { HttpStatusCode.OK }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("ftp://example.com", apiKey = null))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.InvalidUri>()
    }

    test(name = "Should return failure if the uri has invalid domain") {
        val client = mockClient { HttpStatusCode.OK }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://invaliddomain", apiKey = null))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.InvalidUri>()
    }

    test(name = "Should return failure if the uri returns a client error") {
        val client = mockClient { HttpStatusCode.NotFound }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com", apiKey = null))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.InvalidBackend>()
    }

    test(name = "Should return failure if the uri returns a server error") {
        val client = mockClient { HttpStatusCode.InternalServerError }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com", apiKey = null))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.InvalidBackend>()
    }

    test(name = "Should return failure if the request fails") {
        val client = mockFailingClient(IOException("Connection refused"))
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com", apiKey = null))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.Unreachable>()
    }

    test(name = "Should return failure if the uri is malformed") {
        val client = mockFailingClient(IllegalArgumentException("Invalid URL"))
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com", apiKey = null))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.Unreachable>()
    }

    test(name = "Should return failure if the uri is not valid") {
        val client = mockClient { HttpStatusCode.OK }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("", apiKey = null))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.InvalidUri>()
    }

    test(name = "Should return failure if unexpected content is served") {
        val client = mockClient(
            content = "",
            handler = { HttpStatusCode.OK },
        )
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("http://example.com", apiKey = null))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.InvalidBackend>()
    }

    test(name = "Should return failure if content is served from unexpected backend") {
        val client = mockClient(
            content = """
                {
                    "app": "UnknownApp",
                    "version": "1.0.0"
                }
            """.trimIndent(),
            handler = { HttpStatusCode.OK },
        )
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("http://example.com", apiKey = null))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.InvalidBackend>()
    }

    test(name = "Should return success if the uri returns a success response") {
        val client = mockClient { HttpStatusCode.OK }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com", apiKey = "valid_api_key"))
            .isA<Result.Success<Unit>>()
    }

    test(name = "Should return failure if api key is required but not provided") {
        val client = mockClient(
            content = "".trimIndent(),
            handler = { HttpStatusCode.Unauthorized },
        )
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("http://example.com", apiKey = null))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.Unauthenticated>()
    }

    test(name = "Should return failure if api key is required but other is provided") {
        val client = mockClient(
            content = "".trimIndent(),
            handler = { HttpStatusCode.Unauthorized },
        )
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("http://example.com", apiKey = "invalid_api_key"))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.Unauthenticated>()
    }
}

private const val HEALTH_ENDPOINT_RESPONSE_DEFAULT = """
    {
        "app": "SmartHomeBackend",
        "version": "1.0.0"
    }
"""

private fun mockClient(
    content: String = HEALTH_ENDPOINT_RESPONSE_DEFAULT,
    handler: () -> HttpStatusCode,
): HttpClient =
    HttpClient(
        engine = MockEngine {
            respond(
                content = content,
                status = handler.invoke(),
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        },
    ) {
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                },
            )
        }
    }

private fun mockFailingClient(exception: Exception): HttpClient =
    HttpClient(
        engine = MockEngine {
            throw exception
        },
    )
