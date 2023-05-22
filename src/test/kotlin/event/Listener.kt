package event

import dsb.event.DSBAddEvent
import dsb.event.DSBEditEvent
import dsb.event.listener.DSBEventListener

class Listener : DSBEventListener {
    override fun onDSBAdd(event: DSBAddEvent) {

    }

    override fun onDSBEdit(event: DSBEditEvent) {
        println(event.getMessage())
    }
}