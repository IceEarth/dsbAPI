package dsb.request

import dsb.model.GroupData
import dsb.model.GroupEntry
import dsb.model.RepresentationPlan
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL


class RepresentationPlanDataReader(private val serverHello: ServerHello) {
    lateinit var representationPlans: Array<RepresentationPlan>
        private set

    init {
        refreshData()
    }

    fun refreshData(){
        this.representationPlans = serverHello.urls!!.map { getRepresentationPlan(it) }.toTypedArray()

    }

    private fun getRepresentationPlan(url: URL): RepresentationPlan{
        val document = getHTMLBody(url)
        return getTable(document)
    }

    /**
     * @return Gibt ein HTML-Body von einer URL zurück
     * @param url Die Url von dem HTML-Body
     * @throws NullPointerException Falls die Request schiefgeht...
     *
     * */
    private fun getHTMLBody(url: URL): Document{

        /**Erstellen von Client und Request*/
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        /**Die Response bekommen...*/
        val response = client.newCall(request).execute()

        /**Parsen des document(s) bei Schiefgehen: wird eine [NullPointerException] gethrowed*/
        val document: Document = Jsoup.parse(response.body!!.string()) ?: throw NullPointerException("There occurred an error when getting the RepresentationPlans... HTML-Request is empty!")

        println(document)
        return document
    }

    /**
     * @param document Der HTML-Body ist nötig um ausgelesen zu werden
     * @return Gibt einen RepresentationPlan zurück*/
    private fun getTable(document: Document): RepresentationPlan{

        /**
         * Tabelle das durch das unique Element <tr> bekommen wird
         * */
        val table = document.select("table")[2].select("tr")

        /**Erstes Element ist (Stunde | Vertreter | Fach | Raum | Text) und hat somit keinen Datenwert*/
        table.removeFirst()

        /**Initialisieren des Datums vom Format: "19.5.2023 Freitag"*/
        val date = document.getElementsByClass("mon_title").first()?.html()?.let { DataUtil.getDate(it) }

        /**Initialisieren der Builder*/
        val planBuilder = RepresentationPlan.Builder()
        var dataBuilder = GroupData.Builder()

        /**Setzen des Datums im planBuilder()*/
        planBuilder.date(date!!)

        /**Iteration durch die Tabelle table*/
        for((i, column) in table.withIndex()){
            println("el" + column.getElementById("td")?.allElements?.get(0))
            println("i: $i")
            println("column_size: " + column.childrenSize())

            TODO("fixme!!! Speicherung funktioniert noch nicht...")


            /**Wenn in column nur 1 Element ist, ist es die Klasse → die Groupdata dem [RepresentationPlan.Builder] hinzufügen + neuen [GroupData.Builder] anlegen */
            if(column.childrenSize() == 1){
                val groupID = column.allElements[0].html()
                /**Bei der ersten Iteration soll noch nichts gespeichert werden*/
                if(i != 0){
                    planBuilder.addData(dataBuilder.build())
                    dataBuilder = GroupData.Builder()
                }
                dataBuilder.groupID(groupID)

            /**Wenn in column 5 Elemente sind, ist es ein Datensatz → einen Entry zum dataBuilder hinzufügen*/
            }else if(column.childrenSize() == 5){

                /**Wenn ein Eintrag von hour = "1 - 2" (z.B.), sollen trotzdem 2 Einträge (i. d. F.) erstellt werden... Deswegen auch die Iteration*/
                println(column.allElements[0].html())
                val hours = DataUtil.getRange(column.allElements[0].html().toString())

                /**Setze alle restliche Werte für die Übersicht*/
                val teacher = column.allElements[1].html()
                val subject = column.allElements[2].html()
                val room = column.allElements[3].html()
                val text = column.allElements[4].html()

                /**Iteration wie vorher erwähnt um bei für jede hour einen einzelnen Eintrag zu setzten*/
                for(hour in hours){
                    /**Hinzufügen eines GroupEntry's zum dataBuilder*/
                    dataBuilder.addGroupEntry(
                        GroupEntry.Builder()
                            .hour(hour)
                            .teacher(teacher)
                            .subject(subject)
                            .room(room)
                            .text(text)
                            .build()
                    )
                }
            }
        }

        return planBuilder.build()



    }
}