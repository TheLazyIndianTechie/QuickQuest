## Product Requirements Document: Meta Quest Quick Launcher

**Project Title:** Meta Quest Quick Launcher  
**Target Platform:** Meta Quest 2/3/3S/Pro  
**Primary Technology:** Meta Spatial SDK (Android/Kotlin, Jetpack Compose), possible minimal use of Meta Interaction SDK (hand gesture/hotkey triggers)

### 1. Objective

Build a lightweight, gesture- or hotkey-triggered launcher app, inspired by Raycast for macOS, for Meta Quest headsets. The app provides fast search and launching of installed Quest apps, sideloaded apps, and shortcuts in a minimal popup UI overlay.

#### Key Goals
- Zero full-screen takeover: Must overlay minimally atop the current view (preferably passthrough background, spatially UI-locked).
- Instant activation: Appears rapidly on trigger (gesture or hotkey).
- Search-first UX: Typing instantly filters app list with fuzzy search.
- Low resource overhead: Modular, minimal dependencies (avoid Unity/Unreal for core build).
- Compatibility with core Quest OS features.

***

### 2. Features & Functionality

#### Core Features
- **Popup UI Overlay**
  - Glass-morphic, semi-transparent floating window.
  - Appears head-locked or world-locked when triggered.
  - Responsive to controller, hand gesture, or physical button.
  - Dismisses instantly with “close” gesture or hotkey.

- **App Search & Launcher**
  - List all installed apps (including official & sideloaded).
  - Fuzzy search as user types (results update in real time).
  - Tap/click/gesture to launch selected app.
  - Show app icon, name, shortcut description in results.
  - Option to launch websites (if supported): store custom web shortcuts.

- **Custom Shortcuts & Groups**
  - Users can save favorite app lists or categorize apps (work, games, etc.).
  - Configurable shortcuts for OS functions (e.g., open settings, Wi-Fi, battery).

- **Gesture/Hotkey Integration**
  - Hand-gesture trigger (e.g., pinch or wrist tap) using Meta Interaction SDK.
  - Physical button trigger (e.g., Quest controller menu button double-tap).
  - Optional voice activation (if Quest allows).

#### Optional Features (for later scope/extensions)
- Floating widgets: Calendar, weather, clipboard.
- Recent apps/history.
- Deep links: Launch apps with parameters/activities.

***

### 3. Technical Requirements

**Architecture**
- Built with Android/Kotlin using Meta Spatial SDK for overlay/passthrough integration.
- Use Jetpack Compose for UI: fast, modular, optimized for overlays.
- App launching via Android intents & PackageManager – no root/system privilege needed.
- Passthrough overlay for light footprint (use OVROverlay and related classes).
- Gesture input: minimal use of Meta Interaction SDK.

**Device Requirements**
- Compatible with Quest 2/3/3S/Pro (Horizon OS v69+).
- Sideloadable (APK via SideQuest); App Lab or Horizon Store ready.
- Runs with developer mode enabled; no root required.

**Performance**
- Popup must appear in <200ms after trigger.
- Idle memory/cpu footprint < 20MB.

***

### 4. User Experience Requirements

- UI is clean, minimal, glass effect, accessible in MR or VR.
- Focused on quick discoverability and instant dismissal.
- No intrusive notifications or full-screen UI.
- Results update in real time (as user types or gestures).
- Must be operable via controller, hand gestures, and (optionally) voice.

***

### 5. Security & Permissions

- Request only necessary permissions: launch other apps, read app list, internet (if web shortcuts).
- Zero user data transmitted externally unless explicitly configured (web shortcut sync).

***

### 6. Milestones & Deliverables

1. Project setup: Kotlin/Spatial SDK skeleton, developer onboarding docs.
2. Popup overlay prototype: baseline UI/UX (head-locked, overlay, passthrough background, gesture/button trigger).
3. App discovery & launcher: API integration, fuzzy search logic.
4. Gesture/Hotkey handler: test and tune for Quest devices.
5. Beta release: APK, core features, sideload docs.
6. Polish & App Lab submission (optional).

***

### 7. Prompt/LLM Requirements

- Generate Kotlin/Compose code for popup overlay using Spatial SDK.
- Scaffold fuzzy search logic for installed app list.
- Design a modular UI with head-locked overlay and passthrough background.
- Implement simple gesture handler (e.g., pinch to open/close).
- Output minimal build/deployment script for Meta Quest APK.
- Scripts for querying and launching apps via intent.
- (Optional) Code for saving/loading custom user shortcuts.

***

**End PRD**
