package dsb.event

import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.RepresentationPlan
import java.util.Date

data class DSBAddEvent(private val added: GroupEntry, private val groupData: GroupData, private val representationPlan: RepresentationPlan) : DSBEvent
