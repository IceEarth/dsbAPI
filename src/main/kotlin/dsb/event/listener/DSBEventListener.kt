package dsb.event.listener

import dsb.event.DSBAddEvent
import dsb.event.DSBEditEvent
import dsb.event.DSBRemoveEvent
import java.util.EventListener

interface DSBEventListener : EventListener{

    fun onDSBAdd(event: DSBAddEvent){}

    fun onDSBEdit(event: DSBEditEvent){}

    fun onDSBRemove(event: DSBRemoveEvent){}

}