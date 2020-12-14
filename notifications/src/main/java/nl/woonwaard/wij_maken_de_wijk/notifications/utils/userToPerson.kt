package nl.woonwaard.wij_maken_de_wijk.notifications.utils

import androidx.core.app.Person
import nl.woonwaard.wij_maken_de_wijk.domain.models.User

val User.asPerson: Person
    get() = Person.Builder().setName(nameWithLocation).build()
