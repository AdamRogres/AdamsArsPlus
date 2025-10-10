package com.adamsmods.adamsarsplus.util;

import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;

import com.adamsmods.adamsarsplus.glyphs.effect_glyph.*;
import com.adamsmods.adamsarsplus.glyphs.method_glyph.PropagateDetonate;
import com.hollingsworth.arsnouveau.api.event.EffectResolveEvent;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.*;

public class SpellString {

    public static AbstractSpellPart stringSpellComponent(String part){
        AbstractSpellPart retPart;

        retPart = EffectLight.INSTANCE;

        switch(part){
            // Augments
            case "aoe" -> {
                retPart = AugmentAOE.INSTANCE;
            }
            case "aoe_two" -> {
                retPart = AugmentAOETwo.INSTANCE;
            }
            case "aoe_three" -> {
                retPart = AugmentAOEThree.INSTANCE;
            }
            case "lesser_aoe" -> {
                retPart = AugmentLesserAOE.INSTANCE;
            }
            case "accelerate" -> {
                retPart = AugmentAccelerate.INSTANCE;
            }
            case "accelerate_two" -> {
                retPart = AugmentAccelerateTwo.INSTANCE;
            }
            case "accelerate_three" -> {
                retPart = AugmentAccelerateThree.INSTANCE;
            }
            case "amp" -> {
                retPart = AugmentAmplify.INSTANCE;
            }
            case "amp_two" -> {
                retPart = AugmentAmplifyTwo.INSTANCE;
            }
            case "amp_three" -> {
                retPart = AugmentAmplifyThree.INSTANCE;
            }
            case "extend" -> {
                retPart = AugmentExtendTime.INSTANCE;
            }
            case "extend_two" -> {
                retPart = AugmentExtendTimeTwo.INSTANCE;
            }
            case "extend_three" -> {
                retPart = AugmentExtendTimeThree.INSTANCE;
            }
            case "sensitive" -> {
                retPart = AugmentSensitive.INSTANCE;
            }
            case "duration_down" -> {
                retPart = AugmentDurationDown.INSTANCE;
            }
            case "duration_down_two" -> {
                retPart = AugmentDurationDownTwo.INSTANCE;
            }
            case "duration_down_three" -> {
                retPart = AugmentDurationDownThree.INSTANCE;
            }
            case "pierce" -> {
                retPart = AugmentPierce.INSTANCE;
            }
            case "split" -> {
                retPart = AugmentSplit.INSTANCE;
            }
            case "dampen" -> {
                retPart = AugmentDampen.INSTANCE;
            }
            case "dampen_two" -> {
                retPart = AugmentDampenTwo.INSTANCE;
            }
            case "dampen_three" -> {
                retPart = AugmentDampenThree.INSTANCE;
            }
            case "extract" -> {
                retPart = AugmentExtract.INSTANCE;
            }
            case "decelerate" -> {
                retPart = AugmentDecelerate.INSTANCE;
            }
            case "fortune" -> {
                retPart = AugmentFortune.INSTANCE;
            }
            case "randomize" -> {
                retPart = AugmentRandomize.INSTANCE;
            }

            // Effects
            case "light" -> {
                retPart = EffectLight.INSTANCE;
            }
            case "heal" -> {
                retPart = EffectHeal.INSTANCE;
            }
            case "dispel" -> {
                retPart = EffectDispel.INSTANCE;
            }
            case "burst" -> {
                retPart = EffectBurst.INSTANCE;
            }
            case "limitless" -> {
                retPart = EffectLimitless.INSTANCE;
            }
            case "blink" -> {
                retPart = EffectBlink.INSTANCE;
            }
            case "knockback" -> {
                retPart = EffectKnockback.INSTANCE;
            }
            case "delay" -> {
                retPart = EffectDelay.INSTANCE;
            }
            case "snare" -> {
                retPart = EffectSnare.INSTANCE;
            }
            case "gravity" -> {
                retPart = EffectGravity.INSTANCE;
            }
            case "raise_earth" -> {
                retPart = EffectRaiseEarth.INSTANCE;
            }
            case "annihilate" -> {
                retPart = EffectAnnihilate.INSTANCE;
            }
            case "meteor_swarm" -> {
                retPart = EffectMeteorSwarm.INSTANCE;
            }
            case "explosion" -> {
                retPart = EffectExplosion.INSTANCE;
            }
            case "ice_burst" -> {
                retPart = EffectIceburst.INSTANCE;
            }
            case "divine_smite" -> {
                retPart = EffectDivineSmite.INSTANCE;
            }
            case "break" -> {
                retPart = EffectBreak.INSTANCE;
            }
            case "evaporate" -> {
                retPart = EffectEvaporate.INSTANCE;
            }
            case "eruption" -> {
                retPart = EffectEruption.INSTANCE;
            }
            case "fracture" -> {
                retPart = EffectFracture.INSTANCE;
            }
            case "summon_undead" -> {
                retPart = EffectSummonUndead_boss.INSTANCE;
            }
            case "simple_domain" -> {
                retPart = EffectSimpleDomain.INSTANCE;
            }
            case "domain" -> {
                retPart = EffectDomain.INSTANCE;
            }
            case "fangs" -> {
                retPart = EffectFangs.INSTANCE;
            }
            case "linger" -> {
                retPart = EffectLinger.INSTANCE;
            }
            case "harm" -> {
                retPart = EffectHarm.INSTANCE;
            }
            case "hex" -> {
                retPart = EffectHex.INSTANCE;
            }
            case "wither" -> {
                retPart = EffectWither.INSTANCE;
            }
            case "windshear" -> {
                retPart = EffectWindshear.INSTANCE;
            }
            case "launch" -> {
                retPart = EffectLaunch.INSTANCE;
            }
            case "leap" -> {
                retPart = EffectLeap.INSTANCE;
            }
            case "lightning" -> {
                retPart = EffectLightning.INSTANCE;
            }
            case "swap_target" -> {
                retPart = EffectSwapTarget.INSTANCE;
            }
            case "ignite" -> {
                retPart = EffectIgnite.INSTANCE;
            }
            case "slowfall" -> {
                retPart = EffectSlowfall.INSTANCE;
            }
            case "crush" -> {
                retPart = EffectCrush.INSTANCE;
            }
            case "freeze" -> {
                retPart = EffectFreeze.INSTANCE;
            }
            case "cold_snap" -> {
                retPart = EffectColdSnap.INSTANCE;
            }
            case "conjure_water" -> {
                retPart = EffectConjureWater.INSTANCE;
            }
            case "mageblock" -> {
                retPart = EffectPhantomBlock.INSTANCE;
            }
            case "cut" -> {
                retPart = EffectCut.INSTANCE;
            }
            case "flare" -> {
                retPart = EffectFlare.INSTANCE;
            }
            case "animate" -> {
                retPart = EffectAnimate.INSTANCE;
            }
            case "bounce" -> {
                retPart = EffectBounce.INSTANCE;
            }
            case "craft" -> {
                retPart = EffectCraft.INSTANCE;
            }
            case "ender_chest" -> {
                retPart = EffectEnderChest.INSTANCE;
            }
            case "exchange" -> {
                retPart = EffectExchange.INSTANCE;
            }
            case "fell" -> {
                retPart = EffectFell.INSTANCE;
            }
            case "firework" -> {
                retPart = EffectFirework.INSTANCE;
            }
            case "glide" -> {
                retPart = EffectGlide.INSTANCE;
            }
            case "grow" -> {
                retPart = EffectGrow.INSTANCE;
            }
            case "harvest" -> {
                retPart = EffectHarvest.INSTANCE;
            }
            case "infuse" -> {
                retPart = EffectInfuse.INSTANCE;
            }
            case "intangible" -> {
                retPart = EffectIntangible.INSTANCE;
            }
            case "interact" -> {
                retPart = EffectInteract.INSTANCE;
            }
            case "invisibility" -> {
                retPart = EffectInvisibility.INSTANCE;
            }
            case "name" -> {
                retPart = EffectName.INSTANCE;
            }
            case "pickup" -> {
                retPart = EffectPickup.INSTANCE;
            }
            case "place" -> {
                retPart = EffectPlaceBlock.INSTANCE;
            }
            case "pull" -> {
                retPart = EffectPull.INSTANCE;
            }
            case "redstone" -> {
                retPart = EffectRedstone.INSTANCE;
            }
            case "rotate" -> {
                retPart = EffectRotate.INSTANCE;
            }
            case "rune" -> {
                retPart = EffectRune.INSTANCE;
            }
            case "sense_magic" -> {
                retPart = EffectSenseMagic.INSTANCE;
            }
            case "smelt" -> {
                retPart = EffectSmelt.INSTANCE;
            }
            case "toss" -> {
                retPart = EffectToss.INSTANCE;
            }
            case "wall" -> {
                retPart = EffectWall.INSTANCE;
            }
            case "detonate" -> {
                retPart = PropagateDetonate.INSTANCE;
            }
        }

        return retPart;
    }

