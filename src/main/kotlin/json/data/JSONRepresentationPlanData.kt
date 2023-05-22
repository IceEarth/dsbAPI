package json.data

import com.google.gson.Gson
import dsb.model.RepresentationPlan
import java.io.File
import java.io.FileWriter

/**
 * [JSONRepresentationPlanData] ist eine Klasse um die ganzen [RepresentationPlan]s aus einer JSON-File zu lesen und zu schreiben
 * */
class JSONRepresentationPlanData(file: File) : JsonData<Array<RepresentationPlan>>(file){

}