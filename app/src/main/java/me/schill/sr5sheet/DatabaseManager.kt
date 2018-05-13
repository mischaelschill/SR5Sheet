package me.schill.sr5sheet

import me.schill.sr5sheet.model.Database
import me.schill.sr5sheet.model.Settings
import me.schill.sr5sheet.persistence.Persistence
import me.schill.sr5sheet.persistence.Ref
import java.io.File

class DatabaseManager {

	lateinit var settings: Settings
	lateinit var database: Database
	private val loggerUnit = javaClass.simpleName

	fun init(filesDir: File) {
		Persistence.init(filesDir)
		val settingsRef = Ref(Settings())
		settings = settingsRef.get()
		database = settings.database.get()
	}

	fun save() {
		Persistence.save(database)
	}
}