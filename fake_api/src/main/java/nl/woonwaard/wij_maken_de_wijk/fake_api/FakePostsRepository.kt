package nl.woonwaard.wij_maken_de_wijk.fake_api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.models.PostType
import nl.woonwaard.wij_maken_de_wijk.domain.services.PostsRepository
import java.util.*

class FakePostsRepository : PostsRepository {
    override suspend fun getPosts(types: List<PostType>): Set<Post> {
        return withContext(Dispatchers.IO) {
            delay(1000)
            setOf(
                Post(
                    id = 10,
                    "WiFi defect, hulp nodig!",
                    PostType.SERVICE,
                    "Mijn WiFi is zojuist stuk gegaan, is er iemand die mij hiermee kan helpen? " +
                            "Ik heb er geen verstand van en heb het morgen nodig \uD83D\uDE30",
                    creator = FakeUsers.ANITA.user,
                    Date(2020, 11, 26, 9, 0),
                    setOf(11)
                ), Post(
                    id = 20,
                    "Kookclub",
                    PostType.EVENT,
                    "Hoi iedereen,\n\n" +
                            "Ik vind het altijd erg leuk om te koken en\n" +
                            "zou mijn passie graag willen delen met andere mensen. " +
                            "Ik zou daarom een kookclub willen starten. " +
                            "Geef aan of je hier aan mee ze wou willen doen.",
                    creator = FakeUsers.ANJA.user,
                    Date(2020, 11, 25, 14, 0),
                    setOf(21)
                ), Post(
                    id = 30,
                    "Elektrisch koken",
                    PostType.SUSTAINABILITY,
                    "Hoi iedereen,\n" +
                            "Ik heb wat tips en tricks voor elektrisch koken. " +
                            "Als jullie ook tips of tricks hiervoor hebben laat het weten. " +
                            "Mijn tips zijn:\n" +
                            "\n" +
                            "Tip 1: hou rekening met opwarmtijd\n" +
                            "Tip 2: pannen met rechte bodem\n" +
                            "Tip 3: gas terug werkt niet meteen\n" +
                            "Tip 4: zet hem eerder uit\n" +
                            "Tip 5: de pit blijft heet\n" +
                            "Tip 6: geniet van het schoonmaken\n",
                    creator = FakeUsers.ANJA.user,
                    Date(2020, 11, 25, 10, 30),
                    setOf(31)
                ), Post(
                    id = 40,
                    "Figuurzaag nodig",
                    PostType.SERVICE,
                    "Hoi iedereen,\n\n" +
                            "Voor een schoolproject heeft mijn zoon een figuurzaag nodig. " +
                            "Is er iemand die dit kan uitlenen? Alvast bedankt!",
                    creator = FakeUsers.ROB.user,
                    Date(2020, 11, 22, 17, 45),
                    setOf(41)
                )
            ).filter {
                it.type in types
            }.toSet()
        }
    }

    override suspend fun getPostById(id: Int) = getPosts().firstOrNull { it.id == id }
}
