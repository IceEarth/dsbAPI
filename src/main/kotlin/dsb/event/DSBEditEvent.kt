package dsb.event

import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.RepresentationPlan

data class DSBEditEvent(private val newEntry: GroupEntry, val oldEntry: GroupEntry, private val editTypes: Array<EditType>, val groupData: GroupData, val representationPlan: RepresentationPlan) : DSBEvent {

   companion object{

   }

    enum class EditType{
        TEACHER_EDITED, ROOM_EDITED, TEXT_EDITED
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DSBEditEvent

        if (newEntry != other.newEntry) return false
        if (oldEntry != other.oldEntry) return false
        if (!editTypes.contentEquals(other.editTypes)) return false
        if (groupData != other.groupData) return false
        return representationPlan == other.representationPlan
    }

    override fun hashCode(): Int {
        var result = newEntry.hashCode()
        result = 31 * result + oldEntry.hashCode()
        result = 31 * result + editTypes.contentHashCode()
        result = 31 * result + groupData.hashCode()
        result = 31 * result + representationPlan.hashCode()
        return result
    }
}
