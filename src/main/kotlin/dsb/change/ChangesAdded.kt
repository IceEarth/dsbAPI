package dsb.change

import dsb.event.DSBAddEvent
import dsb.event.DSBEvent
import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.ModelUtil
import dsb.model.RepresentationPlan

open class ChangesAdded internal constructor(new: Array<RepresentationPlan>, old: Array<RepresentationPlan>) : ChangeData<DSBAddEvent>(new, old) {


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

    override fun getNullEntries(newPlan: RepresentationPlan): Array<DSBEvent> {
        return ModelUtil.getAddEventsOfPlan(newPlan)
    }


}