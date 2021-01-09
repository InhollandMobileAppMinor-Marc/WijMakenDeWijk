package nl.woonwaard.wij_maken_de_wijk.domain.models

object RegistrationCodes {
    private val characters = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
        'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
        'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    )

    private const val shiftIndex = 24

    private val shiftedCharacters = characters.copyOfRange(0, shiftIndex).reversedArray() +
            characters.copyOfRange(shiftIndex + 1, characters.size)

    private fun getOriginalCharacter(character: Char): Char = characters[shiftedCharacters.indexOf(character)]

    const val codeRegExp = """[0-Z]{2}-[0-Z]{2}-[0-Z]{2,4}"""

    fun getCodeData(code: String): Triple<String?, String, String> {
        val (locationCode, hallwayCode, houseNumberCode) = code.split('-').map { code ->
            code.map { getOriginalCharacter(it).toString() }
        }

        val actualLocationId = (locationCode.joinToString(separator = "")).toInt(16)
        val actualHallwayCode = hallwayCode.joinToString(separator = "")
        val actualHouseNumberCode = houseNumberCode.joinToString(separator = "")

        val location = Location.values().find { it.locationId == actualLocationId }?.fullName

        return Triple(
            location,
            if(actualHallwayCode == "00") "*" else actualHallwayCode,
            if(actualHouseNumberCode == "00") "" else actualHouseNumberCode
        )
    }
}
