package me.schill.sr5sheet.persistence

import java.util.*

data class Ref<T : Entity>(val id: UUID, val className: String) {

	constructor(entity: T) : this(entity.id, entity.className)

	@Suppress("UNCHECKED_CAST")
	fun get(): T {
		return Persistence.load(id, Class.forName(className) as Class<T>)
	}
}