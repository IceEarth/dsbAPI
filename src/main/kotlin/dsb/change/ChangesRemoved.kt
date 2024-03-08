package dsb.change

import dsb.event.DSBEvent
import dsb.model.RepresentationPlan

class ChangesRemoved internal constructor(new: Array<RepresentationPlan>, old: Array<RepresentationPlan>) : ChangesAdded(old, new)
/**[ChangesRemoved] ist genau dasselbe wie [ChangesAdded] nur umgekehrt (first and seconds element werden ausgetauscht)*/
{
    override fun getNullEntries(newPlan: RepresentationPlan): Array<DSBEvent> {
        return emptyArray()
    }

}