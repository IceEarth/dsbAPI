package dsb.json.dataHandling

import dsb.json.JsonData
import java.io.File


/**
 * [JSONSubjectData] ist eine Klasse um die ganzen Subjects aus einer JSON-File zu lesen und zu schreiben
 *
 * */
internal class JSONSubjectData internal constructor(file: File) : JsonData<HashMap<String, String>>(file){
}