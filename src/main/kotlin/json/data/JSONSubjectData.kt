package json.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dsb.model.RepresentationPlan
import java.io.File
import java.io.FileWriter


/**
 * [JSONSubjectData] ist eine Klasse um die ganzen Subjects aus einer JSON-File zu lesen und zu schreiben
 *
 * */
class JSONSubjectData(file: File) : JsonData<HashMap<String, String>>(file){
}