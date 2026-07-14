package br.com.lumivox.app.feature.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun beginListening() {
        _uiState.update {
            it.copy(
                status = SystemStatus.LISTENING,
                statusMessage = "Ouvindo. Fale agora.",
                isListening = true
            )
        }
    }

    fun endListening() {
        _uiState.update {
            it.copy(
                status = SystemStatus.READY,
                statusMessage = "Sistema pronto",
                isListening = false
            )
        }
    }

    fun onSpeechResult(text: String) {
        val normalized = text.trim()
        _uiState.update {
            it.copy(
                status = SystemStatus.READY,
                statusMessage = if (normalized.isEmpty()) {
                    "Nenhuma fala reconhecida"
                } else {
                    "Comando reconhecido"
                },
                recognizedText = normalized,
                resultText = if (normalized.isEmpty()) {
                    it.resultText
                } else {
                    "Você disse: $normalized"
                },
                isListening = false
            )
        }
    }

    fun onSpeechError(message: String) {
        _uiState.update {
            it.copy(
                status = SystemStatus.ERROR,
                statusMessage = message,
                isListening = false
            )
        }
    }

    fun selectFeature(feature: FeatureAction): String {
        val message = when (feature) {
            FeatureAction.LISTEN_TEXT -> _uiState.value.resultText
            FeatureAction.READ_PDF -> "Leitor de PDF selecionado. Escolha um documento para iniciar a leitura."
            FeatureAction.DESCRIBE_IMAGE -> "Descrição de imagem selecionada. Escolha uma foto ou abra a câmera."
            FeatureAction.CREATE_ACTIVITY -> "Criador de atividades selecionado. Informe o tema da atividade."
            FeatureAction.BRAILLE -> "Recurso Braille selecionado."
            FeatureAction.SPOKEN_CALCULATOR -> "Calculadora falada selecionada."
            FeatureAction.LIBRARY -> "Biblioteca acessível selecionada."
            FeatureAction.HELP -> "Ajuda selecionada. Diga ou escolha o recurso sobre o qual deseja orientação."
        }

        _uiState.update {
            it.copy(
                status = SystemStatus.READY,
                statusMessage = message,
                resultText = message
            )
        }
        return message
    }

    fun resolveVoiceCommand(text: String): FeatureAction? {
        val command = text.lowercase()
        return when {
            "pdf" in command || "documento" in command -> FeatureAction.READ_PDF
            "descrever" in command || "imagem" in command || "foto" in command ->
                FeatureAction.DESCRIBE_IMAGE
            "atividade" in command || "exercício" in command -> FeatureAction.CREATE_ACTIVITY
            "braille" in command -> FeatureAction.BRAILLE
            "calculadora" in command || "calcular" in command || "cálculo" in command ->
                FeatureAction.SPOKEN_CALCULATOR
            "biblioteca" in command || "livro" in command -> FeatureAction.LIBRARY
            "ajuda" in command -> FeatureAction.HELP
            "ouvir" in command || "ler texto" in command -> FeatureAction.LISTEN_TEXT
            else -> null
        }
    }

    fun showMessage(message: String) {
        _uiState.update {
            it.copy(status = SystemStatus.READY, statusMessage = message)
        }
    }

    fun setHighContrast(enabled: Boolean) {
        _uiState.update {
            it.copy(
                highContrast = enabled,
                statusMessage = if (enabled) "Alto contraste ativado" else "Alto contraste desativado"
            )
        }
    }

    fun setLargeText(enabled: Boolean) {
        _uiState.update {
            it.copy(
                largeText = enabled,
                statusMessage = if (enabled) "Fonte ampliada ativada" else "Fonte ampliada desativada"
            )
        }
    }

    fun setAutoRead(enabled: Boolean) {
        _uiState.update {
            it.copy(
                autoRead = enabled,
                statusMessage = if (enabled) "Leitura automática ativada" else "Leitura automática desativada"
            )
        }
    }
}
