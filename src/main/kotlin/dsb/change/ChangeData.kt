package dsb.change

import dsb.event.DSBEvent
import dsb.event.DSBRemoveEvent
import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.RepresentationPlan
import kotlin.reflect.KProperty1

abstract class ChangeData<T: DSBEvent>(private val new: Array<RepresentationPlan>, private val old: Array<RepresentationPlan>){
    abstract val newestFirst: Boolean

    open fun getChangeEvents(): Array<DSBEvent>{
        val references = getPlanReferences()
        val result: ArrayList<DSBEvent> = ArrayList()

        for(entry in references.entries){
            result.addAll(getEventsByPlans(entry.key, entry.value))
        }

        return result.toTypedArray()
    }

    protected open fun getEventsByPlans(newPlan: RepresentationPlan, oldPlan: RepresentationPlan?): Array<DSBEvent>{
        /**Vergleich des [RepresentationPlan.date] ist nicht mehr nötig, da schon in [getPlanReferences] die Referenzen erstellt wurden!*/

        /**Erstellen von der Result-Liste*/
        val result: ArrayList<DSBEvent> = ArrayList()

        /**Wenn oldPlan null ist, sollen keine Events returned werden!*/
        if(oldPlan == null){
            return emptyArray()
        }

        /**Falls der generische Typ T ein DSBRemoveEvent ist, soll das ganze umgedreht werden */


        val groupDataReferences = if(newestFirst) getGroupDataReferences(newPlan.data, oldPlan.data) else getGroupDataReferences(oldPlan.data, newPlan.data)

        for(newToOldData in groupDataReferences){
            val newGroupEntries = newToOldData.key.groupEntries
            val oldGroupEntries = newToOldData.value?.groupEntries ?: continue

            val groupData: GroupData = newToOldData.key

            val entryReferences = getEntryReferences(newGroupEntries, oldGroupEntries)

            for(newToOldEntries in entryReferences){
                val event = getChangesOfEntry(
                    newEntry = newToOldEntries.key,
                    oldEntry = newToOldEntries.value,
                    groupData = groupData,
                    representationPlan = newPlan)

                if (event != null) {
                    result.add(event)
                }
            }
        }

        return result.toTypedArray()
    }
    protected abstract fun getChangesOfEntry(newEntry: GroupEntry, oldEntry: GroupEntry?, groupData: GroupData, representationPlan: RepresentationPlan): DSBEvent?

    private fun getPlanReferences(): Map<RepresentationPlan, RepresentationPlan?>{
        return getReferences(new, old, RepresentationPlan::date)
    }

    private fun getGroupDataReferences(newDataArray: Array<GroupData>, oldDataArray: Array<GroupData>): Map<GroupData, GroupData?>{
        return getReferences(newDataArray, oldDataArray, GroupData::groupID)

    }

    private fun getEntryReferences(newEntries: Array<GroupEntry>, oldEntries: Array<GroupEntry>): Map<GroupEntry, GroupEntry?>{
        return getReferences(newEntries, oldEntries){
            newEntry, oldEntry -> (newEntry.hour == oldEntry.hour && newEntry.subject == oldEntry.subject)
        }
    }

    private fun <E> getReferences(new: Array<E>, old: Array<E>, predicate: (E, E) -> Boolean ): Map<E, E?>{
        val result = mutableMapOf<E, E?>()

        for(newEntry in new){
            val oldEntry = old.firstOrNull{predicate(newEntry, it)}
            result[newEntry] = oldEntry
        }
        return result
    }

    private fun <E> getReferences(new: Array<E>, old: Array<E>, property: KProperty1<E, Any>): Map<E, E?>{
        val result = mutableMapOf<E, E?>()

        for(newEntry in new){
            val oldEntry = old.firstOrNull{property.get(it) == property.get(newEntry)}
            result[newEntry] = oldEntry
        }
        return result
    }



}