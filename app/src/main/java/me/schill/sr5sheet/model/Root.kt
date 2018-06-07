package me.schill.sr5sheet.model

import android.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonProperty
import me.schill.sr5sheet.BR
import me.schill.sr5sheet.persistence.Entity
import me.schill.sr5sheet.persistence.Ref
import java.util.*

class Root : Entity(UUID.fromString("06525434-e507-499e-bf50-38ea562876b1")) {
	@JsonProperty
	@Bindable
	var characters: List<Ref<SR5Character>> = ArrayList()
		set(value) {
			field = value
			notifyPropertyChanged(BR.characters)
		}

	@JsonProperty
	@Bindable
	val database: Ref<Database> = Ref(Database())
}