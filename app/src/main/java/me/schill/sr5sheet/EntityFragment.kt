package me.schill.sr5sheet

import android.os.Bundle
import me.schill.sr5sheet.persistence.Entity
import me.schill.sr5sheet.persistence.Persistence
import java.util.*

abstract class EntityFragment<T : Entity>(val entityType: Class<T>) : TitledFragment() {
	lateinit var entity: T
		private set
	private var loadedCallbacks = ArrayList<(EntityFragment<T>) -> Unit>()
	private var loaded = false
		set(value) {
			if (value) {
				loadedCallbacks.forEach {
					it(this)
				}
				loadedCallbacks.clear()
			}
			field = value
		}

	companion object {
		const val ARG_ID = "entityId"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			it.getString(ARG_ID)?.let {
				entity = Persistence.load(UUID.fromString(it), entityType);
				loaded = true
			}
		}
		if (!loaded) {
			entity = entityType.newInstance()
			loaded = true
		}
	}

	fun onLoaded(callback: (EntityFragment<T>) -> Unit) {
		if (loaded) {
			callback(this)
		} else {
			loadedCallbacks.add(callback)
		}
	}
}