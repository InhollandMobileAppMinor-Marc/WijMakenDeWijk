package nl.woonwaard.wij_maken_de_wijk.fake_api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.services.CommentsRepository
import java.util.*

class FakeCommentsRepository : CommentsRepository {
    override suspend fun getCommentsForPost(post: Post): Set<Comment> {
        return withContext(Dispatchers.IO) {
            delay(1000)
            setOf(
                Comment(
                    id = 11,
                    "Bij mij werkt het ook niet meer, " +
                            "ik denk dat er een storing is... Heb jij Ziggo?",
                    creator = FakeUsers.ROB.user,
                    Date(2020, 11, 26, 14, 0)
                ), Comment(
                    id = 21,
                    "Dat lijkt mij heel erg leuk, " +
                            "wat voor type koken ga je een focus opleggen?",
                    creator = FakeUsers.NATALIE.user,
                    Date(2020, 11, 25, 20, 15)
                ), Comment(
                    id = 31,
                    "Ze gaan bij mij morgen de kookplaat op gas " +
                            "vervangen door een elektrische, " +
                            "dus deze tips zijn erg handig. Dankje!",
                    creator = FakeUsers.JEFFREY.user,
                    Date(2020, 11, 26, 8, 30)
                ), Comment(
                    id = 41,
                    "Ik heb er nog eentje liggen! " +
                            "Je kunt hem vanavond bij mij ophalen (appartement 53)",
                    creator = FakeUsers.JEFFREY.user,
                    Date(2020, 11, 23, 15, 30)
                )
            ).filter {
                it.id in post.commentIds
            }.toSet()
        }
    }
}
