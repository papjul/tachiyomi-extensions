package eu.kanade.tachiyomi.extension.pt.silencescan

import eu.kanade.tachiyomi.multisrc.mangathemesia.MangaThemesia
import eu.kanade.tachiyomi.network.interceptor.rateLimit
import eu.kanade.tachiyomi.source.model.SManga
import okhttp3.OkHttpClient
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class SilenceScan : MangaThemesia(
    "Silence Scan",
    "https://silencescan.com.br",
    "pt-BR",
    dateFormat = SimpleDateFormat("MMMMM dd, yyyy", Locale("pt", "BR"))
) {

    override val versionId: Int = 2

    override val client: OkHttpClient = network.cloudflareClient.newBuilder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .rateLimit(1, 2, TimeUnit.SECONDS)
        .build()

    override val altNamePrefix = "Nome alternativo: "

    override fun String?.parseStatus() = when {
        this == null -> SManga.UNKNOWN
        this.contains("Em Andamento") -> SManga.ONGOING
        this.contains("Completo") -> SManga.COMPLETED
        else -> SManga.UNKNOWN
    }
}
