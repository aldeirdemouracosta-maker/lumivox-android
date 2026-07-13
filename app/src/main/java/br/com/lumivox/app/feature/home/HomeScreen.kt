package br.com.lumivox.app.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.lumivox.app.ui.theme.FeatureBlueEnd
import br.com.lumivox.app.ui.theme.FeatureBlueStart
import br.com.lumivox.app.ui.theme.FeatureGoldEnd
import br.com.lumivox.app.ui.theme.FeatureGoldStart
import br.com.lumivox.app.ui.theme.FeatureGreenEnd
import br.com.lumivox.app.ui.theme.FeatureGreenStart
import br.com.lumivox.app.ui.theme.FeaturePurpleEnd
import br.com.lumivox.app.ui.theme.FeaturePurpleStart
import br.com.lumivox.app.ui.theme.FeatureRedEnd
import br.com.lumivox.app.ui.theme.FeatureRedStart
import br.com.lumivox.app.ui.theme.FeatureTealEnd
import br.com.lumivox.app.ui.theme.FeatureTealStart
import br.com.lumivox.app.ui.theme.FocusYellow
import br.com.lumivox.app.ui.theme.Navy900
import br.com.lumivox.app.ui.theme.Navy950
import br.com.lumivox.app.ui.theme.NeonBlue
import br.com.lumivox.app.ui.theme.NeonCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LumivoxHomeScreen(
    uiState: HomeUiState,
    onMicClick: () -> Unit,
    onFeatureClick: (FeatureAction) -> Unit,
    onSpeakStatus: () -> Unit,
    onMenuClick: () -> Unit,
    onHighContrastChange: (Boolean) -> Unit,
    onLargeTextChange: (Boolean) -> Unit,
    onAutoReadChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSettings by rememberSaveable { mutableStateOf(false) }

    BoxWithConstraints(modifier.fillMaxSize()) {
        val compact = maxWidth < 720.dp

        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                if (compact) {
                    LumivoxBottomBar(
                        isListening = uiState.isListening,
                        onMenuClick = onMenuClick,
                        onMicClick = onMicClick,
                        onVolumeClick = onSpeakStatus
                    )
                }
            }
        ) { innerPadding ->
            LumivoxBackground {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    LumivoxTopBar(
                        compact = compact,
                        onMenuClick = onMenuClick,
                        onSettingsClick = { showSettings = true }
                    )

                    if (compact) {
                        CompactHomeContent(
                            uiState = uiState,
                            onFeatureClick = onFeatureClick,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        ExpandedHomeContent(
                            uiState = uiState,
                            onMicClick = onMicClick,
                            onFeatureClick = onFeatureClick,
                            onSpeakStatus = onSpeakStatus,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }

    if (showSettings) {
        AccessibilitySettingsSheet(
            uiState = uiState,
            onDismiss = { showSettings = false },
            onHighContrastChange = onHighContrastChange,
            onLargeTextChange = onLargeTextChange,
            onAutoReadChange = onAutoReadChange
        )
    }
}

@Composable
private fun LumivoxBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        if (MaterialTheme.colorScheme.background == Color.Black) Color.Black else Navy900,
                        if (MaterialTheme.colorScheme.background == Color.Black) Color.Black else Navy950
                    )
                )
            )
    ) {
        content()
    }
}

@Composable
private fun LumivoxTopBar(
    compact: Boolean,
    onMenuClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (compact) {
            Spacer(Modifier.size(48.dp))
        } else {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Rounded.Menu, contentDescription = "Abrir menu")
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "LUMIVOX",
                style = MaterialTheme.typography.titleLarge,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.semantics { heading() }
            )
            Text(
                text = "ASSISTENTE ACESSÍVEL",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        }

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Rounded.Settings, contentDescription = "Abrir configurações de acessibilidade")
        }
    }
}

