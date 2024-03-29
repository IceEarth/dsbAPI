package dsb.change

import dsb.event.DSBAddEvent
import dsb.event.DSBEditEvent
import dsb.event.DSBEvent
import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.RepresentationPlan

class ChangesEdited internal constructor(new: Array<RepresentationPlan>, old: Array<RepresentationPlan>) : ChangeData<DSBAddEvent>(new, old) {


    override fun getChangesOfEntry(
        newEntry: GroupEntry,
        oldEntry: GroupEntry?,
        groupData: GroupData,
        representationPlan: RepresentationPlan
    ): DSBEvent? {

        if(newEntry != oldEntry && oldEntry != null){
            //println("\n${oldEntry} \t\t$newEntry")
            val editTypes: ArrayList<DSBEditEvent.EditType> = ArrayList()

            if(newEntry.room != oldEntry.room){
                editTypes.add(DSBEditEvent.EditType.ROOM_EDITED)
            }
            if(newEntry.teacher != oldEntry.teacher){
                editTypes.add(DSBEditEvent.EditType.TEACHER_EDITED)
            }
            if(newEntry.text != oldEntry.text){
                editTypes.add(DSBEditEvent.EditType.TEXT_EDITED)
            }

            return DSBEditEvent(newEntry, oldEntry, editTypes.toTypedArray(), groupData, representationPlan)
        }
        return null
    }

    override fun getNullEntries(newPlan: RepresentationPlan): Array<DSBEvent> {
        return emptyArray()
    }


}