package dsb

import dsb.event.DSBEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dsb.model.RepresentationPlan
import java.io.File
import java.time.Duration


/**
 * [DSB] wird verwendet um allgemein alle Events und Daten der [RepresentationPlan]s zu verwalten
 *
 * @param username Nutzername muss angegeben werden, da dieser den Login bei https://www.dsbmobile.de/ nötig ist
 * @param password Passwort muss angegeben werden, da dieser den Login bei https://www.dsbmobile.de/ nötig ist
 * @param reputationsCycle Duration für den Zeit-Zyklus, indem die Daten der Website https://www.dsbmobile.de/ refreshed werden sollen
 * @param temporary Boolean, ob die Daten in einer JSON-File gespeichert werden sollen, standardmäßig: false
 * @param file Directory, um verschiedene Daten zu speichern. (Wenn [temporary] true ist hat [file] keinen Impact), standardmäßig: working-directory
 * */
class DSB (
    private val username: String,
    private val password: String,
    private val reputationsCycle: Duration?,
    private val temporary: Boolean,
    private val file: File){

    //Event-Handling
    /**
     * [eventListeners] ist für die Speicherung der Event-Listeners zuständig und wird somit für die Methoden [on] und [emit] gebraucht.
    */
    val eventListeners: MutableMap<String, MutableList<suspend (DSBEvent) -> Unit>> = mutableMapOf()

    /**
     * [on] ist dafür da, um ein auf ein Event zu listen.
     * Das heißt, dass alles was in der DSB#on<DSBEvent>{...} in den Klammern steht als Methode gesehen wird, um wieder in [emit] ausgeführt werden zu können.
     * Der Listener wird beim Ausführen der Methode [on] zu [eventListeners] hinzugefügt.
     * */
    inline fun <reified T : DSBEvent> on(noinline listener: suspend (T) -> Unit) {
        val eventType = T::class.java.simpleName
        val eventListener: suspend (DSBEvent) -> Unit = { event ->
            val castedEvent = event as? T
            castedEvent?.let { listener.invoke(it) }
        }
        eventListeners.getOrPut(eventType) { mutableListOf() }.add(eventListener)
    }

    /**
     * [emit] ist dafür da, um alle Listener (aus [eventListeners]) zu benachrichtigen.
     * */
    fun emit(event: DSBEvent) {
        val eventType = event.javaClass.simpleName
        val listeners = eventListeners[eventType]
        listeners?.forEach { listener ->
            CoroutineScope(Dispatchers.Default).launch {
                listener(event)
            }
        }
    }





    //...
    fun readRepresentationPlans(){
        TODO()
    }




    //TODO add Methods and Attributes



    /**
     * [Builder] ist eine Building Klasse um eine Instanz von [DSB] einfach und übersichtlich zu erstellen
     * */
    data class Builder(var username: String? = null,
                       var password: String? = null,
                       var reputationsCycle: Duration = Duration.ofMinutes(5),
                       var temporary: Boolean = false,
                       var file: File = File(System.getProperty("user.dir"))
    ) {


        fun username(username: String) = apply{this.username = username}
        fun password(password: String) = apply{this.password = password}
        fun reputationsCycle(reputationsCycle: Duration) = apply{this.reputationsCycle = reputationsCycle}

        fun temporary(temporary: Boolean) = apply { this.temporary = temporary }

        fun file(file: File) = apply { this.file = file }

        fun build(): DSB {
            requireNotNull(username){"username must not be null"}
            requireNotNull(password){"password must not be null"}
            return DSB(username!!, password!!, reputationsCycle, temporary, file)
        }

    }
}