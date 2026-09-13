package adamsmods.adamsarsplus.common.particle;

import adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.particle.timelines.IParticleTimelineType;
import com.hollingsworth.arsnouveau.api.registry.ParticleTimelineRegistry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class ModDomainTimelines {
    public static final DeferredRegister<IParticleTimelineType<?>> REGISTER = DeferredRegister.create(
            ParticleTimelineRegistry.PARTICLE_TIMELINE_REGISTRY_KEY, AdamsArsPlus.MODID);
    public static final DeferredHolder<IParticleTimelineType<?>, DomainTimeline.Type> DOMAIN =
            REGISTER.register("domain", DomainTimeline.Type::new);
    public static final DeferredHolder<IParticleTimelineType<?>, UndeadMageTimeline.Type> UNDEAD_MAGE =
            REGISTER.register("undead_mage", UndeadMageTimeline.Type::new);
    private ModDomainTimelines() {}
}
