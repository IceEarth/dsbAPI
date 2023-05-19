package data

import entry.GroupEntry

class GroupDataBuilder {
    private var groupID: String? = null
    private var groupEntries: ArrayList<GroupEntry> = ArrayList()

    fun setGroupID(groupID: String): GroupDataBuilder{
        this.groupID = groupID
        return this
    }

    fun addGroupEntry(groupEntry: GroupEntry): GroupDataBuilder{
        this.groupEntries.add(groupEntry)
        return this
    }

    fun build(): GroupData{
        requireNotNull(groupID){"groupID must not be null"}
        return GroupData(groupID!!, groupEntries.toTypedArray())
    }
}