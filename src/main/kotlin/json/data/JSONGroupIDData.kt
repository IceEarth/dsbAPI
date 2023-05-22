package json.data

import com.sun.jna.StringArray
import java.io.File

/**
 * [JSONGroupIDData] ist eine Klasse um die ganzen GroupIDs vom Format StringArray aus einer JSON-File zu lesen und zu schreiben
 *
 * */
class JSONGroupIDData(file: File) : JsonData<StringArray>(file){

}