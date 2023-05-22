import json.data.JSONRepresentationPlanData
import java.io.File

class Test2 {

}

fun main() {

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
}