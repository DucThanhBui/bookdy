package com.example.bookdy.utils

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.readium.r2.shared.util.Try
import org.readium.r2.shared.util.file.FileSystemError

suspend fun InputStream.toFile(file: File): Try<Unit, FileSystemError> =
    try {
        Try.success(toFileUnsafe(file))
    } catch (e: IOException) {
        Try.failure(FileSystemError.IO(e))
    } catch (e: FileNotFoundException) {
        Try.failure(FileSystemError.IO(e))
    } catch (e: SecurityException) {
        Try.failure(FileSystemError.Forbidden(e))
    }

suspend fun InputStream.toFileUnsafe(file: File) {
    checkNotNull(file.parentFile).mkdirs()
    file.createNewFile()
    try {
        withContext(Dispatchers.IO) {
            use { input ->
                file.outputStream().use { input.copyTo(it) }
            }
        }
    } catch (e: Exception) {
        tryOrLog { file.delete() }
        throw e
    }
}

suspend fun InputStream.copyToNewFile(dir: File): Try<File, FileSystemError> {
    val filename = UUID.randomUUID().toString()
    val file = File(dir, filename)
    return toFile(file).map { file }
}