package nl.woonwaard.wij_maken_de_wijk.domain.models

import java.io.Serializable

enum class Location(val locationId: Int, val fullName: String) : Serializable {
    TEST_LOCATION(0x00, "Test location"),
    VAN_DE_VELDELAAN(0x01, "Van de Veldelaan"),
    DILLENBURGSTRAAT(0x02, "Dillenburgstraat")
}
