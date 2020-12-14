package nl.woonwaard.wij_maken_de_wijk.domain

import nl.woonwaard.wij_maken_de_wijk.domain.utils.toSentenceCasing
import org.junit.Assert
import org.junit.Test

class StringUtilsTests {
    @Test
    fun testSentenceCasingWithUpperCasedString() {
        // Arrange
        val inputText = "HELLO WORLD!"
        val expectedResult = "Hello world!"

        // Act
        val actualResult = inputText.toSentenceCasing()

        // Assert
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun testSentenceCasingWithLowerCasedString() {
        // Arrange
        val inputText = "hello world!"
        val expectedResult = "Hello world!"

        // Act
        val actualResult = inputText.toSentenceCasing()

        // Assert
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun testSentenceCasingWithMixedCasedString() {
        // Arrange
        val inputText = "hELlO WoRlD!"
        val expectedResult = "Hello world!"

        // Act
        val actualResult = inputText.toSentenceCasing()

        // Assert
        Assert.assertEquals(expectedResult, actualResult)
    }
}
