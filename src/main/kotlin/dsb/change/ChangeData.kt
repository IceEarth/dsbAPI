package dsb.change

import dsb.event.DSBEvent
import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.RepresentationPlan
import kotlin.reflect.KProperty1

abstract class ChangeData<T : DSBEvent> internal constructor(private val firstPlans: Array<RepresentationPlan>, private val secondPlans: Array<RepresentationPlan>) {

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
    private fun getEventsByPlans(firstPlan: RepresentationPlan, secondPlan: RepresentationPlan?): Array<DSBEvent> {
        val result: ArrayList<DSBEvent> = ArrayList()

        // Wenn secondPlan null ist, werden keine Events zurückgegeben
        if (secondPlan == null) {
            return getNullEntries(firstPlan)
        }


        val groupDataReferences = getGroupDataReferences(firstPlan.data, secondPlan.data)

        for (firstToOldData in groupDataReferences) {
            val firstGroupEntries = firstToOldData.key.groupEntries
            val secondGroupEntries = firstToOldData.value?.groupEntries

            val groupData: GroupData = firstToOldData.key

            val entryReferences = getEntryReferences(firstGroupEntries, secondGroupEntries)

            for (firstToOldEntries in entryReferences) {
                val event = getChangesOfEntry(
                    firstEntry = firstToOldEntries.key,
                    secondEntry = firstToOldEntries.value,
                    groupData = groupData,
                    representationPlan = firstPlan
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
        firstEntry: GroupEntry,
        secondEntry: GroupEntry?,
        groupData: GroupData,
        representationPlan: RepresentationPlan
    ): DSBEvent?

    /**
     * Abstrakte Methode getNullEntries ist dafür wichtig, wenn in der Überprüfung herrausgefunden wird, dass die Referenz null ist, soll sie in einem bestimmten Fall doch
     * einen Wert zurückgeben (siehe [ChangesAdded]).
     */
    protected abstract fun getNullEntries(firstPlan: RepresentationPlan): Array<DSBEvent>

    /**Hilfsmethode zum Erstellen der Referenzen zwischen Plänen*/
    private fun getPlanReferences(): Map<RepresentationPlan, RepresentationPlan?> {
        return getReferences(firstPlans, secondPlans, RepresentationPlan::date)
    }

    /**Hilfsmethode zum Erstellen der Referenzen zwischen Gruppendaten*/
    private fun getGroupDataReferences(firstDataArray: Array<GroupData>, secondDataArray: Array<GroupData>?): Map<GroupData, GroupData?> {
        return getReferences(firstDataArray, secondDataArray, GroupData::groupID)
    }

    /**Hilfsmethode zum Erstellen der Referenzen zwischen Einträgen*/
    private fun getEntryReferences(firstEntries: Array<GroupEntry>, secondEntries: Array<GroupEntry>?): Map<GroupEntry, GroupEntry?>{
        //Überprüfung, ob ein Fach und eine Stunde (z.B. bei Gruppenteilung) mehreren Lehrern zugeordnet sind.
        /*Es wird deshalb das nur bei dieser Überprüfung durchgeführt, da die aufgerufene Methode fehleranfälliger ist. (Es werden Abstriche bei dem DSBEditEvent gemacht,
        der Lehrer wird nicht mehr angezeigt, wenn er geändert wird.)*/
       if(hasMultipleTeachersAtSameTimeAndSubject(firstEntries)){
           //Nun wird ein Teil des "teachers" als weiterer Primärschlüssel verwendet und zurückgegeben.
           return getDuplicationFreeEntryReferences(firstEntries, secondEntries)
       }

        //Die Referenz wird zurückgegeben
        return getReferences(firstEntries, secondEntries){
            firstEntry, secondEntry -> (firstEntry.hour == secondEntry.hour && firstEntry.subject == secondEntry.subject /*&& firstEntry.teacher.split("→").first() == secondEntry.teacher.split("→").first()*/)
        }
    }

    /**
     * Hilfsmethode zum Erhalten der Referenzen zwischen zwei Feldern, mithilfe eines Predicates zum Vergleichen (mehrere Werte und mehrere Bedingungen).
     */
    private fun <E> getReferences(first: Array<E>, second: Array<E>?, predicate: (E, E) -> Boolean ): Map<E, E?>{
        //Erstellen einer Result-Map
        val result = mutableMapOf<E, E?>()

        //Iteration durch jeden neuen Eintrag
        for(firstEntry in first){
            //Initialisierung von dem alten Eintrag, es wird nach dem Eintrag gefiltert, der genau gleich ist.
            val secondEntry = second?.firstOrNull{predicate(firstEntry, it)}
            result[firstEntry] = secondEntry
        }
        return result
    }

    /**
     * Hilfsmethode zum Erhalten der Referenzen zwischen zwei Feldern, mithilfe einer Property zum Vergleichen (ein Wert und eine Bedingung).
     */
    private fun <E> getReferences(first: Array<E>, second: Array<E>?, property: KProperty1<E, Any>): Map<E, E?>{
        val result = mutableMapOf<E, E?>()

        for(firstEntry in first){
            val secondEntry = second?.firstOrNull{property.get(it) == property.get(firstEntry)}
            result[firstEntry] = secondEntry
        }
        return result
    }


    /**
     * Hilfsmethode um die Referenzen mit einem zusätzlichen Primärschlüssel (Teil des Lehrers) zu bekommen.
     */
    private fun getDuplicationFreeEntryReferences(firstEntries: Array<GroupEntry>, secondEntries: Array<GroupEntry>?): Map<GroupEntry, GroupEntry?>{
        return getReferences(firstEntries, secondEntries){
                firstEntry, secondEntry -> (firstEntry.hour == secondEntry.hour && firstEntry.subject == secondEntry.subject && firstEntry.teacher.split("→").first() == secondEntry.teacher.split("→").first())
        }

    }


    /**
     * Hilfsmethode zum Überprüfen, ob eine GroupEntries-Liste Doppelungen bzgl. des Fachs und der Stunde aufzuweisen hat.
     */
    private fun hasMultipleTeachersAtSameTimeAndSubject(groupEntries: Array<GroupEntry>): Boolean {
        //Erstellen eines groupierten Referenzobjekts: 1 Stunde → 1 Fach (Falls 2 Mal selbes Fach vorhanden, 2 Fach)
        val entriesByHourAndSubject = groupEntries.groupBy { it.hour to it.subject}

        //Iteration durch das erstellte Objekt
        for (entries in entriesByHourAndSubject.values) {

            //Wenn die Größe des Fachs 1 übersteigt, ist eine Dopplung vorhanden.
            if (entries.size > 1) {
                //Debugging...
                //println(entriesByHourAndSubject)
                //println("Mehrere Lehrereinträge zur gleichen Zeit und im selben Fach")

                //Es gibt mehrere Einträge zur gleichen Stunde und im selben Fach
                return true
            }
        }

        // Es gibt keine mehrfachen Einträge zur gleichen Zeit und im selben Fach
        return false
    }


}