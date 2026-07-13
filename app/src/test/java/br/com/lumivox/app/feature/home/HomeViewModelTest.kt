package br.com.lumivox.app.feature.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeViewModelTest {

    @Test
    fun `iniciar escuta atualiza status e estado do microfone`() {
        val viewModel = HomeViewModel()

        viewModel.beginListening()

        assertTrue(viewModel.uiState.value.isListening)
        assertEquals(SystemStatus.LISTENING, viewModel.uiState.value.status)
    }

    @Test
    fun `resultado de voz volta ao estado pronto e preserva texto`() {
        val viewModel = HomeViewModel()

        viewModel.beginListening()
        viewModel.onSpeechResult("abrir biblioteca")

        assertFalse(viewModel.uiState.value.isListening)
        assertEquals("abrir biblioteca", viewModel.uiState.value.recognizedText)
        assertEquals("Você disse: abrir biblioteca", viewModel.uiState.value.resultText)
    }

    @Test
    fun `selecionar leitor de PDF fornece instrucao acessivel`() {
        val viewModel = HomeViewModel()

        val message = viewModel.selectFeature(FeatureAction.READ_PDF)

        assertTrue(message.contains("PDF"))
        assertEquals(message, viewModel.uiState.value.statusMessage)
    }

    @Test
    fun `preferencias de acessibilidade alteram o estado`() {
        val viewModel = HomeViewModel()

        viewModel.setHighContrast(true)
        viewModel.setLargeText(true)
        viewModel.setAutoRead(false)

        val state = viewModel.uiState.value
        assertTrue(state.highContrast)
        assertTrue(state.largeText)
        assertFalse(state.autoRead)
    }

    @Test
    fun `comando de voz abre recurso correspondente`() {
        val viewModel = HomeViewModel()

        assertEquals(
            FeatureAction.DESCRIBE_IMAGE,
            viewModel.resolveVoiceCommand("quero descrever uma foto")
        )
        assertEquals(
            FeatureAction.SPOKEN_CALCULATOR,
            viewModel.resolveVoiceCommand("abrir calculadora")
        )
        assertEquals(null, viewModel.resolveVoiceCommand("bom dia"))
    }
}
