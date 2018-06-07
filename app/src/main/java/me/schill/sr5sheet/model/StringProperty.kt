package me.schill.sr5sheet.model

import android.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonProperty
import me.schill.sr5sheet.BR
import me.schill.sr5sheet.persistence.Ref

class StringProperty() : Property() {
	constructor(type: StringPropertyType) : this() {
		this.type = Ref(type)
		value = type.defaultValue
	}

	@JsonProperty
	@Bindable
	override
	lateinit var type: Ref<StringPropertyType>

	@JsonProperty
	@Bindable
	var value = ""
		set(value) {
			field = value
			notifyPropertyChanged(BR.value)
			notifyPropertyChanged(BR.valueDisplayString)
		}

	override val valueDisplayString: String
		get() = value
}