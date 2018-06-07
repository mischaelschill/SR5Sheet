package me.schill.sr5sheet.model

import java.util.UUID.fromString

object BuiltinDatabase {
	object Properties {
		val metaType = StringPropertyType(
				fromString("087527a3-5099-48fd-beb1-1f7adcb4a481"),
				"Metatyp", "Mensch")

		val karma = IntegerPropertyType(
				fromString("2425b4b0-5353-4316-a4be-90a70f16d71a"),
				"Karma", 0, Int.MAX_VALUE, 0)

		val ethnicity = StringPropertyType(
				fromString("4b43af16-c8e1-482b-b2e5-cbee5905bd84"),
				"Ethnie", "")

		val sex = StringPropertyType(
				fromString("c8b24f2f-8014-4598-92b9-af10bd2a12bf"),
				"Geschlecht", "")

		val age = IntegerPropertyType(
				fromString("31c6083a-0f98-4252-b291-641f48d45864"),
				"Alter", 0, Int.MAX_VALUE, 0)

		val all = mapOf(
				metaType.id to metaType,
				karma.id to karma,
				ethnicity.id to ethnicity,
				sex.id to sex,
				age.id to age
		)

		val defaults = mapOf(
				metaType.id to metaType,
				karma.id to karma
		)
	}

	object Attributes {
		val strength = AttributeType(fromString("6a9ac523-feb5-4410-87a9-d89b42889077"), "Stärke")
		val all = mapOf(
				strength.id to strength)
	}

	val all =
			Properties.all + Attributes.all
}