import dsb.change.ChangesAdded
import dsb.change.ChangesEdited
import dsb.change.ChangesRemoved
import dsb.event.DSBAddEvent
import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.RepresentationPlan
import java.util.*

fun main() {
    val classdata1 : GroupData = GroupData("9k", arrayOf(
        GroupEntry(3, "LEUB->HOPF", "m", "E.05", "   "),
        GroupEntry(4, "LEUB->HOPF", "m", "E.05", "   "),
    ) )
    val classdata2 : GroupData = GroupData("10k", arrayOf(
        GroupEntry(4, "LEUB->HOPF", "m", "E.05", "   "),
        GroupEntry(5, "LEUB->HOPF", "m", "E.05", "   "),
        GroupEntry(6, "nix", "d", "x01", "")

    ) )
    val new : RepresentationPlan = RepresentationPlan(Date(), arrayOf(classdata1, classdata2))

    val classdata3 : GroupData = GroupData("9k", arrayOf(
        GroupEntry(3, "LEUB->HOPF", "m", "E.05", "   "),
        GroupEntry(4, "LEUB->HOPF", "m", "E.05", "   ")
    ) )
    val classdata4 : GroupData = GroupData("10k", arrayOf(
        GroupEntry(4, "LEUB->HOPF", "m", "E.05", "   "),
        GroupEntry(5, "LEUB->HOPF", "m", "E.05", "   ")

    ) )

    val old : RepresentationPlan = RepresentationPlan(Date(12, 4, 4), /*arrayOf(classdata3, classdata4)*/emptyArray())

    //Bei removed: Wenn input: 100; komplett andere Liste: müsste output 100 sein...
    val events = ChangesEdited(arrayOf(new), arrayOf(old)).getChangeEvents()
    println("Events: " + events.size)
    events.forEach {
        it as DSBAddEvent
        println(it.groupData.groupID + "\t" + it.added.toTrimmedString())
    }
}

private fun getPlanEntries(plan: RepresentationPlan): Int{
    var res = 0
    for(data in plan.data){
        res += data.groupEntries.size
    }
    return res
}
/*
val loader = ClassLoader.getSystemClassLoader()
    val file = File(loader.getResource("format.json")?.path)
    println(file.readText())
    val plans = JSONRepresentationPlanData(file).getData()
    if (plans != null) {
        for(plan in plans){
            println("${plan.date} \n")
            for(data in plan.groupEntries){
                for(entry in data.groupEntries){
                    println("$entry\n")
                }
            }
        }
    }
    JSONRepresentationPlanData(file).saveData(plans)
 */
/*
 val request = ServerHelloRequest("148922", "vPl4nS4g")
    val plans = RepresentationPlanDataReader(request).representationPlans

    request.urls?.forEach { println(it) }

    //
    val dateString = "17.05.2023"
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    val date = dateFormat.parse(dateString)
    plans?.first()?.date = date


    plans!!
    println("Plan[1] ${getPlanEntries(plans[1])}")
    println("Plan[0] ${getPlanEntries(plans[0])}")



    JSONRepresentationPlanData(File("/home/simon/IdeaProjects/Discord_Bots/dsbAPI/src/main/resources/dsbapi.json")).saveData(plans)

 */