package nl.woonwaard.wij_maken_de_wijk.fake_api

import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.models.PostType

object FakeData {
    val comments = mutableSetOf(
        Comment(
            id = 11,
            "Bij mij werkt het ook niet meer, " +
                    "ik denk dat er een storing is... Heb jij Ziggo?",
            creator = FakeUsers.ROB.user,
            createDate(2020, 11, 26, 14, 0)
        ), Comment(
            id = 21,
            "Dat lijkt mij heel erg leuk, " +
                    "wat voor type koken ga je een focus opleggen?",
            creator = FakeUsers.NATALIE.user,
            createDate(2020, 11, 25, 20, 15)
        ), Comment(
            id = 31,
            "Ze gaan bij mij morgen de kookplaat op gas " +
                    "vervangen door een elektrische, " +
                    "dus deze tips zijn erg handig. Dankje!",
            creator = FakeUsers.JEFFREY.user,
            createDate(2020, 11, 26, 8, 30)
        ), Comment(
            id = 41,
            "Ik heb er nog eentje liggen! " +
                    "Je kunt hem vanavond bij mij ophalen (appartement 53)",
            creator = FakeUsers.JEFFREY.user,
            createDate(2020, 11, 23, 15, 30)
        )
    )

    val posts = mutableSetOf(
        Post(
            id = 10,
            "WiFi defect, hulp nodig!",
            PostType.SERVICE,
            "Mijn WiFi is zojuist stuk gegaan, is er iemand die mij hiermee kan helpen? " +
                    "Ik heb er geen verstand van en heb het morgen nodig \uD83D\uDE30",
            creator = FakeUsers.ANITA.user,
            createDate(2020, 11, 26, 9, 0),
            mutableSetOf(11)
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
            createDate(2020, 11, 25, 14, 0),
            mutableSetOf(21)
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
            createDate(2020, 11, 25, 10, 30),
            mutableSetOf(31)
        ), Post(
            id = 40,
            "Figuurzaag nodig",
            PostType.SERVICE,
            "Hoi iedereen,\n\n" +
                    "Voor een schoolproject heeft mijn zoon een figuurzaag nodig. " +
                    "Is er iemand die dit kan uitlenen? Alvast bedankt!",
            creator = FakeUsers.ROB.user,
            createDate(2020, 11, 22, 17, 45),
            mutableSetOf(41)
        )
    )
}
