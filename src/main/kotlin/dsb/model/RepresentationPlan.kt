package dsb.model

import java.util.Date

/**
 * [RepresentationPlan] ist für die Speicherung eines Plans für einen ganzen Tag zuständig.
 *
 * @param date Datum des Plans
 * @param data Einträge der Klassen die an diesem Tag eine Vertretung haben
 *
 * */
data class RepresentationPlan internal constructor(val date: Date, val data: Array<GroupData>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RepresentationPlan

        if (date != other.date) return false
        if (data != null) {
            if (other.data == null) return false
            if (!data.contentEquals(other.data)) return false
        } else if (other.data != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + (data?.contentHashCode() ?: 0)
        return result
    }

    /**[Builder] ist eine Building Klasse um eine Instanz von [RepresentationPlan] einfach und übersichtlich zu erstellen*/
    internal data class Builder(var date: Date? = null,
                       var groupDataEntries: ArrayList<GroupData> = ArrayList()){

        fun date(date: Date) = apply { this.date = date }

        fun addData(groupData: GroupData) = apply { this.groupDataEntries.add(groupData) }

        fun build(): RepresentationPlan {
            requireNotNull(date){"date must not be null"}
            return RepresentationPlan(date!!, groupDataEntries.toTypedArray())
        }

    }
}
