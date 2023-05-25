package dsb.change

import dsb.event.DSBAddEvent
import dsb.event.DSBEvent
import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.RepresentationPlan

class ChangesAdded(new: Array<RepresentationPlan>, old: Array<RepresentationPlan>) : ChangeData<DSBAddEvent>(old, new) {
    override val newestFirst: Boolean = true

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