# 🤖 JARVIS — Voice-First AI Assistant for Android

> A JARVIS-style personal AI assistant that runs on your phone. Voice in, voice out, real device actions.

**Language:** Kotlin · **UI:** Material 3 · **Min SDK:** Android 13+

---

## What it is

JARVIS is a **voice-first** Android assistant — not a chat app with a mic bolted on. You talk to it, it talks back, and it can act on your device.

**Target device:** Samsung S23 Ultra (Android 13+) — built and verified on real hardware.

## Features

- 🎙️ **Voice I/O** — speech recognition and spoken responses, hands-free
- 🧠 **Pluggable AI backends** — swap providers without touching the UI (see below)
- ⏰ **Reminders** — local persistence via Room database
- 📡 **Connectivity awareness** — degrades gracefully offline
- 🎨 **JARVIS-style HUD** — Material 3, built to feel like an assistant, not a form

## Pluggable AI providers

The reason JARVIS isn't locked to one vendor:

| Provider | File | Use case |
|---|---|---|
| **Hermes** | `HermesProvider.kt` | Self-hosted agent backend |
| **OpenAI-compatible** | `OpenAiProvider.kt` | Any OpenAI-compatible endpoint |
| **Local** | `LocalAiProvider.kt` | On-device / offline |

All three implement `IProvider`, so adding a fourth means writing one class — no UI changes.

## Architecture

```
app/src/main/java/com/darrenai/jarvis/
├── ai/                  # provider abstraction
│   ├── IProvider.kt     # the contract
│   ├── AiService.kt     # routing
│   ├── HermesProvider.kt
│   ├── OpenAiProvider.kt
│   └── LocalAiProvider.kt
├── database/            # Room persistence
│   ├── ReminderDatabase.kt
│   └── ReminderEntity.kt
├── ConnectivityManager.kt
├── JarvisApplication.kt
└── MainActivity.kt
```

**32 Kotlin files.** Provider abstraction is the core design decision: the assistant's behavior shouldn't be coupled to whichever model is cheapest this month.

---

## Build

**Status:** ✅ Complete and buildable. See [`BUILD_INSTRUCTIONS.md`](BUILD_INSTRUCTIONS.md) for the full guide.

```bash
git clone https://github.com/DarthSandD/jarvis-apk.git
cd jarvis-apk
./gradlew assembleDebug
# → app/build/outputs/apk/debug/app-debug.apk
```

Requires Android SDK + JDK 17. `build-env.sh` is included for environment setup.

---

## Related

- **[jarvis-fsa](https://github.com/DarthSandD/jarvis-fsa)** — agent-loop variant with Termux bridge and device actions
- **[Omniscient](https://github.com/DarthSandD/omniscient)** — HUD-driven voice assistant, another take on the same problem

---

## License

Open source. See repository for details.

**Built by [Darren Lieu](https://darrenlin.pages.dev/)** · [@DarthSandD](https://github.com/DarthSandD)