@Composable
private fun CompactHomeContent(
    uiState: HomeUiState,
    onFeatureClick: (FeatureAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        StatusStrip(
            uiState = uiState,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
        FeatureGrid(
            columns = 2,
            highContrast = uiState.highContrast,
            onFeatureClick = onFeatureClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ExpandedHomeContent(
    uiState: HomeUiState,
    onMicClick: () -> Unit,
    onFeatureClick: (FeatureAction) -> Unit,
    onSpeakStatus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        VoicePanel(
            uiState = uiState,
            onMicClick = onMicClick,
            onSpeakStatus = onSpeakStatus,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.31f)
                .widthIn(min = 240.dp)
        )
        FeatureGrid(
            columns = 4,
            highContrast = uiState.highContrast,
            onFeatureClick = onFeatureClick,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.69f)
        )
    }
}

@Composable
private fun StatusStrip(uiState: HomeUiState, modifier: Modifier = Modifier) {
    val color = when (uiState.status) {
        SystemStatus.READY -> MaterialTheme.colorScheme.secondary
        SystemStatus.LISTENING -> FocusYellow
        SystemStatus.PROCESSING -> Color(0xFFB895FF)
        SystemStatus.ERROR -> MaterialTheme.colorScheme.error
    }

    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.76f),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.7f)),
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                stateDescription = uiState.statusMessage
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Box(
                Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = uiState.statusMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun VoicePanel(
    uiState: HomeUiState,
    onMicClick: () -> Unit,
    onSpeakStatus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.64f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MicActionButton(
                isListening = uiState.isListening,
                onClick = onMicClick,
                size = 166.dp
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = if (uiState.isListening) "OUVINDO..." else "PRONTO PARA OUVIR",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (uiState.isListening) "O que deseja fazer?" else "Toque no microfone para falar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp)
            )
            Spacer(Modifier.height(22.dp))
            Waveform(isActive = uiState.isListening)
            Spacer(Modifier.height(22.dp))
            Button(
                onClick = onSpeakStatus,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(Icons.Rounded.VolumeUp, contentDescription = null)
                Spacer(Modifier.width(10.dp))
                Text("FALAR AGORA")
            }
            Spacer(Modifier.height(18.dp))
            StatusStrip(uiState = uiState)
        }
    }
}

@Composable
private fun Waveform(isActive: Boolean) {
    val heights = remember { listOf(12, 22, 34, 18, 42, 26, 14, 32, 20, 38, 16, 28, 12) }
    val color = if (isActive) FocusYellow else MaterialTheme.colorScheme.secondary
    Row(
        modifier = Modifier
            .height(48.dp)
            .semantics { contentDescription = if (isActive) "Microfone captando voz" else "Microfone inativo" },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        heights.forEach { height ->
            Box(
                Modifier
                    .width(3.dp)
                    .height(height.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = if (isActive) 1f else 0.72f))
            )
        }
    }
}

@Composable
private fun FeatureGrid(
    columns: Int,
    highContrast: Boolean,
    onFeatureClick: (FeatureAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(FeatureAction.entries, key = { it.name }) { feature ->
            FeatureCard(
                feature = feature,
                highContrast = highContrast,
                onClick = { onFeatureClick(feature) }
            )
        }
    }
}

