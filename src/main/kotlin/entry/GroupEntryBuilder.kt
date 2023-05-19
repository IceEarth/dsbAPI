package entry

class GroupEntryBuilder {
    /**
     * Ein Builder um einen GroupEntry zu erstellen
     *
     * */

    private  var hour: Int? = null
    private var teacher: String? = null
    private var subject: String? = null
    private var room: String? = null
    private var text: String? = null


    fun setHour(hour: Int): GroupEntryBuilder{
        this.hour = hour
        return this
    }

    fun setTeacher(teacher: String?): GroupEntryBuilder{
        this.teacher = teacher
        return this
    }

    fun setSubject(subject: String?): GroupEntryBuilder{
        this.subject = subject
        return this
    }

    fun setRoom(room: String?): GroupEntryBuilder{
        this.room = room
        return this
    }

    fun setText(text: String?): GroupEntryBuilder{
        this.text = text
        return this
    }

    /**
     * @return Es wird ein GroupEntry Objekt erstellt und zurückgegeben
     * */
    fun build(): GroupEntry{
        requireNotNull(hour){"hour must not be null"}
        requireNotNull(teacher){"teacher must not be null"}
        requireNotNull(subject){"subject must not be null"}
        return GroupEntry(hour!!, teacher!!, subject!!, room, text)
    }


}