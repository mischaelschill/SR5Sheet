package me.schill.sr5sheet.model

import android.databinding.Bindable
import me.schill.sr5sheet.BR
import me.schill.sr5sheet.persistence.Entity
import java.util.*

class AttributeType : Entity {
    @Bindable
    var name = "?"
        set (value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

	constructor(id: UUID, name: String) : super(id) {
		this.name = name
	}

	constructor() : super() {
	}

	override fun toString() = name
}