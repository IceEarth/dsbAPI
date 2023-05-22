package kord

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import java.util.EventListener

class KordListener(val kord: Kord) {

    fun listen(){
        kord.on<MessageCreateEvent> {

        }

    }


}