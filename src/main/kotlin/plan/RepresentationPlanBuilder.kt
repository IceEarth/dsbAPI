package plan

import data.GroupData
import java.util.Date

class RepresentationPlanBuilder {
    private var date: Date? = null
    private var groupDataEntries: ArrayList<GroupData> = ArrayList()


    /**
     * Setzt das Datum vom plan.RepresentationPlan... (Methode ist nötig, sonst wird eine Nullpoint-Exception gecalled)
     * */
    fun setDate(date: Date): RepresentationPlanBuilder {
        this.date = date
        return this
    }

    fun addGroupData(groupData: GroupData): RepresentationPlanBuilder {
        this.groupDataEntries.add(groupData)
        return this
    }

    fun build(): RepresentationPlan {
        requireNotNull(date){"date must not be null"}
        return RepresentationPlan(date!!, groupDataEntries.toTypedArray())
    }
}