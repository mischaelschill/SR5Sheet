package me.schill.sr5sheet

import me.schill.sr5sheet.model.Root
import me.schill.sr5sheet.model.SR5Character
import me.schill.sr5sheet.persistence.Persistence
import me.schill.sr5sheet.persistence.Ref
import java.io.File

class CharacterManager {
	lateinit var root: Root
	lateinit var current: SR5Character
	private val loggerUnit = javaClass.simpleName

    fun init(filesDir: File) {
	    Persistence.init(filesDir)
	    val rootRef = Ref(Root())
	    root = rootRef.get()
	    val loadedCurrent = root.currentCharacter?.get()
	    if (loadedCurrent != null) {
		    current = loadedCurrent
	    } else {
		    current = SR5Character()
		    root.currentCharacter = Ref(current)
		    root.characters.add(Ref(current))
	    }
    }

	fun save() {
		Persistence.save(current)
	}
}