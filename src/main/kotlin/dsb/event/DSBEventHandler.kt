package dsb.event

import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DSBEventHandler {
    val eventListeners: ConcurrentHashMap<String, suspend (DSBEvent) -> Unit> = ConcurrentHashMap()

    inline fun <reified T : DSBEvent> on(noinline listener: suspend T.() -> Unit) {
        val eventType = T::class.java.simpleName
        eventListeners[eventType] = { event ->
            val castedEvent = event as? T
            castedEvent?.let { listener.invoke(it) }
        }
    }

    fun emit(event: DSBEvent) {
        val eventType = event.javaClass.simpleName
        val listener = eventListeners[eventType]
        listener?.let { CoroutineScope(Dispatchers.Default).launch { it(event) } }
    }
}
