package dsb.model

import dsb.model.GroupData.Builder

/**
 * [GroupEntry] ist eine Data-Klasse um die Werte von einem Eintrag zu speichern
 *
 * @param hour Die Stunde in der die Vertretung stattfindet (Nicht Nullable!)
 * @param teacher Der Lehrer der die Stunde vertritt (Nicht Nullable!)
 * @param subject Das Fach in dem die Vertretung stattfindet (Nicht Nullable!)
 * @param room Der Raum in dem die Vertretung stattfindet
 * @param text Ein zusätzlicher Text der hinzugefügt werden kann
 * */

data class GroupEntry internal constructor(val hour: Int, val teacher: String, val subject: String, val room: String?, val text: String?){

    /**[Builder] ist eine Building Klasse um eine Instanz von [GroupEntry] einfach und übersichtlich zu erstellen*/

    override fun toString(): String {
        return "Hour: $hour\nTeacher: $teacher\nSubject: $subject\nRoom: $room\nText: $text"
    }

    @Suppress("only-testing")

    fun toTrimmedString(): String = toString().replace("\n", " ")
    internal data class Builder(var hour: Int? = null,
            var teacher: String? = null,
            var subject: String? = null,
            var room: String? = null,
            var text: String? = null){


        fun hour(hour: Int) = apply { this.hour = hour }
        fun teacher(teacher: String) = apply { this.teacher = teacher }

        fun subject(subject: String) = apply { this.subject = subject }

        fun room(room: String) = apply { this.room = room }

        fun text(text: String) = apply { this.text = text }

        fun build(): GroupEntry {
            requireNotNull(hour) { "hour must not be null" }
            requireNotNull(teacher) { "teacher must not be null" }
            requireNotNull(subject) { "subject must not be null" }
            return GroupEntry(hour!!, teacher!!, subject!!, room, text)
        }
    }
}
