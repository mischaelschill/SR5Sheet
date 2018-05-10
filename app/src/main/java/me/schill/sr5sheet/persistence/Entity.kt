package me.schill.sr5sheet.persistence

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonProperty
import me.schill.sr5sheet.BR
import java.util.*

abstract class Entity(id: UUID) : BaseObservable() {
	constructor() : this(UUID.randomUUID())

	@Bindable
	@JsonProperty
	var id = id
		set(value) {
			field = value
			notifyPropertyChanged(BR.id)
		}
	var className: String
		@JsonProperty
		get() = javaClass.name
		set(className) {
			if (className != javaClass.name) {
				throw IllegalStateException("Reading " + className + " into " + javaClass.name)
			}
		}

	@get:JsonAnyGetter
	var extras = HashMap<String, Any>()


	@JsonAnySetter
	fun setExtra(key: String, value: Any) {
		extras[key] = value;
	}
}