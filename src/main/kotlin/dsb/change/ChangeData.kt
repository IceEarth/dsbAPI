package dsb.change

import dsb.event.DSBEvent
import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.RepresentationPlan
import kotlin.reflect.KProperty1

abstract class ChangeData<T : DSBEvent> internal constructor(private val newPlans: Array<RepresentationPlan>, private val oldPlans: Array<RepresentationPlan>) {
    /**Gibt an, ob die neuesten Pläne wirklich als Neuste betrachtet werden sollen oder eben nicht (ist für [ChangesRemoved_OLD] wichtig, da dort quasi dasselbe wie bei [ChangesAdded] passiert, nur das die alten Einträge als neues betrachtet werden)*/
    abstract val newestFirst: Boolean

    /**Gibt die Änderungsereignisse zurück*/
    open fun getChangeEvents(): Array<DSBEvent> {
        val references = getPlanReferences()
        val result: ArrayList<DSBEvent> = ArrayList()

        for (entry in references.entries) {
            result.addAll(getEventsByPlans(entry.key, entry.value))
        }

        return result.toTypedArray()
    }

    /**Gibt die Änderungsereignisse für einen bestimmten Plan zurück*/
    private fun getEventsByPlans(newPlan: RepresentationPlan, oldPlan: RepresentationPlan?): Array<DSBEvent> {
        val result: ArrayList<DSBEvent> = ArrayList()

        // Wenn oldPlan null ist, werden keine Events zurückgegeben
        if (oldPlan == null) {
            return emptyArray()
        }

        val groupDataReferences =
            if (newestFirst) getGroupDataReferences(newPlan.data, oldPlan.data) else getGroupDataReferences(oldPlan.data, newPlan.data)

        for (newToOldData in groupDataReferences) {
            val newGroupEntries = newToOldData.key.groupEntries
            val oldGroupEntries = newToOldData.value?.groupEntries

            val groupData: GroupData = newToOldData.key

            val entryReferences = getEntryReferences(newGroupEntries, oldGroupEntries)

            for (newToOldEntries in entryReferences) {
                val event = getChangesOfEntry(
                    newEntry = newToOldEntries.key,
                    oldEntry = newToOldEntries.value,
                    groupData = groupData,
                    representationPlan = newPlan
                )

                if (event != null) {
                    result.add(event)
                }
            }
        }

        return result.toTypedArray()
    }

    /**Abstrakte Methode zur Erstellung des Änderungsereignisses für einen Eintrag*/
    protected abstract fun getChangesOfEntry(
        newEntry: GroupEntry,
        oldEntry: GroupEntry?,
        groupData: GroupData,
        representationPlan: RepresentationPlan
    ): DSBEvent?

    /**Hilfsmethode zum Erstellen der Referenzen zwischen Plänen*/
    private fun getPlanReferences(): Map<RepresentationPlan, RepresentationPlan?> {
        return getReferences(newPlans, oldPlans, RepresentationPlan::date)
    }

    /**Hilfsmethode zum Erstellen der Referenzen zwischen Gruppendaten*/
    private fun getGroupDataReferences(newDataArray: Array<GroupData>, oldDataArray: Array<GroupData>?): Map<GroupData, GroupData?> {
        return getReferences(newDataArray, oldDataArray, GroupData::groupID)
    }

    /**Hilfsmethode zum Erstellen der Referenzen zwischen Einträgen*/
    private fun getEntryReferences(newEntries: Array<GroupEntry>, oldEntries: Array<GroupEntry>?): Map<GroupEntry, GroupEntry?>{
        return getReferences(newEntries, oldEntries){
            newEntry, oldEntry -> (newEntry.hour == oldEntry.hour && newEntry.subject == oldEntry.subject)
        }
    }

    private fun <E> getReferences(new: Array<E>, old: Array<E>?, predicate: (E, E) -> Boolean ): Map<E, E?>{
        val result = mutableMapOf<E, E?>()

        for(newEntry in new){
            val oldEntry = old?.firstOrNull{predicate(newEntry, it)}
            result[newEntry] = oldEntry
        }
        return result
    }

    private fun <E> getReferences(new: Array<E>, old: Array<E>?, property: KProperty1<E, Any>): Map<E, E?>{
        val result = mutableMapOf<E, E?>()

        for(newEntry in new){
            val oldEntry = old?.firstOrNull{property.get(it) == property.get(newEntry)}
            result[newEntry] = oldEntry
        }
        return result
    }



}