package adamsmods.adamsarsplus.util;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public interface IPropagator {

    AbstractAugment DUMMY = new AbstractAugment("dummy", "Dummy") {
        @Override
        public int getDefaultManaCost() {
            return 0;
        }
    };

    void propagate(Level world, HitResult hitResult, LivingEntity shooter, SpellStats stats, SpellResolver resolver);

}
