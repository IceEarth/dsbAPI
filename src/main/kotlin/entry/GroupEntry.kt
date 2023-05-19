package entry

/**
 * Eine Data-Klasse um die Werte von einem Eintrag zu speichern
 *
 * @param hour: Die Stunde in der die Vertretung stattfindet (Nicht Nullable!)
 * @param teacher: Der Lehrer der die Stunde vertritt (Nicht Nullable!)
 * @param subject: Das Fach in dem die Vertretung stattfindet (Nicht Nullable!)
 * @param room: Der Raum in dem die Vertretung stattfindet
 * @param text: Ein zusätzlicher Text der hinzugefügt werden kann
 * */

data class GroupEntry(val hour: Int, val teacher: String, val subject: String, val room: String?, val text: String?)
