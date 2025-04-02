package dev.joseluisgs.common.storage

import com.github.michaelbull.result.Result

interface FileStorage<T, E> {
    fun loadFromFile(file: String): Result<T, E>
    fun saveToFile(data: T, file: String): Result<T, E>
}