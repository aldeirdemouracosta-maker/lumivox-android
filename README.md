# LUMIVOX Android

Aplicativo Android nativo do **LUMIVOX — Assistente Acessível**, construído com Kotlin,
Jetpack Compose e MVVM. A interface foi adaptada do conceito visual azul-neon para
celulares, tablets e janelas redimensionáveis.

## Escopo desta versão

- tela inicial responsiva com oito recursos;
- painel de voz expandido em tablets;
- síntese de voz nativa em português do Brasil;
- reconhecimento de voz com permissão em tempo de execução;
- alto contraste, fonte ampliada e leitura automática;
- descrições semânticas para TalkBack;
- estado da tela centralizado em `HomeViewModel`;
- testes unitários da lógica principal;
- automação de APK debug e release assinado no GitHub Actions.

Os oito recursos estão navegáveis e fornecem retorno falado. Leitura real de PDF,
descrição de imagem, criação por IA, calculadora, biblioteca e conteúdo Braille são
camadas funcionais das próximas etapas.

## Requisitos

- Android Studio compatível com AGP 8.13;
- JDK 17;
- Android SDK Platform 37 e Build Tools 37.0.0;
- Gradle 8.13.

## Gerar APK debug

No Android Studio, use **Build > Build APK(s)**. Pela linha de comando, após gerar o
Gradle Wrapper uma única vez:

```bash
gradle wrapper --gradle-version 8.13
./gradlew testDebugUnitTest assembleDebug
```

Saída:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Gerar APK release assinado localmente

Defina as variáveis antes da compilação:

```bash
export LUMIVOX_KEYSTORE_PATH=/caminho/lumivox-release.jks
export LUMIVOX_STORE_PASSWORD='senha-do-keystore'
export LUMIVOX_KEY_ALIAS='lumivox'
export LUMIVOX_KEY_PASSWORD='senha-da-chave'
./gradlew assembleRelease
```

Nunca envie o arquivo `.jks` ou senhas ao repositório.

## GitHub Actions

O fluxo `.github/workflows/android-build.yml` gera o APK debug em cada `push` ou
pull request. A execução manual também pode gerar o release assinado após cadastrar
os seguintes segredos no repositório:

- `LUMIVOX_KEYSTORE_BASE64`
- `LUMIVOX_STORE_PASSWORD`
- `LUMIVOX_KEY_ALIAS`
- `LUMIVOX_KEY_PASSWORD`

Para converter o keystore em Base64 no Linux:

```bash
base64 -w 0 lumivox-release.jks
```

No PowerShell:

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("lumivox-release.jks"))
```

## Identidade provisória

O `applicationId` atual é `br.com.lumivox.app`. Ele deve ser confirmado antes da
primeira publicação, pois trocar o identificador depois cria outro aplicativo para
o Android e para a Play Store.

