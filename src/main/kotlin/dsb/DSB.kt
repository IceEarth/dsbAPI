package dsb

import plan.RepresentationPlan
import java.time.Duration

class DSB (val username: String?,
           val password: String?,
           val reputationsCycle: Duration?,){


    fun readRepresentationPlans(){
        TODO()
    }


    //TODO add Methods and Attributes

    data class Builder(var username: String? = null,
                       var password: String? = null,
                       var reputationsCycle: Duration? = Duration.ofMinutes(5)) {


        fun username(username: String) = apply{this.username = username}
        fun password(password: String) = apply{this.password = password}
        fun reputationsCycle(reputationsCycle: Duration) = apply{this.reputationsCycle = reputationsCycle}

        fun build(): DSB {
            requireNotNull(username){"username must not be null"}
            requireNotNull(password){"password must not be null"}
            return DSB(username, password, reputationsCycle)
        }

    }
}