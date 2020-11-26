package nl.woonwaard.wij_maken_de_wijk.fake_api

import nl.woonwaard.wij_maken_de_wijk.domain.models.User
import nl.woonwaard.wij_maken_de_wijk.domain.utils.toSentenceCasing

enum class FakeUsers(val id: Int) {
    WOONWAARD(0),
    ANITA(1),
    ANJA(2),
    ROB(3),
    NATALIE(4),
    JEFFREY(5),
    JOHN(6);

    val user: User
        get() = User(id, name.toSentenceCasing())
}
