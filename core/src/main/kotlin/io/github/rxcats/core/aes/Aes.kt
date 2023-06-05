package io.github.rxcats.core.aes

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object Aes {
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val DEFAULT_SECRET_KEY = "92d736d1b9af42dc89e1103ede1d7628"

    @OptIn(ExperimentalEncodingApi::class)
    fun encode(value: String, secretKey: String = DEFAULT_SECRET_KEY): Result<String> = runCatching {
        val iv = secretKey.substring(0, 16)
        val textBytes = value.toByteArray(Charsets.UTF_8)
        val ivSpec = IvParameterSpec(iv.toByteArray(Charsets.UTF_8))
        val newKey = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), ALGORITHM)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)
        Base64.Default.encode(cipher.doFinal(textBytes))
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun decode(value: String, secretKey: String = DEFAULT_SECRET_KEY): Result<String> = runCatching {
        val iv = secretKey.substring(0, 16)
        val textBytes = Base64.Default.decode(value)
        val ivSpec = IvParameterSpec(iv.toByteArray(Charsets.UTF_8))
        val newKey = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), ALGORITHM)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
        String(cipher.doFinal(textBytes), Charsets.UTF_8)
    }
}
