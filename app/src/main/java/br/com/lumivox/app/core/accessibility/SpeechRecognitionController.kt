package br.com.lumivox.app.core.accessibility

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class SpeechRecognitionController(
    context: Context,
    private val listener: Listener
) : RecognitionListener {

    interface Listener {
        fun onListeningStarted()
        fun onListeningFinished()
        fun onResult(text: String)
        fun onError(message: String)
    }

    private val appContext = context.applicationContext
    private val recognizer: SpeechRecognizer? =
        if (SpeechRecognizer.isRecognitionAvailable(appContext)) {
            SpeechRecognizer.createSpeechRecognizer(appContext).also {
                it.setRecognitionListener(this)
            }
        } else {
            null
        }

    fun start() {
        val service = recognizer
        if (service == null) {
            listener.onError("O reconhecimento de voz não está disponível neste aparelho.")
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale com o LUMIVOX")
        }
        service.startListening(intent)
    }

    fun stop() {
        recognizer?.stopListening()
    }

    fun destroy() {
        recognizer?.cancel()
        recognizer?.destroy()
    }

    override fun onReadyForSpeech(params: Bundle?) = listener.onListeningStarted()

    override fun onResults(results: Bundle?) {
        val text = results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.firstOrNull()
            .orEmpty()
        listener.onListeningFinished()
        listener.onResult(text)
    }

    override fun onPartialResults(partialResults: Bundle?) = Unit
    override fun onBeginningOfSpeech() = Unit
    override fun onRmsChanged(rmsdB: Float) = Unit
    override fun onBufferReceived(buffer: ByteArray?) = Unit
    override fun onEndOfSpeech() = Unit
    override fun onEvent(eventType: Int, params: Bundle?) = Unit

    override fun onError(error: Int) {
        val message = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Não foi possível acessar o áudio."
            SpeechRecognizer.ERROR_CLIENT -> "A escuta foi interrompida."
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permissão do microfone negada."
            SpeechRecognizer.ERROR_NETWORK,
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Falha de rede durante o reconhecimento de voz."
            SpeechRecognizer.ERROR_NO_MATCH -> "Não consegui compreender a fala. Tente novamente."
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "O reconhecimento de voz está ocupado."
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Nenhuma fala foi detectada."
            else -> "Ocorreu um erro no reconhecimento de voz."
        }
        listener.onError(message)
    }
}

