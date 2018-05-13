package me.schill.sr5sheet.persistence

import android.databinding.Observable
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.lang.Exception
import java.util.*

object Persistence : Observable.OnPropertyChangedCallback() {
	private lateinit var filesDir: File
	private var initialized = false;
	private val cache = HashMap<UUID, Entity>();
	private val mapper = ObjectMapper().registerModule(KotlinModule())
	val loggerUnit = javaClass.simpleName

	fun init(filesDir: File) {
		if (!initialized) {
			initialized = true
			this.filesDir = filesDir
			Log.i(loggerUnit, "Initializing with files dir: " + filesDir.toString())
		}
	}

	override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
		Log.i(loggerUnit, "Received property changed: " + sender.toString())
		synchronized(this, {
			if (sender is Entity) {
				save(sender)
			}
		})
	}

	@Suppress("UNCHECKED_CAST")
	fun <T : Entity> load(id: UUID, cl: Class<T>): T {
		Log.i(loggerUnit, "Loading object: " + id.toString() + " (" + cl.name + ")")
		try {
			synchronized(this, {
				if (cache.containsKey(id)) {
					val cached = cache.get(id)
					if (cl.isInstance(cached)) {
						Log.i(loggerUnit, "Loading from cache")
						return cached as T
					}
				} else {
					val file = File(filesDir, id.toString() + ".json")
					if (file.exists()) {
						Log.i(loggerUnit, "Loading from file: " + file.toString())
						val loaded = mapper.readValue(file, cl)
						cache[loaded.id] = loaded
						loaded.addOnPropertyChangedCallback(this)
						return loaded
					}
				}
			})
		} catch (ex: Exception) {
			Log.e(loggerUnit, "Error while trying to load: " + id.toString() + " (" + cl.name + ")", ex)
		}
		Log.i(loggerUnit, "Creating new entity")
		val entity = cl.newInstance()
		entity.id = id
		entity.addOnPropertyChangedCallback(this)
		return entity
	}

	fun save(entity: Entity): Boolean {
		Log.i(loggerUnit, "Saving object: " + entity.id.toString() + " (" + entity.className + ")")
		try {
			synchronized(this, {
				if (!cache.containsKey(entity.id)) {
					cache[entity.id] = entity;
				}
				mapper.writeValue(File(filesDir, entity.id.toString() + ".json"), entity)
			})
			return true
		} catch (ex: Exception) {
			Log.e(loggerUnit, "Error while trying to save: " + entity.id.toString() + " (" + entity.className + ")", ex)
		}
		return false
	}
}