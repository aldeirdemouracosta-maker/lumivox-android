package br.com.lumivox.app.core.accessibility

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale
import java.util.UUID

class TextToSpeechController(context: Context) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var ready = false
    private var pendingText: String? = null

    override fun onInit(status: Int) {
        val engine = textToSpeech ?: return
        ready = status == TextToSpeech.SUCCESS

        if (ready) {
            val result = engine.setLanguage(Locale("pt", "BR"))
            ready = result != TextToSpeech.LANG_MISSING_DATA &&
                result != TextToSpeech.LANG_NOT_SUPPORTED
            engine.setSpeechRate(0.92f)
        }

        if (ready) {
            pendingText?.let(::speak)
            pendingText = null
        }
    }

    fun speak(text: String) {
        val message = text.trim()
        if (message.isEmpty()) return

        if (!ready) {
            pendingText = message
            return
        }

        textToSpeech?.speak(
            message,
            TextToSpeech.QUEUE_FLUSH,
            null,
            UUID.randomUUID().toString()
        )
    }

    fun stop() {
        textToSpeech?.stop()
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        ready = false
    }
}

