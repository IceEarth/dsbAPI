package plan

import data.GroupData
import java.util.Date

data class RepresentationPlan(private val date: Date, private val groupEntries: Array<GroupData>?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RepresentationPlan

        if (date != other.date) return false
        if (groupEntries != null) {
            if (other.groupEntries == null) return false
            if (!groupEntries.contentEquals(other.groupEntries)) return false
        } else if (other.groupEntries != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + (groupEntries?.contentHashCode() ?: 0)
        return result
    }
}
