package dev.joseluisgs.utils

import java.awt.Desktop
import java.io.File

fun openInBrowser(file: File) {
    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(file.toURI())
    }
}
