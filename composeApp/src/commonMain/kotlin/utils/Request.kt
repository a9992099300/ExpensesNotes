package utils

import io.github.aakira.napier.Napier

inline fun <reified R> simpleRequest(
    request: () -> R?
): Result<R> =
    try {
        val result = request()
        if (result != null) {
            Result.Success(result)
        } else {
            Result.Error(IllegalArgumentException())
        }
    } catch (e: Exception) {
        Napier.d("Exception ${e} mes ${e.message} cause ${e.cause}", tag = "testMain")
        Result.Error(e)
    }

inline fun <T> String?.allowRequest(request: (String) -> T): T? =
    if (this != null) {
        request(this)
    } else {
        null
    }