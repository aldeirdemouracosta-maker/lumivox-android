package br.com.lumivox.app.feature.home

enum class FeatureAction(val title: String) {
    LISTEN_TEXT("Ouvir texto"),
    READ_PDF("Ler PDF"),
    DESCRIBE_IMAGE("Descrever imagem"),
    CREATE_ACTIVITY("Criar atividade"),
    BRAILLE("Braille"),
    SPOKEN_CALCULATOR("Calculadora falada"),
    LIBRARY("Biblioteca"),
    HELP("Ajuda")
}

enum class SystemStatus {
    READY,
    LISTENING,
    PROCESSING,
    ERROR
}

data class HomeUiState(
    val status: SystemStatus = SystemStatus.READY,
    val statusMessage: String = "Sistema pronto",
    val recognizedText: String = "",
    val resultText: String = "Bem-vindo ao LUMIVOX, seu assistente acessível.",
    val isListening: Boolean = false,
    val highContrast: Boolean = false,
    val largeText: Boolean = false,
    val autoRead: Boolean = true
)

