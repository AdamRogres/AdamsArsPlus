package adamsmods.adamsarsplus.common.entity.example.ai;

import adamsmods.adamsarsplus.common.entity.example.mages.EntityMageBase;

import java.util.function.Supplier;

public class ComboCastGoal<T extends EntityMageBase> extends CastGoal<T> {

    public ComboCastGoal(T entity, double speed, int attackInterval, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
        super(entity, speed, attackInterval, attackRange, canUse, animId, delayTicks);
    }

}
