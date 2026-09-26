package network.marsys.smarthome.shared.data.authentication

import network.marsys.smarthome.shared.data.authentication.ports.outbound.Authenticator
import network.marsys.smarthome.shared.domain.authentication.AuthenticateUseCase
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.mapFailure

class AuthenticateUseCaseImpl(
    private val authenticator: Authenticator,
) : AuthenticateUseCase {
    override suspend fun invoke(
        issuer: String,
        clientIdentifier: String,
    ): Result<Unit, AuthenticateUseCase.Reason> =
        authenticator
            .authenticate(
                issuer = issuer,
                clientIdentifier = clientIdentifier,
            )
            .mapFailure { reason ->
                when (reason) {
                    is Authenticator.Reason.Cancelled ->
                        AuthenticateUseCase.Reason.Cancelled

                    is Authenticator.Reason.Failed ->
                        AuthenticateUseCase.Reason.Failed

                    is Authenticator.Reason.Unavailable ->
                        AuthenticateUseCase.Reason.Unavailable
                }
            }
}
