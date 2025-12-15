package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.datagen.CommunityMages;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;

import static com.adamsmods.adamsarsplus.Config.COM_MAGES;
import static com.adamsmods.adamsarsplus.Config.MAGES_GRIEF;
import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.EARTH_MAGE_ENTITY;

public class EarthMageEntity extends MysteriousMageEntity implements RangedAttackMob {

    public EarthMageEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);
    }

    public EarthMageEntity(Level pLevel){
        this(EARTH_MAGE_ENTITY.get(), pLevel);
    }

    @Override
    public void tick() {
        super.tick();

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40D)
                .add(Attributes.ARMOR, 4D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.2F)
                .add(Attributes.FOLLOW_RANGE, (double)40.0F)
                .add(Attributes.ATTACK_DAMAGE, 8D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0F);
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float p_82196_2_) {

    }

    public void mageSpawn() {
        RandomSource randomSource = this.level().getRandom();

        if (!CommunityMages.mages.isEmpty()) {
            try {
                int size = 1;
                int Offset = 0;

                if(COM_MAGES.get()){
                    this.setIndex(randomSource.nextInt(CommunityMages.mages.size()));
                    size = CommunityMages.mages.size();
                } else {
                    this.setIndex(10);
                }

                for(int i = 0; i < size; i++){
                    if(i + this.getIndex() < size){
                        if(CommunityMages.mages.get(i + this.getIndex()).tier.contains("earth")){
                            if(MAGES_GRIEF.get() || !CommunityMages.mages.get(i + this.getIndex()).tier.contains("griefing")){
                                Offset = i;
                                break;
                            }
                        }
                    } else {
                        if(CommunityMages.mages.get(i + this.getIndex() - size).tier.contains("earth")){
                            if(MAGES_GRIEF.get() || !CommunityMages.mages.get(i + this.getIndex()).tier.contains("griefing")){
                                Offset = i;
                                break;
                            }
                        }
                    }
                }

                CommunityMages.ComMages communityMage = (CommunityMages.ComMages)CommunityMages.mages.get(this.getIndex() + Offset);

                this.setColor(communityMage.color);
                this.setName(communityMage.name);
                this.setCooldown(communityMage.coold);
                this.setSpellData(communityMage.spell, communityMage.color);
                this.type = communityMage.type;
                this.tier = communityMage.tier;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.setColor("white");
            this.setName("Oops");
        }
    }
}
