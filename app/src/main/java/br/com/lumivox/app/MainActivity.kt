package br.com.lumivox.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.lumivox.app.core.accessibility.SpeechRecognitionController
import br.com.lumivox.app.core.accessibility.TextToSpeechController
import br.com.lumivox.app.feature.home.FeatureAction
import br.com.lumivox.app.feature.home.HomeViewModel
import br.com.lumivox.app.feature.home.LumivoxHomeScreen
import br.com.lumivox.app.ui.theme.LumivoxTheme

class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var textToSpeech: TextToSpeechController
    private lateinit var speechRecognition: SpeechRecognitionController

    private val audioPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            speechRecognition.start()
        } else {
            val message = "Permissão do microfone negada. Autorize o acesso nas configurações do Android."
            viewModel.onSpeechError(message)
            textToSpeech.speak(message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        textToSpeech = TextToSpeechController(this)
        speechRecognition = SpeechRecognitionController(
            context = this,
            listener = object : SpeechRecognitionController.Listener {
                override fun onListeningStarted() {
                    viewModel.beginListening()
                }

                override fun onListeningFinished() {
                    viewModel.endListening()
                }

                override fun onResult(text: String) {
                    viewModel.onSpeechResult(text)
                    val feature = viewModel.resolveVoiceCommand(text)
                    if (feature != null) {
                        handleFeature(feature)
                    } else if (viewModel.uiState.value.autoRead && text.isNotBlank()) {
                        textToSpeech.speak("Você disse: $text")
                    }
                }

                override fun onError(message: String) {
                    viewModel.onSpeechError(message)
                    if (viewModel.uiState.value.autoRead) {
                        textToSpeech.speak(message)
                    }
                }
            }
        )

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LumivoxTheme(
                highContrast = uiState.highContrast,
                largeText = uiState.largeText
            ) {
                LumivoxHomeScreen(
                    uiState = uiState,
                    onMicClick = ::requestVoiceInput,
                    onFeatureClick = { feature -> handleFeature(feature) },
                    onSpeakStatus = { textToSpeech.speak(uiState.statusMessage) },
                    onMenuClick = {
                        val message = "Menu principal. Escolha um dos recursos disponíveis na tela."
                        viewModel.showMessage(message)
                        if (uiState.autoRead) textToSpeech.speak(message)
                    },
                    onHighContrastChange = { enabled ->
                        viewModel.setHighContrast(enabled)
                        if (uiState.autoRead) {
                            textToSpeech.speak(
                                if (enabled) "Alto contraste ativado" else "Alto contraste desativado"
                            )
                        }
                    },
                    onLargeTextChange = { enabled ->
                        viewModel.setLargeText(enabled)
                        if (uiState.autoRead) {
                            textToSpeech.speak(
                                if (enabled) "Fonte ampliada ativada" else "Fonte ampliada desativada"
                            )
                        }
                    },
                    onAutoReadChange = { enabled ->
                        viewModel.setAutoRead(enabled)
                        if (enabled) textToSpeech.speak("Leitura automática ativada")
                    }
                )
            }
        }

        textToSpeech.speak("Bem-vindo ao LUMIVOX, seu assistente acessível.")
    }

    private fun requestVoiceInput() {
        if (viewModel.uiState.value.isListening) {
            speechRecognition.stop()
            return
        }

        val permissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {
            speechRecognition.start()
        } else {
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun handleFeature(feature: FeatureAction) {
        val message = viewModel.selectFeature(feature)
        if (feature == FeatureAction.LISTEN_TEXT || viewModel.uiState.value.autoRead) {
            textToSpeech.speak(message)
        }
    }

    override fun onDestroy() {
        speechRecognition.destroy()
        textToSpeech.shutdown()
        super.onDestroy()
    }
}
