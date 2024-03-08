import dsb.DSB
import dsb.event.DSBAddEvent
import dsb.event.DSBEditEvent
import dsb.event.DSBRemoveEvent
import dsb.settings.Settings
import java.time.Duration

fun main(){


    //ServerHelloRequest("148922","vPl4nS4g")

    val dsb = DSB.Builder("148922","vPl4nS4g").settings(
        Settings.Builder()
            .serverHelloRequestCycle(Duration.ofMinutes(5))
            .dataRefreshCycle(Duration.ofSeconds(30))
            .build()
    ).build()

    dsb.launch()

    println(System.getProperty("user.dir"))



    dsb.on<DSBEditEvent> {
        println("EDIT: " +
                "${groupData.groupID} $newEntry")
    }
    dsb.on<DSBAddEvent>{
        println("ADD: ${groupData.groupID} $added")
    }
    dsb.on<DSBRemoveEvent>{
        println("REMOVE: ${groupData.groupID} $removed")
    }
}