@Composable
private fun FeatureCard(
    feature: FeatureAction,
    highContrast: Boolean,
    onClick: () -> Unit
) {
    val gradient = featureGradient(feature, highContrast)
    val icon = featureIcon(feature)
    val borderColor = if (highContrast) Color.Cyan else gradient.first.copy(alpha = 0.95f)

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .semantics {
                contentDescription = "Abrir ${feature.title}"
            },
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(if (highContrast) 2.dp else 1.5.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradient))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (feature == FeatureAction.BRAILLE) {
                    Text(
                        text = "⠿",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                } else if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(52.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = feature.title.uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun featureIcon(feature: FeatureAction): ImageVector? = when (feature) {
    FeatureAction.LISTEN_TEXT -> Icons.Rounded.VolumeUp
    FeatureAction.READ_PDF -> Icons.Rounded.PictureAsPdf
    FeatureAction.DESCRIBE_IMAGE -> Icons.Rounded.PhotoCamera
    FeatureAction.CREATE_ACTIVITY -> Icons.Rounded.Edit
    FeatureAction.BRAILLE -> null
    FeatureAction.SPOKEN_CALCULATOR -> Icons.Rounded.Calculate
    FeatureAction.LIBRARY -> Icons.Rounded.MenuBook
    FeatureAction.HELP -> Icons.Rounded.HelpOutline
}

private fun featureGradient(feature: FeatureAction, highContrast: Boolean): List<Color> {
    if (highContrast) return listOf(Color(0xFF151515), Color.Black)

    return when (feature) {
        FeatureAction.LISTEN_TEXT,
        FeatureAction.LIBRARY,
        FeatureAction.HELP -> listOf(FeatureBlueStart, FeatureBlueEnd)
        FeatureAction.READ_PDF -> listOf(FeatureRedStart, FeatureRedEnd)
        FeatureAction.DESCRIBE_IMAGE -> listOf(FeatureGreenStart, FeatureGreenEnd)
        FeatureAction.CREATE_ACTIVITY -> listOf(FeaturePurpleStart, FeaturePurpleEnd)
        FeatureAction.BRAILLE -> listOf(FeatureGoldStart, FeatureGoldEnd)
        FeatureAction.SPOKEN_CALCULATOR -> listOf(FeatureTealStart, FeatureTealEnd)
    }
}

@Composable
private fun MicActionButton(
    isListening: Boolean,
    onClick: () -> Unit,
    size: androidx.compose.ui.unit.Dp
) {
    val accent = if (isListening) FocusYellow else MaterialTheme.colorScheme.secondary

    Box(
        modifier = Modifier
            .size(size)
            .border(2.dp, accent.copy(alpha = 0.35f), CircleShape)
            .padding(10.dp)
            .border(2.dp, accent.copy(alpha = 0.7f), CircleShape)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            onClick = onClick,
            shape = CircleShape,
            containerColor = if (isListening) Color(0xFF9A6100) else NeonBlue,
            contentColor = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .semantics {
                    contentDescription = if (isListening) "Parar escuta" else "Ativar microfone"
                    stateDescription = if (isListening) "Ouvindo" else "Inativo"
                }
        ) {
            Icon(
                imageVector = Icons.Rounded.Mic,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.55f)
            )
        }
    }
}

@Composable
private fun LumivoxBottomBar(
    isListening: Boolean,
    onMenuClick: () -> Unit,
    onMicClick: () -> Unit,
    onVolumeClick: () -> Unit
) {
    Surface(
        color = Navy950.copy(alpha = 0.98f),
        border = BorderStroke(1.dp, NeonBlue.copy(alpha = 0.45f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .padding(horizontal = 18.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier
                    .size(52.dp)
                    .border(1.dp, NeonBlue.copy(alpha = 0.7f), CircleShape)
            ) {
                Icon(Icons.Rounded.Menu, contentDescription = "Abrir menu")
            }

            MicActionButton(
                isListening = isListening,
                onClick = onMicClick,
                size = 76.dp
            )

            IconButton(
                onClick = onVolumeClick,
                modifier = Modifier
                    .size(52.dp)
                    .border(1.dp, NeonBlue.copy(alpha = 0.7f), CircleShape)
            ) {
                Icon(Icons.Rounded.VolumeUp, contentDescription = "Ouvir mensagem atual")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccessibilitySettingsSheet(
    uiState: HomeUiState,
    onDismiss: () -> Unit,
    onHighContrastChange: (Boolean) -> Unit,
    onLargeTextChange: (Boolean) -> Unit,
    onAutoReadChange: (Boolean) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 32.dp)
        ) {
            Text(
                text = "RECURSOS DE ACESSIBILIDADE",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .semantics { heading() }
            )
            SettingSwitch(
                title = "Alto contraste",
                description = "Usa fundo preto, texto branco e bordas em ciano.",
                checked = uiState.highContrast,
                onCheckedChange = onHighContrastChange
            )
            SettingSwitch(
                title = "Fonte ampliada",
                description = "Aumenta textos e controles do aplicativo.",
                checked = uiState.largeText,
                onCheckedChange = onLargeTextChange
            )
            SettingSwitch(
                title = "Leitura automática",
                description = "Lê em voz alta ações e resultados.",
                checked = uiState.autoRead,
                onCheckedChange = onAutoReadChange
            )
        }
    }
}

@Composable
private fun SettingSwitch(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .semantics(mergeDescendants = true) {},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 3.dp)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}
