# Altnik Downloader — Instagram / Facebook Video & Photo Saver (Android)

Share koro Instagram Reel -> **Altnik Downloader** -> auto download Gallery te.

## Features
- Instagram Reel / Post / Story link (public) + Facebook video/photo + TikTok/YouTube link
- 2 vabe kaj kore:
  1. Instagram e **Share > Altnik Downloader** (auto start)
  2. **Copy Link > app e Paste > Download**
- Kono login / API key lage na (free Cobalt API instance use kore)
- System DownloadManager diye `Downloads/AltnikDownloader/` e save — Gallery/Files e dekha jabe

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

## Jodi Cobalt instance down thake
`CobaltClient.kt` e `BASE_URL` change koro, jekono public instance:
- `https://api.cobalt.tools/`
- `https://cobalt-api.kwiatekmiki.com/`
- `https://co.wukko.xyz/`

API doc: https://github.com/imputnet/cobalt (POST `api/json` with `{"url": "..."}`)

## Legal note
- Sudhu public content / nijer content download koro
- Onner video bina permission e re-upload koro na

## Files
- `MainActivity.kt` — share-intent + paste + resolve + download
- `CobaltApi.kt` — request/response models
- `CobaltClient.kt` — Retrofit setup (BASE_URL ekhane)
- `DownloadHelper.kt` — DownloadManager save logic
- `activity_main.xml` — UI
