package me.schill.sr5sheet.model

import android.databinding.Bindable
import android.databinding.Observable
import me.schill.sr5sheet.BR
import me.schill.sr5sheet.persistence.Entity
import me.schill.sr5sheet.persistence.Ref

abstract class Property : Entity() {
	private var nameBound = false

	@get:Bindable
	abstract val valueDisplayString: String

	@get:Bindable
	abstract val type: Ref<out PropertyType>

	@get:Bindable
	val name: String
		get() {
			val typeInstance = type.get()
			if (!nameBound) {
				nameBound = true
				typeInstance.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
					override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
						if (propertyId == BR.name) {
							notifyPropertyChanged(BR.name)
						}
					}
				})
			}
			return typeInstance.name
		}

	@Bindable
	var removed = false
		set(value) {
			field = value
			notifyPropertyChanged(BR.removed)
		}
}