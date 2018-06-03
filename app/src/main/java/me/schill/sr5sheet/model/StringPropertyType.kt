package me.schill.sr5sheet.model

import android.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class StringPropertyType : PropertyType {

	@JsonProperty
	@Bindable
	var defaultValue = ""

	constructor(id: UUID, name: String, defaultValue: String) : super(id, name) {
		this.defaultValue = defaultValue
	}

	constructor(name: String, defaultValue: String) : super(name) {
		this.defaultValue = defaultValue
	}

	constructor() : super() {
	}

	override fun createInstance() = StringProperty(this)


}