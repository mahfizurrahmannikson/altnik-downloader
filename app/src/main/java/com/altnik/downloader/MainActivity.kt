package com.altnik.downloader

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var etLink: EditText
    private lateinit var btnPaste: Button
    private lateinit var btnDownload: Button
    private lateinit var progress: ProgressBar
    private lateinit var tvStatus: TextView
    private lateinit var ivThumb: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etLink = findViewById(R.id.etLink)
        btnPaste = findViewById(R.id.btnPaste)
        btnDownload = findViewById(R.id.btnDownload)
        progress = findViewById(R.id.progress)
        tvStatus = findViewById(R.id.tvStatus)
        ivThumb = findViewById(R.id.ivThumb)

        askNotificationPermission()
        handleShareIntent(intent)

        btnPaste.setOnClickListener { pasteFromClipboard() }

        btnDownload.setOnClickListener {
            val link = etLink.text.toString().trim()
            if (link.isEmpty()) {
                toast(getString(R.string.msg_no_link)); return@setOnClickListener
            }
            if (!isSupportedLink(link)) {
                toast(getString(R.string.msg_invalid)); return@setOnClickListener
            }
            fetchAndDownload(link)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleShareIntent(intent)
    }

    // Instagram -> Share -> Altnik Downloader lands here
    private fun handleShareIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val shared = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return
            // Shared text may contain extra words — extract first http link
            val link = Regex("https?://\\S+").find(shared)?.value ?: shared
            etLink.setText(link)
            tvStatus.text = "Link received. Press Download."
            // auto-start for best UX
            if (isSupportedLink(link)) fetchAndDownload(link)
        }
    }

    private fun isSupportedLink(link: String): Boolean {
        val l = link.lowercase()
        return l.contains("instagram.com") || l.contains("facebook.com") ||
                l.contains("fb.watch") || l.contains("fb.com") ||
                l.contains("youtube.com") || l.contains("youtu.be") ||
                l.contains("tiktok.com")
    }

    private fun pasteFromClipboard() {
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData? = cm.primaryClip
        if (clip != null && clip.itemCount > 0) {
            val text = clip.getItemAt(0).text?.toString() ?: ""
            val link = Regex("https?://\\S+").find(text)?.value ?: text
            etLink.setText(link)
        } else {
            toast("Clipboard is empty")
        }
    }

    private fun fetchAndDownload(pageUrl: String) {
        setLoading(true, "Resolving video…")
        lifecycleScope.launch {
            try {
                val res = CobaltClient.api.resolve(CobaltRequest(url = pageUrl))
                when (res.status) {
                    "redirect", "tunnel" -> {
                        val fileUrl = res.url!!
                        showThumb(null)
                        startSystemDownload(fileUrl, isVideo = true)
                    }
                    "picker" -> {
                        // carousel: pick first video item, else first item
                        val item = res.picker?.firstOrNull { it.url != null }
                            ?: res.picker?.firstOrNull()
                        val fileUrl = item?.url
                        if (fileUrl == null) {
                            setLoading(false, "No downloadable media found.")
                        } else {
                            if (item.thumb != null) showThumb(item.thumb)
                            startSystemDownload(fileUrl, isVideo = true)
                        }
                    }
                    else -> {
                        setLoading(false, res.text ?: getString(R.string.msg_fail))
                    }
                }
            } catch (e: Exception) {
                setLoading(false, "Error: ${e.message}")
            }
        }
    }

    private fun startSystemDownload(fileUrl: String, isVideo: Boolean) {
        try {
            val name = DownloadHelper.makeFileName(fileUrl, isVideo)
            DownloadHelper.enqueue(this, fileUrl, name)
            setLoading(false, getString(R.string.msg_done) + " ($name)")
        } catch (e: Exception) {
            setLoading(false, "${getString(R.string.msg_fail)} (${e.message})")
        }
    }

    private fun showThumb(thumbUrl: String?) {
        if (thumbUrl == null) { ivThumb.visibility = View.GONE; return }
        ivThumb.visibility = View.VISIBLE
        Glide.with(this).load(thumbUrl).into(ivThumb)
    }

    private fun setLoading(loading: Boolean, msg: String) {
        btnDownload.isEnabled = !loading
        btnDownload.text = if (loading) getString(R.string.btn_downloading) else getString(R.string.btn_download)
        progress.visibility = if (loading) View.VISIBLE else View.GONE
        if (!loading || progress.isIndeterminate) progress.isIndeterminate = loading
        tvStatus.text = msg
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001
                )
            }
        }
    }

    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}
