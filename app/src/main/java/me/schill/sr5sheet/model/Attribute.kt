package me.schill.sr5sheet.model

import android.databinding.Bindable
import me.schill.sr5sheet.BR
import me.schill.sr5sheet.persistence.Entity

class AttributeType : Entity() {
    @Bindable
    var name = "?"
        set (value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    override fun toString() = name
}