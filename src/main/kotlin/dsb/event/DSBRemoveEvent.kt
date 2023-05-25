package dsb.event

import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.RepresentationPlan

data class DSBRemoveEvent(private val removed: GroupEntry, private val groupData: GroupData, private val representationPlan: RepresentationPlan) :
    DSBEvent