package me.schill.sr5sheet

import android.os.Environment
import android.util.Log
import me.schill.sr5sheet.model.SR5Character
import me.schill.sr5sheet.model.Settings
import java.io.File
import org.simpleframework.xml.core.Persister
import java.lang.Exception

object CharacterManager {
    var settings: Settings
    var current: SR5Character
    val serializer = Persister()
    val settingsFile: File

    init {
        settingsFile = Environment.getDataDirectory().resolve("settings.xml")
        if (settingsFile.exists()) {
            try {
                settings = serializer.read(Settings::class.java, settingsFile.inputStream())
            } catch (ex: Exception) {
                settings = Settings()
            }
        } else {
            settings = Settings()
        }

        val name =  settings.currentCharacter

        if (name != null) {
            current = loadImpl(name) ?: SR5Character()
        } else {
            current = SR5Character()
        }

        settings.currentCharacter = current.name;
        saveSettings()
    }

    fun load(name: String): Boolean {
        var result = false
        loadImpl(name)?.let {
            current = it
            result = true
        }
        return result;
    }

    private fun loadImpl(name: String): SR5Character? {
        val file = Environment.getDataDirectory().resolve("$name.xml")
        if (file.exists()) {
            try {
                val character = serializer.read(SR5Character::class.java, file.inputStream())
                settings.currentCharacter = name;
                saveSettings()
                return character
            } catch (ex: Exception) {
                Log.e("CharacterManager", "Trying to load character", ex);
            }
        }
        return null
    }

    private fun saveSettings() {
        try {
            settingsFile.outputStream().use {
                serializer.write(settings, it);
            }
        } catch (ex: Exception) {
            Log.e("CharacterManager", "Trying to save settings", ex);
        }
    }

    fun save(): Boolean {
        val name = current.name
        val file = Environment.getDataDirectory().resolve("$name.xml")
        try {
            file.outputStream().use {
                serializer.write(current, it)
            }
            val oldName = settings.currentCharacter
            if (oldName != name && oldName != null) {
                try {
                    val oldFile = Environment.getDataDirectory().resolve("$oldName.xml");
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                } catch (ex: Exception) {
                    Log.e("CharacterManager", "Trying to delete old file", ex);
                }
                settings.currentCharacter = name;
            }
            saveSettings()
            return true
        } catch (ex: Exception) {
            Log.e("CharacterManager", "Trying to save character", ex);
        }
        return false
    }
}