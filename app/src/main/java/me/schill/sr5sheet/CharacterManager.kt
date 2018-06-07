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
	    val loadedCurrent = root.characters.firstOrNull()?.get()
	    if (loadedCurrent != null) {
		    current = loadedCurrent
	    } else {
		    current = SR5Character()
		    current.postDeserialize()
		    Persistence.save(current)?.let {
			    root.characters += it
		    }
	    }
    }

	fun save() {
		Persistence.save(current)
	}
}