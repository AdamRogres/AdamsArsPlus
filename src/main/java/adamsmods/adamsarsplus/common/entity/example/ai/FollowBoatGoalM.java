package adamsmods.adamsarsplus.common.entity.example.ai;

import adamsmods.adamsarsplus.common.entity.example.MermaidEntity;
import net.minecraft.world.entity.ai.goal.FollowBoatGoal;

import java.util.function.Supplier;

public class FollowBoatGoalM extends FollowBoatGoal {

    Supplier<Boolean> canUse;

    public FollowBoatGoalM(MermaidEntity pMob, Supplier<Boolean> canUse) {
        super(pMob);
        this.canUse = canUse;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && (canUse != null && canUse.get());
    }

}
