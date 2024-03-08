package dsb

import dsb.change.ChangesAdded
import dsb.change.ChangesEdited
import dsb.change.ChangesRemoved
import dsb.event.DSBEvent
import dsb.event.listener.DSBEventListener
import dsb.event.manager.EventHandler
import dsb.json.JSONWriter
import dsb.model.RepresentationPlan
import dsb.request.RepresentationPlanDataReader
import dsb.request.ServerHelloRequest
import dsb.settings.Settings
import kotlinx.coroutines.*
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


/**
 * [DSB] wird verwendet um allgemein alle Events und Daten der [RepresentationPlan]s zu verwalten
 *
 * @property username Nutzername muss angegeben werden, da dieser den Login bei https://www.dsbmobile.de/ nötig ist
 * @property password Passwort muss angegeben werden, da dieses den Login bei https://www.dsbmobile.de/ nötig ist
 * @property settings optional: Einstellungen für das Verhalten von Speicherung, etc.
 * */
class DSB internal constructor(
    private val username: String,
    private val password: String,
    private val settings: Settings){

    //Attribute
    private val serverHello = ServerHelloRequest(username, password)
    private val dataReader = RepresentationPlanDataReader(serverHello)
    ///private val timer = CoroutineScope(Dispatchers.Default)
    private val scheduler = Executors.newScheduledThreadPool(1)



    //Event-Handling
    /**
     * [eventHandler] ist für die Speicherung der Event-Listener zuständig.
    */
    val eventHandler = EventHandler()

    /**
     * [on] ist dafür da, um ein auf ein Event zu listenen.
     * Das heißt, dass alles was in der DSB#on<DSBEvent>{...} in den Klammern steht als Methode gesehen wird, um bei einem EventCall ausgeführt werden zu können.
     * Der Listener wird beim Ausführen der Methode [on] zum [eventHandler] hinzugefügt.
     * */
    inline fun <reified T : DSBEvent> on(noinline listener: suspend T.() -> Unit) {
        val eventType = T::class.java.simpleName
        val eventListener: suspend (DSBEvent) -> Unit = { event ->
            if (event is T) {
                listener.invoke(event)
            }
        }
        eventHandler.addKotlinListener(eventType, eventListener)
    }

    /**
     * Füge einen oder mehrere Listener zu, die bei einer Änderung benachrichtigt werden.
     */
    fun addListeners(vararg listeners: DSBEventListener) {
        eventHandler.addJavaListeners(*listeners)
    }

    /**
     * Entfernt einen oder mehrere Listener zu, die bei einer Änderung dann nicht mehr benachrichtigt werden.
     */
    fun removeListeners(vararg listeners: DSBEventListener){
        eventHandler.removeJavaListeners(*listeners)
    }


    //Attribute
    //lateinit var plans: Array<RepresentationPlan>

    /**
     * Fahre die Application hoch, ab jetzt wird jede Änderung durch die Listener benachrichtigt.
     */
    public fun launch() {
        serverHelloReputation()
        dataReputation()
    }

    /**
     * Fahre die Application runter.
     */
    public fun stop(){
        scheduler.shutdown()
    }


    /**
     * Wiederholung für die ServerHelloRequest im Cycle von in [Settings] angeben.
     */
    private fun serverHelloReputation(){
        val task = Runnable {
            serverHello.refresh()
            println("Refreshed ServerHelloRequest")
        }

        val period = settings.serverHelloRequestCycle.toMillis()

        scheduler.scheduleAtFixedRate(task, period, period, TimeUnit.MILLISECONDS)
    }

    /**
     * Wiederholung für der Daten im Cycle von in [Settings] angeben.
     * Außerdem aufrufen der Methode zum Auslesen der Änderungen.
     */
    private fun dataReputation(){
        val task = Runnable {
            //Deklarieren des alten und des neuen Plans und schreiben in JSON-File
            val oldPlans = getCurrentPlans()
            JSONWriter(File("/home/simon/Schreibtisch/JavaTest/DSB/old.json"), oldPlans!!).write()
            dataReader.refresh()
            val newPlans = getCurrentPlans()
            JSONWriter(File("/home/simon/Schreibtisch/JavaTest/DSB/new.json"), newPlans!!).write()

            //Die Änderungen lesen
            callNewEvents(oldPlans, newPlans)
            //println("Refreshed Data")
        }

        val period = settings.dataRefreshCycle.toMillis()

        scheduler.scheduleAtFixedRate(task, period, period, TimeUnit.MILLISECONDS)
    }


    /**
     * Benachrichtigt die Listener auf Änderungen des Events.
     */
    private fun callNewEvents(old: Array<RepresentationPlan>?, new: Array<RepresentationPlan>?){
        if(old == null || new == null) throw NullPointerException("There occured an error while trying to get the changes: RepresentationsPlans equals null! Login data may be incorrect.")
        val removed = ChangesRemoved(new, old).getChangeEvents()
        val added = ChangesAdded(new, old).getChangeEvents()
        val edited = ChangesEdited(new, old).getChangeEvents()

        eventHandler.callEvents(*removed, *added, *edited)
    }



    private fun getCurrentPlans(): Array<RepresentationPlan>? {
        return dataReader.representationPlans
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