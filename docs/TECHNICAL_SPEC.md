# Especificação técnica — LUMIVOX Android 1.0

## 1. Leitura do conceito visual

A imagem de referência apresenta uma interface de tecnologia assistiva em azul-neon,
com fundo azul-marinho quase preto, tipografia branca, contornos luminosos e cartões
coloridos. A versão móvel é a referência primária para o APK:

1. cabeçalho compacto com marca central e configurações;
2. grade de duas colunas com oito recursos;
3. barra inferior com menu, microfone central e leitura em voz alta;
4. alto contraste e fonte ampliada acessíveis nas configurações.

Em telas a partir de 720 dp, o layout se adapta ao conceito mostrado no notebook:
o painel de voz ocupa a esquerda e a grade muda para quatro colunas. Não existe uma
segunda implementação da tela; o mesmo componente Compose reage à largura disponível.

## 2. Paleta convertida

| Uso | Cor |
| --- | --- |
| Fundo principal | `#020817` |
| Superfície | `#061027` |
| Painel elevado | `#0A1B3D` |
| Azul de ação | `#008DFF` |
| Ciano de destaque | `#00D9FF` |
| Foco/escuta | `#FFD166` |
| Texto principal | `#F8FBFF` |
| Texto secundário | `#BFD9FF` |

O modo de alto contraste substitui superfícies por preto puro, texto por branco e
contornos por ciano. Assim, a mudança é funcional e não apenas decorativa.

## 3. Componentes Compose

| Elemento visual | Componente |
| --- | --- |
| Estrutura adaptativa | `BoxWithConstraints` + `Scaffold` |
| Cabeçalho | `LumivoxTopBar` |
| Estado do assistente | `StatusStrip` |
| Painel de voz | `VoicePanel` |
| Microfone | `MicActionButton` |
| Recursos principais | `LazyVerticalGrid` + `FeatureCard` |
| Navegação móvel | `LumivoxBottomBar` |
| Acessibilidade | `AccessibilitySettingsSheet` |

## 4. Estado e eventos

`HomeViewModel` mantém um único `HomeUiState`. A Activity conecta esse estado às APIs
nativas de voz, sem colocar `Context` dentro do ViewModel.

```text
Toque ou voz
    -> MainActivity
    -> HomeViewModel
    -> HomeUiState
    -> recomposição da HomeScreen
```

Serviços Android usados:

- `TextToSpeech` com idioma `pt-BR`;
- `SpeechRecognizer` com permissão de microfone em tempo de execução;
- parser local de comandos para selecionar os oito recursos sem depender de internet;
- `StateFlow` para estado observável e testável.

## 5. Regras de acessibilidade

- controles interativos têm pelo menos 48 dp;
- ícones decorativos não são anunciados separadamente;
- cartões anunciam a ação completa, por exemplo “Abrir Ler PDF”;
- o microfone anuncia estado “Inativo” ou “Ouvindo”;
- estados importantes permanecem visíveis em texto;
- a fonte ampliada modifica toda a tipografia do tema;
- resultados não dependem exclusivamente de cor;
- a grade é rolável em telas pequenas e com fonte ampliada;
- animações contínuas do conceito visual foram removidas da primeira versão para
  reduzir distração e enjoo por movimento.

## 6. Limites desta entrega inicial

O reconhecimento e a síntese de voz estão implementados. Os cartões de PDF, imagem,
atividades, calculadora, biblioteca e Braille têm estado, retorno textual e retorno
falado, mas suas ferramentas internas serão implementadas como features independentes
nas próximas etapas. Chamadas de IA deverão usar um serviço intermediário protegido;
nenhuma chave privada deve ficar dentro do APK.
