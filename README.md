# TotemTweak — Fabric 1.21.1

Customise the Totem of Undying experience:

| Feature | Config key | Default | Range |
|---|---|---|---|
| Overlay animation scale | `animationScale` | `1.0` | `0.0` – `1.0` |
| Overlay animation transparency | `animationAlpha` | `1.0` | `0.0` (invisible) – `1.0` (opaque) |
| Remove totem particles | `removeParticles` | `false` | `true` / `false` |
| Totem size in hand | `handScalePercent` | `100.0` | `1` – `100` |

## Config file

`<minecraft>/config/totemtweak.json`

```json
{
  "animationScale": 0.5,
  "animationAlpha": 0.4,
  "removeParticles": true,
  "handScalePercent": 60.0
}
```

Edit the file and use `/totemtweak reload` in-game (no restart needed).

## In-game commands

| Command | Description |
|---|---|
| `/totemtweak reload` | Reload config from disk |
| `/totemtweak info` | Print current config values |

## Build

```bash
./gradlew build
# Jar at: build/libs/totemtweak-1.0.0+1.21.1.jar
```

## How it works

| Mixin | Target | Purpose |
|---|---|---|
| `TotemOverlayRendererMixin` | `InGameHud#renderTotemPopUpAnimation` | Cancels vanilla overlay → redraws with custom scale & alpha |
| `TotemTimeAccessor` | `InGameHud.totemTime` | Reads private field without reflection |
| `GameRendererMixin` | `ParticleManager#addTotemParticles` | Cancels particle burst when `removeParticles=true` |
| `LivingEntityRendererMixin` | `HeldItemRenderer#renderFirstPersonItem` | Wraps rendering in a scale matrix for the totem item |
