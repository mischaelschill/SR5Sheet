package me.schill.sr5sheet

import android.databinding.Observable
import android.util.Log
import me.schill.sr5sheet.model.SR5Character
import me.schill.sr5sheet.model.Settings
import org.simpleframework.xml.core.Persister
import java.io.File
import java.lang.Exception
import java.util.*

class CharacterManager: Observable.OnPropertyChangedCallback() {
    var settings = Settings()
    var current = SR5Character()
    private var currentId = UUID::randomUUID.toString()
    private val serializer = Persister()
    private lateinit var filesDir:File
    private lateinit var charDir:File
    private val settingsFile
        get() = filesDir.resolve("settings.xml")
    private val loggerUnit = "CharacterManager"

    fun init(filesDir: File) {
        this.filesDir = filesDir
        this.charDir = filesDir.resolve("char")
        charDir.mkdirs()

        if (settingsFile.exists()) {
            Log.i(loggerUnit, "Found settings file")
            try {
                settings = serializer.read(Settings::class.java, settingsFile.inputStream())
            } catch (ex: Exception) {
                Log.w(loggerUnit, "Unable to load settings file because of " + ex.message)
                settings = Settings()
            }
        } else {
            Log.i(loggerUnit, "Created new settings file")
            settings = Settings()
        }

        val charId = settings.currentCharacter

        if (charId != null) {
            Log.i(loggerUnit, "Trying to load previous character: " + charId)
            loadImpl(charId)?.let {
                current = it
                currentId = charId
                Log.i(loggerUnit, "Successfully loaded character")
            }
        }
        current.addOnPropertyChangedCallback(this)
        settings.currentCharacter = currentId
        saveSettings()
    }

    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        save()
    }

    fun load(charId: String): Boolean {
        var result = false
        loadImpl(charId)?.let {
            current.removeOnPropertyChangedCallback(this)
            current = it
            current.addOnPropertyChangedCallback(this)
            currentId = charId
            settings.currentCharacter = charId
            saveSettings()
            result = true
        }
        return result
    }

    private fun loadImpl(charId: String): SR5Character? {
        val file = charDir.resolve("$charId.xml")
        if (file.exists()) {
            try {
                file.inputStream().use {
                    val character = serializer.read(SR5Character::class.java, it)
                    return character
                }
            } catch (ex: Exception) {
                Log.e("CharacterManager", "Trying to load character", ex)
            }
        }
        return null
    }

    private fun saveSettings() {
        try {
            settingsFile.outputStream().use {
                serializer.write(settings, it)
            }
        } catch (ex: Exception) {
            Log.e("CharacterManager", "Trying to save settings", ex)
        }
    }

    fun save(): Boolean {
        val file = charDir.resolve("$currentId.xml")
        try {
            file.outputStream().use {
                serializer.write(current, it)
            }
            return true
        } catch (ex: Exception) {
            Log.e("CharacterManager", "Trying to save character", ex)
        }
        return false
    }
}