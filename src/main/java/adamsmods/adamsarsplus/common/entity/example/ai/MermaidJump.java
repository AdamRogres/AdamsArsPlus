package adamsmods.adamsarsplus.common.entity.example.ai;

import adamsmods.adamsarsplus.common.entity.example.MermaidEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public class MermaidJump<M extends MermaidEntity> extends Behavior<M> {

    public MermaidJump(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition, int pDuration) {
        super(pEntryCondition, pDuration);
    }


}
