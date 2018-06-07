package me.schill.sr5sheet.model

import android.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonProperty
import me.schill.sr5sheet.BR
import me.schill.sr5sheet.persistence.Ref

class IntegerProperty() : Property() {
	constructor(type: IntegerPropertyType) : this() {
		this.type = Ref(type)
		value = type.defaultValue
	}

	@JsonProperty
	@Bindable
	override
	lateinit var type: Ref<IntegerPropertyType>

	@JsonProperty
	@Bindable
	var value = 0
		set(value) {
			field = value
			notifyPropertyChanged(BR.value)
			notifyPropertyChanged(BR.valueDisplayString)
		}

	override val valueDisplayString: String
		get() = value.toString()
}