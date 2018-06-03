package me.schill.sr5sheet.model

import android.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class IntegerPropertyType : PropertyType {
	@JsonProperty
	@Bindable
	var minValue = Integer.MIN_VALUE

	@JsonProperty
	@Bindable
	var maxValue = Integer.MAX_VALUE

	@JsonProperty
	@Bindable
	var defaultValue = 0

	constructor(id: UUID, name: String, minValue: Int, maxValue: Int, defaultValue: Int) : super(id, name) {
		this.minValue = minValue
		this.maxValue = maxValue
		this.defaultValue = defaultValue
	}

	constructor() {

	}

	override fun createInstance() = IntegerProperty(this)
}

