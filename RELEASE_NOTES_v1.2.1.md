# 🎮 ShadowKeys v1.2.1 - GTA SA Cheat Injector

## 🚀 What's New

### ✨ Major Features

**Complete GTA SA Cheat Injection System**
- Shell-based cheat injection (no root required)
- Fixed keyboard layout (240dp, no resizing/jerking)
- Screen tap for game focus before injection
- Optimized timing (first char gets extra delay)
- ENTER key support for GTA SA compatibility
- Configurable keystroke delay (50-300ms)
- Settings Activity with timing adjustment
- Test injection functionality
- All 12 GTA SA cheats pre-configured

### 🔧 Technical Improvements

- **ShellInputInjector** with context-aware settings
- Fixed keyboard dimensions to prevent UI instability
- Hardware acceleration enabled for smooth rendering
- Proper error handling and logging
- SharedPreferences for settings persistence

### 🎮 Pre-Configured Cheat Codes

- **HESOYAM** - Money ($250,000), Health, Armor
- **BAGUVIX** - God Mode (Infinite Health)
- **LXGIWYL** - Weapon Set 1
- **KJKSZPJ** - Weapon Set 2
- **UZUMYMW** - Weapon Set 3
- **ROCKETMAN** - Jetpack
- **JUMPJET** - Hydra (Fighter Jet)
- **PANZER** - Rhino Tank
- **WANRLTW** - Infinite Ammo
- **KANGAROO** - Super Jump
- **TURNDOWNTHEHEAT** - Lower Wanted Level
- **TURNUPTHEHEAT** - Raise Wanted Level

### 📱 UI Enhancements

- Professional keyboard layout with emojis
- Settings button in keyboard for easy access
- Toast feedback for all actions
- Instructions dialog for GTA SA usage
- Test injection feature for verification

## 🛠️ How to Use

### Initial Setup

1. **Install the APK** on your Android device
2. **Enable Keyboard**: Go to Settings → System → Languages & Input → Virtual Keyboard → Manage Keyboards → Enable "Cheat Keyboard"
3. **Enable Accessibility Service** (if needed): Settings → Accessibility → Enable "Keyboard Accessibility Service"
4. **Enable Overlay Permission**: Settings → Apps → Special Access → Display over other apps → Enable for ShadowKeys

### Using Cheats in GTA SA

1. **Launch GTA San Andreas** Classic version
2. **Load your game** or start a new game
3. **Be in GAMEPLAY mode** (not menu, not paused)
4. **Tap the game screen once** (to ensure game has focus)
5. **Press the overlay button** to open the keyboard
6. **Press any cheat button** (e.g., HESOYAM)
7. **Watch for "CHEAT ACTIVATED"** message on screen

### If Cheats Don't Work

1. Open keyboard → Press **⚙️ SETTINGS** button
2. Increase **keystroke delay** to 150-200ms
3. Try the **🧪 Test Injection** button first (in Notes app)
4. If test works, try again in GTA SA

## 📋 Testing Protocol

### Test 1: Basic Injection (Outside Game)
- Open Notes app
- Enable keyboard
- Press HESOYAM button
- Expected: "HESOYAM" typed character by character

### Test 2: Timing Test
- Go to Settings (from keyboard)
- Adjust timing slider
- Test injection in Notes app
- Confirm timing works correctly

### Test 3: GTA SA Test (The Real Test)
- Launch GTA San Andreas Classic
- Be in gameplay mode
- Tap screen once
- Press overlay → Press HESOYAM
- Watch for cheat activation message

## ⚙️ Settings

Access settings from the keyboard by pressing the **⚙️ SETTINGS** button:

- **Keystroke Delay**: Adjust timing between keys (50-300ms)
  - Fast devices: 80-100ms
  - Medium devices: 100-150ms
  - Slow devices: 150-250ms
- **Test Injection**: Test the injection system in any text app
- **Instructions**: View detailed GTA SA usage instructions

## 🐛 Bug Fixes

- Fixed keyboard jerking/resizing issues
- Fixed cheat codes not injecting into GTA SA
- Fixed UI instability in game environment
- Improved timing for better cheat activation
- Added proper focus handling for game compatibility

## 📦 Installation

1. Download `ShadowKeys.apk` from the releases section
2. Enable "Install from Unknown Sources" in Android Settings
3. Install the APK
4. Follow the setup instructions above

## 🔒 Permissions

- **SYSTEM_ALERT_WINDOW**: For overlay button
- **BIND_INPUT_METHOD**: For keyboard functionality
- **BIND_ACCESSIBILITY_SERVICE**: For input injection (optional)

## 📝 Notes

- Works best with **GTA San Andreas Classic** version
- Some GTA SA versions may require different timing
- Test injection feature helps verify system is working
- Settings allow fine-tuning for different devices

## 🙏 Credits

Built with React Native and Android native modules for optimal performance.

---

**Version**: 1.2.1  
**Release Date**: January 2, 2026  
**APK Size**: ~18 MB

## 🔄 Changes from v1.2.0

- Updated version number to v1.2.1
- Same features and improvements as v1.2.0
- Bug fixes and stability improvements

