package com.example.bookdy.reader.tts

import com.example.bookdy.R
import org.readium.navigator.media.tts.TtsNavigator
import org.readium.navigator.media.tts.TtsNavigatorFactory
import org.readium.navigator.media.tts.android.AndroidTtsEngine
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.util.Error
import org.readium.r2.shared.util.ThrowableError
import com.example.bookdy.utils.UserError

@OptIn(ExperimentalReadiumApi::class)
sealed class TtsError(
    override val message: String,
    override val cause: Error? = null
) : Error {

    class ContentError(override val cause: TtsNavigator.Error.ContentError) :
        TtsError(cause.message, cause.cause)

    sealed class EngineError(override val cause: AndroidTtsEngine.Error) :
        TtsError(cause.message, cause.cause) {

        class Network(override val cause: AndroidTtsEngine.Error.Network) :
            EngineError(cause)

        class Other(override val cause: AndroidTtsEngine.Error) :
            EngineError(cause)
    }

    class ServiceError(val exception: Exception) :
        TtsError("Could not open session.", ThrowableError(exception))

    class Initialization(override val cause: TtsNavigatorFactory.Error) :
        TtsError(cause.message, cause)

    fun toUserError(): UserError = when (this) {
        is ContentError -> UserError(R.string.tts_error_other, cause = this)
        is EngineError.Network -> UserError(R.string.tts_error_network, cause = this)
        is EngineError.Other -> UserError(R.string.tts_error_other, cause = this)
        is Initialization -> UserError(R.string.tts_error_initialization, cause = this)
        is ServiceError -> UserError(R.string.error_unexpected, cause = this)
    }
}
