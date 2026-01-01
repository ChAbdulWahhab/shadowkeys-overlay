# 📦 GitHub Release Instructions for v1.2.1

## ⚠️ Important Note

**v1.2.0 already exists!** This is an updated release with version **v1.2.1**.

## Option 1: Using GitHub Web Interface (Easiest)

1. Go to: https://github.com/ChAbdulWahhab/shadowkeys-overlay/releases/new
2. Fill in the following:
   - **Tag**: `v1.2.1`
   - **Release title**: `ShadowKeys v1.2.1 - GTA SA Cheat Injector`
   - **Description**: Copy the contents from `RELEASE_NOTES_v1.2.1.md`
3. Click **"Attach binaries"** and upload `release/ShadowKeys.apk`
4. Click **"Publish release"**

## Option 2: Using PowerShell Script (Automated)

1. Get a GitHub Personal Access Token:
   - Go to: https://github.com/settings/tokens
   - Click "Generate new token (classic)"
   - Select `repo` scope
   - Copy the token

2. Run the script:
   ```powershell
   .\create_release.ps1 -Token "your_github_token_here" -Tag "v1.2.1"
   ```

## Option 3: Using GitHub CLI (If Installed)

```bash
gh release create v1.2.1 \
  --title "ShadowKeys v1.2.1 - GTA SA Cheat Injector" \
  --notes-file RELEASE_NOTES_v1.2.1.md \
  release/ShadowKeys.apk
```

## Release Details

- **Version**: v1.2.1 (Updated from v1.2.0)
- **APK File**: `release/ShadowKeys.apk`
- **APK Size**: ~18 MB
- **Release Notes**: See `RELEASE_NOTES_v1.2.1.md`
- **Version Code**: 3
- **Version Name**: 1.2.1

