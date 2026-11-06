# Bluey Android + Windows add-on

Your repository already contains the Android application in `:app`. This add-on introduces a Windows desktop companion in `:desktop`, without replacing or modifying the Android source.

## 1. Add the files

Copy the `desktop/` directory and `.github/workflows/windows-exe.yml` into the root of your existing `googly.eyed` repository.

## 2. Include the desktop module

Open the existing root `settings.gradle.kts` and add this line after `include(":app")`:

```kotlin
include(":desktop")
```

Do not replace the existing settings file.

## 3. Build Android

From the repository root:

```bash
./gradlew assembleDebug
```

On Windows PowerShell:

```powershell
.\gradlew.bat assembleDebug
```

The Android APK is produced by the existing Android module at:

`app/build/outputs/apk/debug/app-debug.apk`

## 4. Build the Windows installer

Run this on a Windows 10/11 machine with JDK 21:

```powershell
.\gradlew.bat :desktop:packageExe
```

The self-contained installer is written under:

`desktop/build/compose/binaries/main/exe/`

The installer bundles its Java runtime, so end users do not need to install Java separately. Windows EXE packaging must run on Windows; it is not cross-compiled from Linux or macOS.

## 5. GitHub Actions

After adding the workflow, open **Actions → Build Bluey Windows installer → Run workflow**. The generated EXE is attached as a workflow artifact. Pushing a tag such as `v1.0.0` also triggers a build.

## Scope and parity notes

- Android remains the existing Kotlin/Jetpack Compose app, unchanged by this add-on.
- Windows is a Compose Desktop implementation with an animated blueberry face, pointer-following eyes, focus task, adjustable timer, pause/resume/end controls, completion count, and desktop chirp.
- This initial Windows port intentionally does not claim to provide Android's simulated focus classifier, Android voice history, microphone assistant, or Mac pairing. Those require separate desktop implementations and platform-specific permissions/integration.
- The Windows edition does not monitor the screen or capture user activity.
