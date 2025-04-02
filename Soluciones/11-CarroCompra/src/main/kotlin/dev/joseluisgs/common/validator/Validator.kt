package dev.joseluisgs.common.validator

import com.github.michaelbull.result.Result

interface Validator<T, E> {
    fun validate(t: T): Result<T, E>
}