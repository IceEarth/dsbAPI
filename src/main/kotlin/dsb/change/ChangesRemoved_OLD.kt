package dsb.change

import dsb.event.DSBAddEvent
import dsb.event.DSBEvent
import dsb.event.DSBRemoveEvent
import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.RepresentationPlan

class ChangesRemoved_OLD (new: Array<RepresentationPlan>, old: Array<RepresentationPlan>) : ChangeData<DSBRemoveEvent>(old, new) {
    override val newestFirst: Boolean = false

    override fun getChangesOfEntry(
        newEntry: GroupEntry,
        oldEntry: GroupEntry?,
        groupData: GroupData,
        representationPlan: RepresentationPlan
    ): DSBEvent? {
        if(oldEntry == null){
            return DSBAddEvent(newEntry, groupData, representationPlan)
        }
        return null
    }


}