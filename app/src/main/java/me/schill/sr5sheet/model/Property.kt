package me.schill.sr5sheet.model

import me.schill.sr5sheet.persistence.Entity
import me.schill.sr5sheet.persistence.Ref

abstract class Property : Entity() {
	abstract val valueDisplayString: String
	abstract val type: Ref<out PropertyType>
	val name: String
		get() = type.get().name
}