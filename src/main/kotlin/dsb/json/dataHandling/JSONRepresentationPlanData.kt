package dsb.json.dataHandling

import dsb.model.RepresentationPlan
import java.io.File

/**
 * [JSONRepresentationPlanData] ist eine Klasse um die ganzen [RepresentationPlan]s aus einer JSON-File zu lesen und zu schreiben
 * */
internal class JSONRepresentationPlanData (file: File) : JsonData<Array<RepresentationPlan>>(file){

}