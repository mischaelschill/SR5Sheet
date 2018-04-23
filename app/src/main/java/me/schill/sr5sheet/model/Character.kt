package me.schill.sr5sheet.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import me.schill.sr5sheet.BR
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root
class SR5Character() : BaseObservable() {


    @Bindable
    @field:Element
    var name = "Anonymous"
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @Bindable
    @field:Element
    var metatype = "Mensch"
        set(value) {
            field = value
            notifyPropertyChanged(BR.metatype)
        }

    @Bindable
    @field:Element(required = false)
    var ethnicity = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ethnicity)
        }

    @Bindable
    @field:Element(required = false)
    var age = 21
        set(value) {
            field = value
            notifyPropertyChanged(BR.age)
        }

    @Bindable
    @field:Element(required = false)
    var sex = "?"
        set(value) {
            field = value
            notifyPropertyChanged(BR.sex)
        }

    @Bindable
    @field:Element(required = false)
    var height = 180
        set(value) {
            field = value
            notifyPropertyChanged(BR.height)
        }

    @Bindable
    @field:Element(required = false)
    var weight = 80
        set(value) {
            field = value
            notifyPropertyChanged(BR.weight)
        }

    @Bindable
    @field:Element(required = false)
    var streetCred = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.streetCred)
        }

    @Bindable
    @field:Element(required = false)
    var notoriety = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.notoriety)
        }

    @Bindable
    @field:Element(required = false)
    var publicAwareness = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.publicAwareness)
        }

    @Bindable
    @field:Element(required = false)
    var karma = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.karma)
        }

    @Bindable
    @field:Element(required = false)
    var totalKarma = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.totalKarma)
        }
}