package dsb

import dsb.event.DSBEvent
import dsb.event.listener.DSBEventListener
import dsb.event.manager.EventManager
import dsb.model.RepresentationPlan
import dsb.settings.Settings


/**
 * [DSB] wird verwendet um allgemein alle Events und Daten der [RepresentationPlan]s zu verwalten
 *
 * @property username Nutzername muss angegeben werden, da dieser den Login bei https://www.dsbmobile.de/ nötig ist
 * @property password Passwort muss angegeben werden, da dieser den Login bei https://www.dsbmobile.de/ nötig ist
 * @property settings optional: Einstellungen für das Verhalten von Speicherung, etc.
 * */
class DSB (
    private val username: String,
    private val password: String,
    private val settings: Settings){

    //Event-Handling
    /**
     * [eventListeners] ist für die Speicherung der Event-Listeners zuständig und wird somit für die Methoden [on] und [emit] gebraucht.
    */
    val events = EventManager()

    /**
     * [on] ist dafür da, um ein auf ein Event zu listen.
     * Das heißt, dass alles was in der DSB#on<DSBEvent>{...} in den Klammern steht als Methode gesehen wird, um wieder in [emit] ausgeführt werden zu können.
     * Der Listener wird beim Ausführen der Methode [on] zum [events] hinzugefügt.
     * */
    inline fun <reified T : DSBEvent> on(noinline listener: suspend T.() -> Unit) {
        val eventType = T::class.java.simpleName
        val eventListener: suspend (DSBEvent) -> Unit = { event ->
            if (event is T) {
                listener.invoke(event)
            }
        }
        events.addKotlinListener(eventType, eventListener)
    }

    fun addListeners(vararg listeners: DSBEventListener) {
        events.addJavaListeners(*listeners)
    }

    fun removeListeners(vararg listeners: DSBEventListener){
        events.removeJavaListeners(*listeners)
    }

    @Deprecated("will be removed... just for testing", ReplaceWith("events.notifyEvents(event)"))
    fun emit(event: DSBEvent){
        events.notifyEvents(event)
    }


    //...
    fun readRepresentationPlans(){
        TODO()
    }




    //TODO add Methods and Attributes



    /**
     * [Builder] ist eine Building Klasse um eine Instanz von [DSB] einfach und übersichtlich zu erstellen
     * */
    data class Builder(var username: String,
                       var password: String,
                       var settings: Settings = Settings.defaults)
    {


        fun settings(settings: Settings) = apply { this.settings = settings }
        fun build() = DSB(username, password, settings)

    }
}