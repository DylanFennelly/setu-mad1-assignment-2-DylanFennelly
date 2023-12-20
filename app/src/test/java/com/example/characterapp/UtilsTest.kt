package com.example.characterapp

import com.example.characterapp.helpers.calculateMod
import com.example.characterapp.helpers.validateAbilityScoreInput
import com.example.characterapp.helpers.validateLevelInput
import org.junit.Test

import org.junit.Assert.*


class UtilsTest {
    @Test
    fun validateLevelInput_test(){
        assertTrue("Boolean does not equal true", validateLevelInput("1"))         //min case
        assertTrue("Boolean does not equal true", validateLevelInput("9"))
        assertTrue("Boolean does not equal true", validateLevelInput("10"))        //10 case
        assertTrue("Boolean does not equal true", validateLevelInput("15"))
        assertTrue("Boolean does not equal true", validateLevelInput("19"))
        assertTrue("Boolean does not equal true", validateLevelInput("20"))        //max case

        assertFalse("Boolean does not equal false", validateLevelInput(""))         //empty case
        assertFalse("Boolean does not equal false", validateLevelInput("0"))        //min edge case
        assertFalse("Boolean does not equal false", validateLevelInput("21"))       //max edge case
        assertFalse("Boolean does not equal false", validateLevelInput("30"))
        assertFalse("Boolean does not equal false", validateLevelInput("-1"))       //negative case
        assertFalse("Boolean does not equal false", validateLevelInput("-20"))
        assertFalse("Boolean does not equal false", validateLevelInput("abc"))      //alphabetical case
    }

    @Test
    fun validateAbilityScoreInput_test(){
        assertTrue("Boolean does not equal true", validateAbilityScoreInput("1"))         //min case
        assertTrue("Boolean does not equal true", validateAbilityScoreInput("9"))
        assertTrue("Boolean does not equal true", validateAbilityScoreInput("10"))        //10 case
        assertTrue("Boolean does not equal true", validateAbilityScoreInput("19"))
        assertTrue("Boolean does not equal true", validateAbilityScoreInput("20"))        //20
        assertTrue("Boolean does not equal true", validateAbilityScoreInput("21"))        //20
        assertTrue("Boolean does not equal true", validateAbilityScoreInput("30"))        //max case

        assertFalse("Boolean does not equal false", validateAbilityScoreInput(""))         //empty case
        assertFalse("Boolean does not equal false", validateAbilityScoreInput("0"))        //min edge case
        assertFalse("Boolean does not equal false", validateAbilityScoreInput("31"))       //max edge case
        assertFalse("Boolean does not equal false", validateAbilityScoreInput("40"))
        assertFalse("Boolean does not equal false", validateAbilityScoreInput("-1"))       //negative case
        assertFalse("Boolean does not equal false", validateAbilityScoreInput("-30"))
        assertFalse("Boolean does not equal false", validateAbilityScoreInput("abc"))      //alphabetical case
    }

    @Test
    fun testCalculateMod() {
        assertEquals("3", calculateMod("16"))        //standard case
        assertEquals("2", calculateMod("15")  )        //case with decimal
        assertEquals("-1", calculateMod("8"),)        //case with minus
        assertEquals("-2", calculateMod("7"),)        //case with minus and decimal
        assertEquals("-5", calculateMod("1"),)        //lower edge case
        assertEquals("10", calculateMod("30"),)        //upper edge case
        assertEquals("0", calculateMod(""))        //blank case
        assertEquals("0", calculateMod("abc"),)        //letter case
        assertEquals("0", calculateMod("12.2"))        //decimal case
        assertEquals("0", calculateMod("12.8"))        //decimal case - higher decimal
    }

}