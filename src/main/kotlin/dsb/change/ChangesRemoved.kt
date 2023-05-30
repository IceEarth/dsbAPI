package dsb.change

import dsb.model.RepresentationPlan

class ChangesRemoved internal constructor(new: Array<RepresentationPlan>, old: Array<RepresentationPlan>) : ChangesAdded(old, new) {
}