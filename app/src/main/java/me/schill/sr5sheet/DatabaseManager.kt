package me.schill.sr5sheet

import me.schill.sr5sheet.model.Database
import me.schill.sr5sheet.model.Root
import me.schill.sr5sheet.persistence.Persistence
import me.schill.sr5sheet.persistence.Ref
import java.io.File

class DatabaseManager {

	lateinit var root: Root
	lateinit var database: Database
	private val loggerUnit = javaClass.simpleName

	fun init(filesDir: File) {
		Persistence.init(filesDir)
		val rootRef = Ref(Root())
		root = rootRef.get()
		database = root.database.get()
	}

	fun save() {
		Persistence.save(database)
	}
}