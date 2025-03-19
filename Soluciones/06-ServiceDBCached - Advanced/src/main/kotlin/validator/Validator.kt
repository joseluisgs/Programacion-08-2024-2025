package dev.joseluisgs.validator

interface Validator<T> {
    fun validate(t: T): T
}
