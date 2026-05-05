package network.marsys.smarthome.shared.library.core

/**
 * A sealed interface representing a result of an operation that can either
 * be a success with a value of type [T], or a failure with a value of type [E].
 */
sealed interface Result<out T, out E> {
    data class Success<T>(val value: T) : Result<T, Nothing>
    data class Failure<E>(val value: E) : Result<Nothing, E>

    companion object {
        /**
         * Factory methods for creating success result.
         */
        fun <T> succeed(with: T): Result<T, Nothing> =
            Success(with)

        /**
         * Factory methods for creating failure result.
         */
        fun <E> fail(with: E): Result<Nothing, E> =
            Failure(with)
    }
}

inline fun <T, E> Result<T, E>.onFailure(action: (Result.Failure<E>) -> Nothing): T =
    when (this) {
        is Result.Success -> value
        is Result.Failure -> action(this)
    }

inline fun <T, E> Result<T, E>.onSuccess(action: (Result.Success<T>) -> Nothing): E =
    when (this) {
        is Result.Success -> action(this)
        is Result.Failure -> value
    }
