# Altnik Downloader — Instagram / Facebook Video & Photo Saver (Android)

Share koro Instagram Reel -> **Altnik Downloader** -> auto download Gallery te.

## Features
- Instagram Reel / Post / Story link (public) + Facebook video/photo + TikTok/YouTube link
- 2 vabe kaj kore:
  1. Instagram e **Share > Altnik Downloader** (auto start)
  2. **Copy Link > app e Paste > Download**
- System DownloadManager diye `Downloads/AltnikDownloader/` e save — Gallery/Files e dekha jabe
- App er vitore tomar nijer/authorized Cobalt server URL dite hobe. Public random server reliable na, onek server API access block kore.

## Build ONLINE (Android Studio lagbe na) — Recommended
Workflow file already added: `.github/workflows/android.yml`
1. https://github.com e account kholo (free)
2. **New repository** banao (name: `altnik-downloader`, Public)
3. **Uploading an existing file** > tomar PC er `InstaFB Downloader` folder er SOB file drag-drop kore **Commit**
   - mone rekho `.github/workflows/android.yml` soho upload hote hobe
4. Repo te **Actions** tab e jao > `Build APK` run hobe (3-6 min)
5. Green tick ele > build e dhuko > **Artifacts > altnik-downloader-apk** download koro (zip er vitore `.apk`)
6. APK phone e copy kore install koro (Unknown sources allow korte hobe)

## Build (Android Studio - optional)
1. Android Studio (Hedgehog+) open koro
2. **Open** > `InstaFB Downloader` folder select
3. Gradle sync hote dao (net lagbe, 2-5 min first time)
4. Phone connect kore **Run** (USB debugging ON) — naki **Build > Build APK**
5. APK pabe: `app/build/outputs/apk/debug/app-debug.apk`

## Use
1. Instagram/Facebook e Reel kholo > **Share > Copy link**
   - athoba **Share > Altnik Downloader** (direct)
2. App e link asle **Download** chap dao
3. Notification ele bujhba download sesh. File: `Downloads/AltnikDownloader/`

## Cobalt server setup (important)
Cobalt official hosted API **does not allow third-party apps** and may require Turnstile CAPTCHA. Public random instances also go offline often. The reliable option is a Cobalt server that you operate or are authorized to use.

The app now asks for the server URL on the main screen and saves it locally. Cobalt API v10+ uses `POST /` — not the old `/api/json` endpoint.

For your own server, use the official Cobalt documentation: https://github.com/imputnet/cobalt/blob/main/docs/run-an-instance.md

API documentation: https://github.com/imputnet/cobalt/blob/main/docs/api.md

## Legal note
- Sudhu public content / nijer content download koro
- Onner video bina permission e re-upload koro na

## Files
- `MainActivity.kt` — share-intent + paste + resolve + download
- `CobaltApi.kt` — request/response models
- `CobaltClient.kt` — Retrofit setup (BASE_URL ekhane)
- `DownloadHelper.kt` — DownloadManager save logic
- `activity_main.xml` — UI
