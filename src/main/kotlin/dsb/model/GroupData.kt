package dsb.model


/**
 * [GroupData] ist für das Speichern der Einträge einer einzelnen (Schul-)Klasse zuständig
 *
 * @param groupID Klassenname z.B. 5a, 7c, ...
 * @param groupEntries Einträge der Klasse bestehend aus [GroupEntry]s
 * */
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

    fun getJSONObject(){
        /*
        * JSON-Array(RepresentationPlan) of JSON-Object with JSON-Array(GroupData)
        * in dem JSON-Array(GroupData): JSON-Array(GroupEntry)
        * in JSON-Array(Group-Entry):JSON-Object
        *
        *
        *
        * */
    }


    /**[Builder] ist eine Building Klasse um eine Instanz von [GroupData] einfach und übersichtlich zu erstellen*/
    data class Builder(var groupID: String? = null,
            var groupEntries: ArrayList<GroupEntry> = ArrayList()){


        fun groupID(groupID: String) = apply{ this.groupID = groupID}

        fun addGroupEntry(groupEntry: GroupEntry) = apply { this.groupEntries.add(groupEntry) }


        fun build(): GroupData {
            requireNotNull(groupID){"groupID must not be null"}
            return GroupData(groupID!!, groupEntries.toTypedArray())
        }
    }
}
