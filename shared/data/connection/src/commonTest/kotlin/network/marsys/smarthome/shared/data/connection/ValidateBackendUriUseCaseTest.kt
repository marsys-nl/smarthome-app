package network.marsys.smarthome.shared.data.connection

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import network.marsys.smarthome.shared.domain.connection.ValidateBackendUriUseCase
import network.marsys.smarthome.shared.library.core.Result

private fun mockClient(
    content: String = "",
    handler: () -> HttpStatusCode,
): HttpClient =
    HttpClient(
        engine = MockEngine {
            respond(
                content = content,
                status = handler.invoke(),
            )
        },
    )

private fun mockFailingClient(exception: Exception): HttpClient =
    HttpClient(
        engine = MockEngine {
            throw exception
        },
    )

val validateBackendUriUseCaseTest by testSuite(
    displayName = "Validate backend uri use case tests",
) {
    test(name = "Should return success if the uri returns a success response") {
        val client = mockClient { HttpStatusCode.OK }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com"))
            .isA<Result.Success<Unit>>()
    }

    test(name = "Should return failure if the uri returns an other 2xx status code") {
        val client = mockClient { HttpStatusCode.NoContent }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com"))
            .isA<Result.Success<Unit>>()
    }

    test(name = "Should return failure if the uri has an unsupported scheme") {
        val client = mockClient { HttpStatusCode.OK }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("ftp://example.com"))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.InvalidUri>()
    }

    test(name = "Should return failure if the uri has invalid domain") {
        val client = mockClient { HttpStatusCode.OK }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://invaliddomain"))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.InvalidUri>()
    }

    test(name = "Should return failure if the uri returns a client error") {
        val client = mockClient { HttpStatusCode.NotFound }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com"))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.InvalidBackend>()
    }

    test(name = "Should return failure if the uri returns a server error") {
        val client = mockClient { HttpStatusCode.InternalServerError }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com"))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.InvalidBackend>()
    }

    test(name = "Should return failure if the request fails") {
        val client = mockFailingClient(IOException("Connection refused"))
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com"))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.Unreachable>()
    }

    test(name = "Should return failure if the uri is malformed") {
        val client = mockFailingClient(IllegalArgumentException("Invalid URL"))
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com"))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.Unreachable>()
    }

    test(name = "Should return failure if the uri is not valid") {
        val client = mockClient { HttpStatusCode.OK }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke(""))
            .isA<Result.Failure<ValidateBackendUriUseCase.Reason>>()
            .get(Result.Failure<ValidateBackendUriUseCase.Reason>::value)
            .isA<ValidateBackendUriUseCase.Reason.InvalidUri>()
    }
}
