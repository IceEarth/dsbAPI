package dsb.event

import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.RepresentationPlan
import java.util.Date

data class DSBAddEvent(val added: GroupEntry, val groupData: GroupData, val representationPlan: RepresentationPlan) : DSBEvent
