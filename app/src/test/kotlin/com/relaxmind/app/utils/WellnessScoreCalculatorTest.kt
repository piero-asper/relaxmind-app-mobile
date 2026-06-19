package com.relaxmind.app.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class WellnessScoreCalculatorTest {

    @Test
    fun testDocumentationExampleScore() {
        // Documentation example details:
        // - emotionalState average: 3.6 (from days: 3, 4, 5, 2, 4)
        // - sleep: 4.0 (derived from 8/2 = 4)
        // - energy: 6.0 (out of 10)
        // - stress: 7.0 (out of 10)
        // - binaryAnswers: 1.0 (from "Sí", representing 1 out of 5 binary questions, average = 0.2)
        // Expected total score: 64/100
        val score = WellnessScoreCalculator.calculateScore(
            emotionalState = 3.6,
            sleep = 4.0,
            energy = 6.0,
            stress = 7.0,
            frequencyAnswers = listOf(2.0, 3.0, 4.0, 1.0, 5.0),
            binaryAnswers = listOf(1.0, 0.0, 0.0, 0.0, 0.0)
        )
        
        assertEquals(64, score)
    }

    @Test
    fun testPerfectWellnessScore() {
        // Perfect inputs should map to 100/100
        val perfectAnswers = CheckInAnswers(
            emotionalState = 5,
            sleep = 5,
            energy = 10,
            stress = 10,
            frequencyAnswers = listOf(5, 5, 5),
            binaryAnswers = listOf(1, 1)
        )
        
        val score = WellnessScoreCalculator.calculateScore(perfectAnswers)
        assertEquals(100, score)
    }

    @Test
    fun testWorstWellnessScore() {
        // Minimum inputs calculation:
        // b1: 1 / 5 * 0.3 = 0.06
        // b2: (1/5 + 1/10 + 1/10) / 3 * 0.40 = 0.4 / 3 * 0.4 = 0.05333...
        // b3: 1 / 5 * 0.20 = 0.04
        // b4: 0.0
        // Sum = 0.06 + 0.05333 + 0.04 = 0.15333... -> 15/100
        val worstAnswers = CheckInAnswers(
            emotionalState = 1,
            sleep = 1,
            energy = 1,
            stress = 1,
            frequencyAnswers = listOf(1, 1, 1),
            binaryAnswers = listOf(0, 0)
        )
        
        val score = WellnessScoreCalculator.calculateScore(worstAnswers)
        assertEquals(15, score)
    }

    @Test
    fun testNullSleepDefaultsToRegular() {
        // When sleep is null, it should default to 3 (Regular)
        // If we compare answers with null sleep vs answers with sleep = 3, they should yield the same score
        val answersWithNullSleep = CheckInAnswers(
            emotionalState = 3,
            sleep = null,
            energy = 5,
            stress = 5,
            frequencyAnswers = listOf(3, 3),
            binaryAnswers = listOf(1)
        )
        
        val answersWithRegularSleep = CheckInAnswers(
            emotionalState = 3,
            sleep = 3,
            energy = 5,
            stress = 5,
            frequencyAnswers = listOf(3, 3),
            binaryAnswers = listOf(1)
        )
        
        val scoreNull = WellnessScoreCalculator.calculateScore(answersWithNullSleep)
        val scoreRegular = WellnessScoreCalculator.calculateScore(answersWithRegularSleep)
        
        assertEquals(scoreRegular, scoreNull)
    }

    @Test
    fun testCategoryResolution() {
        assertEquals("Muy bajo", WellnessScoreCalculator.getCategory(15))
        assertEquals("Bajo", WellnessScoreCalculator.getCategory(30))
        assertEquals("Moderado", WellnessScoreCalculator.getCategory(50))
        assertEquals("Bueno", WellnessScoreCalculator.getCategory(75))
        assertEquals("Excelente", WellnessScoreCalculator.getCategory(95))
    }
}
