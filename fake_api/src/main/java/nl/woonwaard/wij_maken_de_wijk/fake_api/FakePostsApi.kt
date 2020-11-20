package nl.woonwaard.wij_maken_de_wijk.fake_api

import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.models.PostType
import nl.woonwaard.wij_maken_de_wijk.domain.services.PostsApi
import java.util.*

class FakePostsApi : PostsApi {
    override suspend fun getPosts(types: List<PostType>): List<Post> {
        return listOf(
            Post(
                id = 0,
                "WiFi defect, hulp nodig!",
                PostType.SERVICE,
                "Mijn WiFi is zojuist stuk gegaan, is er iemand die mij hiermee kan helpen? " +
                        "Ik heb er geen verstand van en heb het morgen nodig \uD83D\uDE30",
                creatorAccountId = 0,
                Date(),
                emptyList()
            ), Post(
                id = 1,
                "Kookclub",
                PostType.EVENT,
                "Hoi iedereen,\n\n" +
                        "Ik vind het altijd erg leuk om te koken en\n" +
                        "zou mijn passie graag willen delen met andere mensen. " +
                        "Ik zou daarom een kookclub willen starten. " +
                        "Geef aan of je hier aan mee ze wou willen doen.",
                creatorAccountId = 1,
                Date(),
                emptyList()
            ), Post(
                id = 2,
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
                creatorAccountId = 1,
                Date(),
                emptyList()
            ), Post(
                id = 3,
                "Figuurzaag nodig",
                PostType.SERVICE,
                "Hoi iedereen,\n\n" +
                        "Voor een schoolproject heeft mijn zoon een figuurzaag nodig. " +
                        "Is er iemand die dit kan uitlenen? Alvast bedankt!",
                creatorAccountId = 2,
                Date(),
                emptyList()
            )
        ).filter {
            it.type in types
        }
    }

    override suspend fun getPostById(id: Int) = getPosts().firstOrNull { it.id == id }
}
