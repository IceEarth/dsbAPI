package dsb.event.manager

import dsb.event.DSBEvent
import dsb.event.listener.DSBEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventManager {

    private val kotlinListeners: MutableMap<String, MutableList<suspend (DSBEvent) -> Unit>> = mutableMapOf()
    private val javaListeners = ArrayList<DSBEventListener>()
    fun notifyEvents(event: DSBEvent){
        notifyJavaListeners(event)
        notifyKotlinListeners(event)
    }


    private fun notifyJavaListeners(event: DSBEvent){
        val eventType = event.javaClass.simpleName.removeSuffix("Event")
        javaListeners.forEach{
            val methodName = "on${eventType}"
            val method = it.javaClass.getMethod(methodName, event.javaClass)
            method.invoke(it, event)
        }
    }

    private fun notifyKotlinListeners(event: DSBEvent){
        val eventType = event.javaClass.simpleName
        val listeners = kotlinListeners[eventType]
        listeners?.forEach {
            CoroutineScope(Dispatchers.Default).launch { it(event) }
        }
    }
    fun addJavaListeners(vararg listeners: DSBEventListener){
        listeners.forEach { javaListeners.add(it) }
    }

    fun removeJavaListeners(vararg listeners: DSBEventListener){
        listeners.forEach { javaListeners.remove(it) }
    }

    fun addKotlinListener(eventType: String, listener: suspend (DSBEvent) -> Unit) {
        val eventListener: suspend (Any) -> Unit = { event ->
            val castedEvent = event as? DSBEvent
            castedEvent?.let { listener.invoke(it) }
        }
        this.kotlinListeners.getOrPut(eventType) { mutableListOf() }.add(eventListener)
    }



}