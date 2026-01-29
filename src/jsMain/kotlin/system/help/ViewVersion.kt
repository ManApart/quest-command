package system.help

import kotlinx.browser.window
import kotlinx.coroutines.await

actual suspend fun getVersion(): String {
    return window.fetch("version.txt").await().text().await().trim()
}