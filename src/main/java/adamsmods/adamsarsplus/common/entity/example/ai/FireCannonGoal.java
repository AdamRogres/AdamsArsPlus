package adamsmods.adamsarsplus.common.entity.example.ai;

import adamsmods.adamsarsplus.common.entity.example.FirenandoEntity;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

import static adamsmods.adamsarsplus.common.entity.example.FirenandoEntity.SHOOTING;

public class FireCannonGoal extends ProjCastingGoal<FirenandoEntity> {


    public FireCannonGoal(FirenandoEntity entity, double speed, int attackInterval, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
        super(entity, speed, attackInterval, attackRange, canUse, animId, delayTicks);
    }

    public void stop() {
        super.stop();
        mob.getEntityData().set(SHOOTING, false);
    }

    @Override
    void animationChecks(LivingEntity target) {

        if (this.seeTime >= 40 && !this.hasAnimated) {
            this.hasAnimated = true;
            mob.getEntityData().set(SHOOTING, true);
            Networking.sendToNearbyClient(mob.level, mob, new PacketAnimEntity(mob.getId(), animId));
        }

        if (this.hasAnimated) {
            animatedTicks++;
            if (animatedTicks == delayTicks / 2) {
                mob.performRangedAttack(target, 1);
            } else if (animatedTicks >= delayTicks) {
                this.done = true;
            }
        }
    }

}