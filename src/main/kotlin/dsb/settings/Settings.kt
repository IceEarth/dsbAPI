package dsb.settings

import java.io.File
import java.time.Duration

/**
 * Datenklasse, die die Einstellungen für die Anwendung repräsentiert.
 *
 * @property reputationsCycle Die Dauer des Reputationszyklus.
 * @property workingDirectory Das Arbeitsverzeichnis für die Anwendung.
 * @property temporary Flag, das angibt, ob die Anwendung im temporären Modus läuft.
 * @property saveGroupIDs Flag, das angibt, ob Gruppen-IDs gespeichert werden sollen.
 * @property saveSubjects Flag, das angibt, ob Fächer gespeichert werden sollen.
 */
data class Settings (
    val reputationsCycle: Duration,
    val workingDirectory: File,
    val temporary: Boolean,
    val saveGroupIDs: Boolean,
    val saveSubjects: Boolean
) {

    companion object {
        /**
         * Standard-Einstellungen für die Anwendung.
         */
        val defaults = Settings(
            reputationsCycle = Duration.ofMinutes(5),
            workingDirectory = File(System.getProperty("user.dir")),
            temporary = false,
            saveGroupIDs = false,
            saveSubjects = false
        )
    }

    /**
     * Builder-Klasse zum Erstellen von Settings-Objekten mit individuellen Werten.
     *
     * @property reputationsCycle Die Dauer des Reputationszyklus.
     * @property workingDirectory Das Arbeitsverzeichnis für die Anwendung.
     * @property temporary Flag, das angibt, ob die Anwendung im temporären Modus läuft.
     * @property saveGroupIDs Flag, das angibt, ob Gruppen-IDs gespeichert werden sollen.
     * @property saveSubjects Flag, das angibt, ob Fächer gespeichert werden sollen.
     */
    data class Builder(var reputationsCycle: Duration = defaults.reputationsCycle,
                       var workingDirectory: File = defaults.workingDirectory,
                       var temporary: Boolean = defaults.temporary,
                       var saveGroupIDs: Boolean = defaults.saveGroupIDs,
                       var saveSubjects: Boolean = defaults.saveSubjects){

        fun reputationsCycle(reputationsCycle: Duration) = apply { this.reputationsCycle = reputationsCycle }
        fun workingDirectory(workingDirectory: File) = apply { this.workingDirectory = workingDirectory }
        fun temporary(temporary: Boolean) = apply { this.temporary = temporary }
        fun saveGroupIDs(saveGroupIDs: Boolean) = apply { this.saveGroupIDs = saveGroupIDs }
        fun saveSubjects(saveSubjects: Boolean) = apply { this.saveSubjects = saveSubjects }

        fun build() = Settings(
            reputationsCycle = reputationsCycle,
            workingDirectory = workingDirectory,
            temporary = temporary,
            saveGroupIDs = saveGroupIDs,
            saveSubjects = saveSubjects
        )

    }

}