package network.marsys.smarthome.shared.data.connection

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isFalse
import dev.nmarsman.expect.assertions.isTrue
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException

private fun mockClient(handler: () -> HttpStatusCode): HttpClient =
    HttpClient(
        engine = MockEngine {
            respond(content = "", status = handler.invoke())
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
    test(name = "Should return true if the uri returns a success response") {
        val client = mockClient { HttpStatusCode.OK }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com"))
            .isTrue()
    }

    test(name = "Should return true if the uri returns an other 2xx status code") {
        val client = mockClient { HttpStatusCode.NoContent }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com"))
            .isTrue()
    }

    test(name = "Should return false if the uri returns a client error") {
        val client = mockClient { HttpStatusCode.NotFound }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com"))
            .isFalse()
    }

    test(name = "Should return false if the uri returns a server error") {
        val client = mockClient { HttpStatusCode.InternalServerError }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com"))
            .isFalse()
    }

    test(name = "Should return false if the request fails") {
        val client = mockFailingClient(IOException("Connection refused"))
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com"))
            .isFalse()
    }

    test(name = "Should return false if the uri is malformed") {
        val client = mockFailingClient(IllegalArgumentException("Invalid URL"))
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke("https://example.com"))
            .isFalse()
    }

    test(name = "Should return false if the uri is not valid") {
        val client = mockClient { HttpStatusCode.OK }
        val useCase = ValidateBackendUriUseCaseImpl(client)

        expectThat(subject = useCase.invoke(""))
            .isFalse()
    }
}
