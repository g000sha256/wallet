package ru.g000sha256.wallet.common

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import javax.inject.Inject
import javax.inject.Singleton
import ru.g000sha256.wallet.model.dbo.CardDbo

private const val KEY_SHOW_CATEGORIES = "show_categories"
private const val KEY_SORT = "sort"

@Singleton
class Storage @Inject constructor(private val gson: Gson, context: Context) {

    var showCategories: Boolean
        get() = sharedPreferences.getBoolean(KEY_SHOW_CATEGORIES, false)
        set(value) {
            sharedPreferences
                .edit()
                .putBoolean(KEY_SHOW_CATEGORIES, value)
                .apply()
        }

    var sort: Int
        get() = sharedPreferences.getInt(KEY_SORT, 0)
        set(value) {
            sharedPreferences
                .edit()
                .putInt(KEY_SORT, value)
                .apply()
        }

    private val file: File
    private val sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE)
    private val typeToken = object : TypeToken<List<CardDbo>>() {}

    init {
        val filesDirectory = context.filesDir
        filesDirectory.mkdirs()
        file = File(filesDirectory, "cards")
    }

    @Synchronized
    fun restore(): List<CardDbo>? {
        try {
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            return gson.fromJson(inputStreamReader, typeToken.type)
        } catch (throwable: Throwable) {
            return null
        }
    }

    @Synchronized
    fun save(cards: List<CardDbo>) {
        file.delete()
        file.createNewFile()
        val json = gson.toJson(cards, typeToken.type)
        val fileOutputStream = FileOutputStream(file)
        val outputStreamWriter = OutputStreamWriter(fileOutputStream)
        outputStreamWriter.use { it.append(json) }
    }

}