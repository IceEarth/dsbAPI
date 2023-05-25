package dsb.settings

import java.awt.MultipleGradientPaint.CycleMethod
import java.io.File
import java.time.Duration

/**
 * Datenklasse, die die Einstellungen für die Anwendung repräsentiert.
 *
 * @property workingDirectory Das Arbeitsverzeichnis für die Anwendung.
 * @property temporary Flag, das angibt, ob die Anwendung im temporären Modus läuft.
 * @property dataRefreshCycle Der Zyklus, indem die Daten geholt werden sollen.
 * @property serverHelloRequestCycle Der Zyklus, indem die ServerHelloRequest wiederholt wird.
 * @property saveGroupIDs Flag, das angibt, ob Gruppen-IDs gespeichert werden sollen.
 * @property saveSubjects Flag, das angibt, ob Fächer gespeichert werden sollen.
 */
data class Settings internal constructor(
    val workingDirectory: File,
    val temporary: Boolean,
    val dataRefreshCycle: Duration,
    val serverHelloRequestCycle: Duration,
    val saveGroupIDs: Boolean,
    val saveSubjects: Boolean
) {

    companion object {
        /**
         * Standard-Einstellungen für die Anwendung.
         */
        val defaults = Settings(
            dataRefreshCycle = Duration.ofMinutes(5),
            serverHelloRequestCycle = Duration.ofMinutes(30),
            workingDirectory = File(System.getProperty("user.dir")),
            temporary = false,
            saveGroupIDs = false,
            saveSubjects = false
        )
    }

    /**
     * Builder-Klasse zum Erstellen von Settings-Objekten mit individuellen Werten.
     *
     * @property workingDirectory Das Arbeitsverzeichnis für die Anwendung.
     * @property temporary Flag, das angibt, ob die Anwendung im temporären Modus läuft.
     * @property dataRefreshCycle Der Zyklus, indem die Daten geholt werden sollen.
     * @property serverHelloRequestCycle Der Zyklus, indem die ServerHelloRequest wiederholt wird.
     * @property saveGroupIDs Flag, das angibt, ob Gruppen-IDs gespeichert werden sollen.
     * @property saveSubjects Flag, das angibt, ob Fächer gespeichert werden sollen.
     */
    public data class Builder( var workingDirectory: File,
                        var temporary: Boolean,
                        var dataRefreshCycle: Duration,
                        var serverHelloRequestCycle: Duration,
                        var saveGroupIDs: Boolean,
                        var saveSubjects: Boolean){

        fun dataRefreshCycle(dataRefreshCycle: Duration) = apply { this.dataRefreshCycle = dataRefreshCycle }
        fun workingDirectory(workingDirectory: File) = apply { this.workingDirectory = workingDirectory }
        fun temporary(temporary: Boolean) = apply { this.temporary = temporary }
        fun saveGroupIDs(saveGroupIDs: Boolean) = apply { this.saveGroupIDs = saveGroupIDs }
        fun saveSubjects(saveSubjects: Boolean) = apply { this.saveSubjects = saveSubjects }

        fun serverHelloRequestCycle(serverHelloRequestCycle: Duration) = apply { this.serverHelloRequestCycle = serverHelloRequestCycle }

        fun build() = Settings(
            dataRefreshCycle = dataRefreshCycle,
            workingDirectory = workingDirectory,
            temporary = temporary,
            saveGroupIDs = saveGroupIDs,
            serverHelloRequestCycle = serverHelloRequestCycle,
            saveSubjects = saveSubjects
        )

    }

}