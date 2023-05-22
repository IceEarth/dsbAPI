package dsb.event

data class DSBEditEvent(/*val groupEntry: GroupEntry, val groupData: GroupData, val representationPlan: RepresentationPlan*/val msg: String) :
    DSBEvent {

    fun getMessage() = this.msg
}
