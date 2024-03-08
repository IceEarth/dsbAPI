package dsb.json.dataHandling

import com.sun.jna.StringArray
import java.io.File

/**
 * [JSONGroupIDData] ist eine Klasse um die ganzen GroupIDs vom Format StringArray aus einer JSON-File zu lesen und zu schreiben
 *
 * */
internal class JSONGroupIDData internal constructor(file: File) : JsonData<StringArray>(file){

}