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
                " ${representationPlan.date} ${groupData.groupID} \n new: $newEntry \n what: $editTypes \n old: $oldEntry")
    }
    dsb.on<DSBAddEvent>{
        println("ADD: ${representationPlan.date} ${groupData.groupID} $added")
    }
    dsb.on<DSBRemoveEvent>{
        println("REMOVE: ${representationPlan.date} ${groupData.groupID} $removed")
    }
}