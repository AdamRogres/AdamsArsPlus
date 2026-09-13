package adamsmods.adamsarsplus.common.particle;

import adamsmods.adamsarsplus.common.glyphs.effect_glyph.EffectDomain;
import com.hollingsworth.arsnouveau.api.particle.timelines.*;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.BaseProperty;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import java.util.List;

/** Independent Domain style, using the same editable properties as Linger. */
public class DomainTimeline extends BaseTimeline<DomainTimeline> {
    public final LingerTimeline settings;
    public static final MapCodec<DomainTimeline> CODEC = LingerTimeline.CODEC.xmap(DomainTimeline::new, value -> value.settings);
    public static final StreamCodec<RegistryFriendlyByteBuf, DomainTimeline> STREAM = LingerTimeline.STREAM_CODEC.map(DomainTimeline::new, value -> value.settings);

    public DomainTimeline() { this(new LingerTimeline()); }
    public DomainTimeline(LingerTimeline settings) { this.settings = settings; }

    @Override
    public IParticleTimelineType<DomainTimeline> getType() { return ModDomainTimelines.DOMAIN.get(); }

    @Override
    public List<BaseProperty<?>> getProperties() { return settings.getProperties(); }

    public static class Type implements IParticleTimelineType<DomainTimeline> {
        public MapCodec<DomainTimeline> codec() { return DomainTimeline.CODEC; }
        public StreamCodec<RegistryFriendlyByteBuf, DomainTimeline> streamCodec() { return DomainTimeline.STREAM; }
        public DomainTimeline create() { return new DomainTimeline(); }
        public AbstractSpellPart getSpellPart() { return EffectDomain.INSTANCE; }
    }
}
