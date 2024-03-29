package dsb.event

import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.RepresentationPlan

data class DSBRemoveEvent(val removed: GroupEntry, val groupData: GroupData, val representationPlan: RepresentationPlan) :
    DSBEvent