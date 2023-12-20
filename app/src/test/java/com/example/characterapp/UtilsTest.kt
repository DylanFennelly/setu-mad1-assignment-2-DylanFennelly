package com.example.characterapp

import com.example.characterapp.helpers.calculateHP
import com.example.characterapp.helpers.calculateMod
import com.example.characterapp.helpers.validateAbilityScoreInput
import com.example.characterapp.helpers.validateLevelInput
import org.junit.Test
import com.example.characterapp.R

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
    fun calculateMod_test() {
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

    @Test
    fun calculateHP_test(){
        assertEquals("Incorrect max HP", 10.toShort(), calculateHP("1", "Bard", "14"))           //level 1 bard, 14 con: 8 + 2 = 10     (simple case)
        assertEquals("Incorrect max HP", 17.toShort(), calculateHP("3", "Wizard", "12"))         //level 3 wizard, 12 con: 6 + 1 + (4+1) * 2 = 17   (level up case)
        assertEquals("Incorrect max HP", 345.toShort(), calculateHP("20", "Barbarian", "30"))    //level 20 barbarian, 30 con: 12 + 10 + (7+10)*19 = 345    (upper bound)
        assertEquals("Incorrect max HP", 3.toShort(), calculateHP("3", "Sorcerer", "1"))         //level 3 sorcerer, 1 con: 6 - 5 + [(4-5) -1 < 1? 1] * 2 = 3   (negative HP per level case - defaults to 1 hp per level)
        assertEquals("Incorrect max HP", 0.toShort(), calculateHP("1", "Invalid", "10"))                        //Level 1 no class, 10 con = 0     (for if calculation is done before class assigned)
        assertEquals("Incorrect max HP", 19.toShort(), calculateHP("20", "Invalid", "10"))                      //Level 20 no class, 10 con = 0 + 1*19 = 19     (if level up would give 0 hp, defaults to 1)

        assertEquals("Incorrect max HP", 6.toShort(), calculateHP("21", "Wizard", "10"))         //level 21 wizard, 10 con: 6 + 0 + (4+0) * 20 = 86 ->! invalid level = 6 +0+0 = 6 (invalid level)
        assertEquals("Incorrect max HP", 6.toShort(), calculateHP("1", "Wizard", "100"))         //level 3 wizard, 100 con: 6 + 50 + (4+50) * 2 = 164 ->! invalid CON = 6 + 0 + (4+0)*2 = 14 (invalid CON)
    }

}