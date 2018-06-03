package me.schill.sr5sheet.model

import android.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonProperty
import me.schill.sr5sheet.persistence.Entity
import java.util.*

abstract class PropertyType : Entity {
	abstract fun createInstance(): Property

	@JsonProperty
	@Bindable
	var name = "?"

	constructor(id: UUID, name: String) : super(id) {
		this.name = name
	}

	constructor(name: String) : super() {
		this.name = name
	}

	constructor() : super() {

	}

}

