package nl.woonwaard.wij_maken_de_wijk.fake_api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.services.CommentsApi
import java.util.*

class FakeCommentsApi : CommentsApi {
    override suspend fun getCommentsForPost(post: Post): Set<Comment> {
        return withContext(Dispatchers.IO) {
            delay(1000)
            setOf(
                Comment(
                    id = 11,
                    "Bij mij werkt het ook niet meer, " +
                            "ik denk dat er een storing is... Heb jij Ziggo?",
                    creatorAccountId = FakeUsers.ROB.id,
                    Date()
                ), Comment(
                    id = 21,
                    "Dat lijkt mij heel erg leuk, " +
                            "wat voor type koken ga je een focus opleggen?",
                    creatorAccountId = FakeUsers.NATALIE.id,
                    Date()
                ), Comment(
                    id = 31,
                    "Ze gaan bij mij morgen de kookplaat op gas " +
                            "vervangen door een elektrische, " +
                            "dus deze tips zijn erg handig. Dankje!",
                    creatorAccountId = FakeUsers.JEFFREY.id,
                    Date()
                ), Comment(
                    id = 41,
                    "Ik heb er nog eentje liggen! " +
                            "Je kunt hem vanavond bij mij ophalen (appartement 53)",
                    creatorAccountId = FakeUsers.JEFFREY.id,
                    Date()
                )
            ).filter {
                it.id in post.commentIds
            }.toSet()
        }
    }
}
