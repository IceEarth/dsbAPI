package dsb.model

import dsb.event.DSBAddEvent
import dsb.event.DSBEvent

class ModelUtil {
    companion object{
        fun getAddEventsOfPlan(plan: RepresentationPlan): Array<DSBEvent>{
            val res: ArrayList<DSBEvent> = ArrayList()
            for(groupData in plan.data){
                for(entry in groupData.groupEntries){
                    res.add(DSBAddEvent(entry, groupData, plan))
                }
            }
            return res.toTypedArray()
        }
    }
}