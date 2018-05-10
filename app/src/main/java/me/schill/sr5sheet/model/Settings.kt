package me.schill.sr5sheet.model

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import com.fasterxml.jackson.annotation.JsonProperty
import me.schill.sr5sheet.BR
import me.schill.sr5sheet.persistence.Entity
import me.schill.sr5sheet.persistence.Ref
import java.util.*

class Settings : Entity(UUID.fromString("b6525434-e507-499e-bf50-38ea562876b0")) {
	@JsonProperty
	@Bindable
	var currentCharacter: Ref<SR5Character>? = null
		set(value) {
			field = value
			notifyPropertyChanged(BR.currentCharacter)
		}

	@JsonProperty
	@Bindable
	val characters: ObservableArrayList<Ref<SR5Character>> = ObservableArrayList()

	@JsonProperty
	@Bindable
	val database: Ref<Database> = Ref(Database())
}