package me.schill.sr5sheet.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import me.schill.sr5sheet.BR
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root
class SR5Character : BaseObservable() {
    @Element
    @Bindable
    var name = "Anonymous"
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }
    @Element
    @Bindable
    var metatype = "Mensch"
        set(value) {
            field = value
            notifyPropertyChanged(BR.metatype)
        }
    @Element
    @Bindable
    var specialAttribute: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.specialAttribute)
        }

}