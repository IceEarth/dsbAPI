package data

import entry.GroupEntry

data class GroupData(val groupID: String, val groupEntries: Array<GroupEntry>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GroupData

        if (groupID != other.groupID) return false
        return groupEntries.contentEquals(other.groupEntries)
    }

    override fun hashCode(): Int {
        var result = groupID.hashCode()
        result = 31 * result + groupEntries.contentHashCode()
        return result
    }



    data class Builder(var groupID: String? = null,
            var groupEntries: ArrayList<GroupEntry> = ArrayList()){


        fun groupID(groupID: String) = apply{ this.groupID = groupID}

        fun addGroupEntry(groupEntry: GroupEntry) = apply { this.groupEntries.add(groupEntry) }


        fun build(): GroupData{
            requireNotNull(groupID){"groupID must not be null"}
            return GroupData(groupID!!, groupEntries.toTypedArray())
        }
    }
}