    public static ParticleColor stringColor(String color){
        int r = 0;
        int g = 0;
        int b = 0;

        switch(color){
            case "white"        -> r = 255;
            case "light_gray"   -> r = 180;
            case "gray"         -> r = 100;
            case "black"        -> r = 0;
            case "red"          -> r = 255;
            case "orange"       -> r = 255;
            case "yellow"       -> r = 182;
            case "lime"         -> r = 255;
            case "green"        -> r = 0;
            case "light_blue"   -> r = 200;
            case "cyan"         -> r = 0;
            case "blue"         -> r = 0;
            case "purple"       -> r = 255;
            case "magenta"      -> r = 120;
            case "pink"         -> r = 255;
            case "brown"        -> r = 132;

            default             -> r = 255;
        }

        switch(color){
            case "white"        -> g = 255;
            case "light_gray"   -> g = 180;
            case "gray"         -> g = 100;
            case "black"        -> g = 0;
            case "red"          -> g = 0;
            case "orange"       -> g = 106;
            case "yellow"       -> g = 255;
            case "lime"         -> g = 255;
            case "green"        -> g = 255;
            case "light_blue"   -> g = 200;
            case "cyan"         -> g = 82;
            case "blue"         -> g = 0;
            case "purple"       -> g = 0;
            case "magenta"      -> g = 55;
            case "pink"         -> g = 0;
            case "brown"        -> g = 95;

            default             -> g = 255;
        }

        switch(color){
            case "white"        -> b = 255;
            case "light_gray"   -> b = 180;
            case "gray"         -> b = 100;
            case "black"        -> b = 0;
            case "red"          -> b = 0;
            case "orange"       -> b = 0;
            case "yellow"       -> b = 0;
            case "lime"         -> b = 0;
            case "green"        -> b = 0;
            case "light_blue"   -> b = 255;
            case "cyan"         -> b = 105;
            case "blue"         -> b = 255;
            case "purple"       -> b = 255;
            case "magenta"      -> b = 105;
            case "pink"         -> b = 110;
            case "brown"        -> b = 59;

            default             -> b = 255;
        }

        ParticleColor returnColor = new ParticleColor(r,g,b);

        return returnColor;
    }
}
