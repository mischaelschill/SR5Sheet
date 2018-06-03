package me.schill.sr5sheet.model

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import com.fasterxml.jackson.annotation.JsonProperty
import me.schill.sr5sheet.BR
import me.schill.sr5sheet.persistence.Entity
import me.schill.sr5sheet.persistence.Persistence
import me.schill.sr5sheet.persistence.Ref
import java.util.*

class SR5Character : Entity() {
	@Bindable
	@JsonProperty
	var name = "Anonymous"
		set(value) {
			field = value
			notifyPropertyChanged(BR.name)
		}

	@JsonProperty
	val properties = ObservableArrayList<Ref<Property>>()
		get() {
			val properties = HashSet<UUID>()
			field.forEach { properties.add(it.get().type.id) }
			BuiltinDatabase.Properties.all
					.filterKeys { !properties.contains(it) }
					.forEach({ _, type ->
						Persistence.save(type.createInstance())?.let {
							field.add(it)
						}
					})
			return field
		}

	@Bindable
	@JsonProperty
	var metatype = "Mensch"
		set(value) {
			field = value
			notifyPropertyChanged(BR.metatype)
		}

	@Bindable
	@JsonProperty
	var ethnicity = ""
		set(value) {
			field = value
			notifyPropertyChanged(BR.ethnicity)
		}

	@Bindable
	@JsonProperty
	var age = 21
		set(value) {
			field = value
			notifyPropertyChanged(BR.age)
		}

	@Bindable
	@JsonProperty
	var sex = "?"
		set(value) {
			field = value
			notifyPropertyChanged(BR.sex)
		}

	@Bindable
	@JsonProperty
	var height = 180
		set(value) {
			field = value
			notifyPropertyChanged(BR.height)
		}

	@Bindable
	@JsonProperty
	var weight = 80
		set(value) {
			field = value
			notifyPropertyChanged(BR.weight)
		}

	@Bindable
	@JsonProperty
	var streetCred = 0
		set(value) {
			field = value
			notifyPropertyChanged(BR.streetCred)
		}

	@Bindable
	@JsonProperty
	var notoriety = 0
		set(value) {
			field = value
			notifyPropertyChanged(BR.notoriety)
		}

	@Bindable
	@JsonProperty
	var publicAwareness = 0
		set(value) {
			field = value
			notifyPropertyChanged(BR.publicAwareness)
		}

	@Bindable
	@JsonProperty
	var karma = 0
		set(value) {
			field = value
			notifyPropertyChanged(BR.karma)
		}

	@Bindable
	@JsonProperty
	var totalKarma = 0
		set(value) {
			field = value
			notifyPropertyChanged(BR.totalKarma)
		}

	override fun toString() = name
}