# PDV Kotlin

Aplicativo Android mobile de PDV criado em Kotlin com Jetpack Compose, MVVM, Clean Architecture, Hilt, Room, Retrofit/OkHttp, StateFlow, Navigation Compose e Gradle Kotlin DSL.

## Rodar no Android Studio

1. Abra a pasta do projeto no Android Studio.
2. Aguarde o Sync do Gradle.
3. Selecione um emulador ou dispositivo Android.
4. Execute a configuração `app`.

## Rodar no VS Code

1. Abra esta pasta no VS Code.
2. Instale extensões úteis: Kotlin, Gradle for Java e Android iOS Emulator.
3. Use o terminal integrado:

```bash
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

## Logins mockados

- Admin: `admin` / `admin123`
- Caixa: `caixa` / `caixa123`
- Gerente: `gerente` / `gerente123`

## Arquitetura

- `core`: DI, navegação e utilitários compartilhados.
- `domain`: modelos, contratos de repositório e use cases.
- `data`: Room, DAOs, entidades, API fake Retrofit e implementações de repositórios.
- `presentation`: navegação Compose principal.
- `features`: telas e ViewModels por área funcional.

## Funcionalidades iniciais

- Login com sessão persistida em DataStore.
- Produtos com Room, busca rápida, código de barras e dados mockados.
- Venda com carrinho, quantidade, desconto, observações, cancelamento e pedido segurado.
- Pagamento com dinheiro, crédito, débito, PIX, múltiplas formas e troco.
- Recibo pronto para extensão SAT/NFC-e, Bluetooth e PDF.
- Relatórios de vendas, fechamento, ticket médio e mais vendidos.
- Configurações com tema, empresa, impressora, impostos e logout.
- Base offline-first com `pendingSync` nas entidades para sincronização futura.

## Comandos úteis

```bash
./gradlew :app:compileDebugKotlin
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```
