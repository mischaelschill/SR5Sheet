package me.schill.sr5sheet

import me.schill.sr5sheet.model.SR5Character
import me.schill.sr5sheet.model.Settings
import me.schill.sr5sheet.persistence.Persistence
import me.schill.sr5sheet.persistence.Ref
import java.io.File

class CharacterManager {
	lateinit var settings: Settings
	lateinit var current: SR5Character
	private val loggerUnit = javaClass.simpleName

    fun init(filesDir: File) {
	    Persistence.init(filesDir)
	    val settingsRef = Ref(Settings())
	    settings = settingsRef.get()
	    val loadedCurrent = settings.currentCharacter?.get()
	    if (loadedCurrent != null) {
		    current = loadedCurrent
	    } else {
		    current = SR5Character()
		    settings.currentCharacter = Ref(current)
		    settings.characters.add(Ref(current))
	    }
    }

	fun save() {
		Persistence.save(current)
	}
}