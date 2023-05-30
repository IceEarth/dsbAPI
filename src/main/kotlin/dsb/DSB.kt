package dsb

import dsb.change.ChangesRemoved
import dsb.event.DSBEvent
import dsb.event.listener.DSBEventListener
import dsb.event.manager.EventHandler
import dsb.model.RepresentationPlan
import dsb.request.RepresentationPlanDataReader
import dsb.request.ServerHelloRequest
import dsb.settings.Settings


/**
 * [DSB] wird verwendet um allgemein alle Events und Daten der [RepresentationPlan]s zu verwalten
 *
 * @property username Nutzername muss angegeben werden, da dieser den Login bei https://www.dsbmobile.de/ nötig ist
 * @property password Passwort muss angegeben werden, da dieser den Login bei https://www.dsbmobile.de/ nötig ist
 * @property settings optional: Einstellungen für das Verhalten von Speicherung, etc.
 * */
class DSB internal constructor(
    private val username: String,
    private val password: String,
    private val settings: Settings){

    //Attribute
    private val serverHello = ServerHelloRequest(username, password)
    var representationPlans = RepresentationPlanDataReader(serverHello).representationPlans


    //Event-Handling
    /**
     * [events] ist für die Speicherung der Event-Listener zuständig.
    */
    val events = EventHandler()

    /**
     * [on] ist dafür da, um ein auf ein Event zu listen.
     * Das heißt, dass alles was in der DSB#on<DSBEvent>{...} in den Klammern steht als Methode gesehen wird, um bei einem EventCall ausgeführt werden zu können.
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






    //...
    fun readRepresentationPlans(){
        val removed = ChangesRemoved(emptyArray(), emptyArray()).getChangeEvents()

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