package adamsmods.adamsarsplus.event;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.BossConfig;
import adamsmods.adamsarsplus.ConfigHandler;
import com.hollingsworth.arsnouveau.api.event.SpellDamageEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public final class BossDifficultyEvents {
    private static final String APPLIED = "adamsarsplus_boss_config";

    private static BossConfig.Settings settings(LivingEntity entity) {
        if (!ConfigHandler.COMMON_SPEC.isLoaded()) return null;
        var id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        return id.getNamespace().equals(AdamsArsPlus.MODID) ? BossConfig.BOSSES.get(id.getPath()) : null;
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof LivingEntity boss)) return;
        var settings = settings(boss);
        if (settings == null) return;
        CompoundTag previous = boss.getPersistentData().getCompound(APPLIED);
        float oldMax = boss.getMaxHealth();
        float oldHealth = boss.getHealth();
        for (var option : BossConfig.ATTRIBUTES) {
            var config = settings.attributes().get(option.key());
            if (config == null) continue;
            double value = config.get();
            // Do not reset command/script base changes on each chunk load when the config is unchanged.
            if (previous.contains(option.key()) && Double.compare(previous.getDouble(option.key()), value) == 0) continue;
            var attribute = boss.getAttribute(option.attribute().get());
            if (attribute != null) {
                attribute.setBaseValue(value);
                previous.putDouble(option.key(), value);
            }
        }
        boss.getPersistentData().put(APPLIED, previous);
        if (oldMax != boss.getMaxHealth() && oldMax > 0) {
            boss.setHealth(Math.min(boss.getMaxHealth(), oldHealth / oldMax * boss.getMaxHealth()));
        }
    }

    @SubscribeEvent
    public static void onSpellDamage(SpellDamageEvent.Pre event) {
        if (event.caster == null || event.caster.level().isClientSide()) return;
        var settings = settings(event.caster);
        if (settings != null) event.damage *= settings.spellDamageMultiplier().get().floatValue();
    }
}
