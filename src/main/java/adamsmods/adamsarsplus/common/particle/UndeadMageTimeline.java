package adamsmods.adamsarsplus.common.particle;

import adamsmods.adamsarsplus.common.glyphs.effect_glyph.EffectSummonUndead_boss;
import com.hollingsworth.arsnouveau.api.particle.timelines.*;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.BaseProperty;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import java.util.List;

/** Melee spell-impact style belonging only to Summon Undead Mage. */
public class UndeadMageTimeline extends BaseTimeline<UndeadMageTimeline> {
    public final TouchTimeline settings;
    public static final MapCodec<UndeadMageTimeline> CODEC = TouchTimeline.CODEC.xmap(UndeadMageTimeline::new, value -> value.settings);
    public static final StreamCodec<RegistryFriendlyByteBuf, UndeadMageTimeline> STREAM = TouchTimeline.STREAM_CODEC.map(UndeadMageTimeline::new, value -> value.settings);

    public UndeadMageTimeline() { this(new TouchTimeline()); }
    public UndeadMageTimeline(TouchTimeline settings) { this.settings = settings; }
    public IParticleTimelineType<UndeadMageTimeline> getType() { return ModDomainTimelines.UNDEAD_MAGE.get(); }
    public List<BaseProperty<?>> getProperties() { return settings.getProperties(); }

    public static class Type implements IParticleTimelineType<UndeadMageTimeline> {
        public MapCodec<UndeadMageTimeline> codec() { return UndeadMageTimeline.CODEC; }
        public StreamCodec<RegistryFriendlyByteBuf, UndeadMageTimeline> streamCodec() { return UndeadMageTimeline.STREAM; }
        public UndeadMageTimeline create() { return new UndeadMageTimeline(); }
        public AbstractSpellPart getSpellPart() { return EffectSummonUndead_boss.INSTANCE; }
    }
}
