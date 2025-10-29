package com.adamsmods.adamsarsplus.block;

import com.adamsmods.adamsarsplus.block.tile.AutoTurretTile;
import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.TileCaster;
import com.hollingsworth.arsnouveau.api.util.CasterUtil;
import com.hollingsworth.arsnouveau.api.util.SourceUtil;
import com.hollingsworth.arsnouveau.common.block.RotatingSpellTurret;
import com.hollingsworth.arsnouveau.common.block.tile.BasicSpellTurretTile;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.block.tile.TimerSpellTurretTile;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.items.WarpScroll;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketOneShotAnimation;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

import static net.minecraft.world.item.Items.COMMAND_BLOCK;

public class AutoSpellTurret extends RotatingSpellTurret {
    public static HashMap<AbstractCastMethod, ITurretBehavior> ROT_TURRET_BEHAVIOR_MAP = new HashMap();

    public AutoSpellTurret() {
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AutoTurretTile(pos, state);
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {

        ItemStack stack = player.getItemInHand(handIn);
        if (handIn == InteractionHand.MAIN_HAND) {
            if (stack.getItem() instanceof ICasterTool || worldIn.isClientSide) {
                if (handIn != InteractionHand.MAIN_HAND) {
                    return InteractionResult.PASS;
                } else if (worldIn.isClientSide) {
                    return InteractionResult.SUCCESS;
                } else {
                    Spell spell = CasterUtil.getCaster(stack).getSpell();
                    if (!spell.isEmpty()) {
                        if (spell.getCastMethod() == null) {
                            PortUtil.sendMessage(player, Component.translatable("ars_nouveau.alert.turret_needs_form"));
                            return InteractionResult.SUCCESS;
                        }

                        if (!TURRET_BEHAVIOR_MAP.containsKey(spell.getCastMethod())) {
                            PortUtil.sendMessage(player, Component.translatable("ars_nouveau.alert.turret_type"));
                            return InteractionResult.SUCCESS;
                        }

                        BlockEntity var10 = worldIn.getBlockEntity(pos);
                        if (var10 instanceof BasicSpellTurretTile) {
                            BasicSpellTurretTile tile = (BasicSpellTurretTile)var10;
                            tile.spellCaster.copyFromCaster(CasterUtil.getCaster(stack));
                            tile.spellCaster.setSpell(spell.clone());
                            tile.updateBlock();
                            PortUtil.sendMessage(player, Component.translatable("ars_nouveau.alert.spell_set"));
                            worldIn.sendBlockUpdated(pos, state, state, 2);
                        }
                    }

                    return InteractionResult.PASS;
                }
            }

            BlockEntity var9 = worldIn.getBlockEntity(pos);
            if (var9 instanceof AutoTurretTile) {
                AutoTurretTile autoSpellTurretTile = (AutoTurretTile) var9;

                if(player.isShiftKeyDown() && !autoSpellTurretTile.creative){
                    if(autoSpellTurretTile.owner == null){
                        autoSpellTurretTile.owner = player;
                        PortUtil.sendMessage(player, Component.literal("Set you as turret owner."));
                        return InteractionResult.SUCCESS;
                    } else {
                        if(player == autoSpellTurretTile.owner){
                            autoSpellTurretTile.owner = null;
                            PortUtil.sendMessage(player, Component.literal("Turret owner removed."));
                            return InteractionResult.SUCCESS;
                        }
                    }
                } else if(stack == COMMAND_BLOCK.getDefaultInstance()){
                    if(autoSpellTurretTile.creative){
                        autoSpellTurretTile.creative = false;
                        PortUtil.sendMessage(player, Component.literal("Turret set as Survival Mode."));
                        return InteractionResult.SUCCESS;
                    } else {
                        autoSpellTurretTile.creative = true;
                        PortUtil.sendMessage(player, Component.literal("Turret set as Creative Mode."));
                        return InteractionResult.SUCCESS;
                    }
                } else if(!autoSpellTurretTile.creative && (autoSpellTurretTile.owner == null || autoSpellTurretTile.owner == player)){
                    autoSpellTurretTile.mode++;
                    if(autoSpellTurretTile.mode >= 4){
                        autoSpellTurretTile.mode = 0;
                    }
                    autoSpellTurretTile.target = null;

                    if(autoSpellTurretTile.mode == 0){
                        PortUtil.sendMessage(player, Component.literal("Targeting: Players"));
                    } else if(autoSpellTurretTile.mode == 1){
                        PortUtil.sendMessage(player, Component.literal("Targeting: Non-owner"));
                    } else if(autoSpellTurretTile.mode == 2){
                        PortUtil.sendMessage(player, Component.literal("Targeting: Non-Players"));
                    } else {
                        PortUtil.sendMessage(player, Component.literal("Targeting: Monsters"));
                    }

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    public void shootSpell(ServerLevel world, BlockPos pos) {
        BlockEntity block = world.getBlockEntity(pos);
        if (block instanceof AutoTurretTile tile) {
            ISpellCaster caster = tile.getSpellCaster();
            if (!caster.getSpell().isEmpty()) {
                int manaCost = tile.getManaCost();
                if (manaCost <= 0 || SourceUtil.takeSourceWithParticles(pos, world, 10, manaCost) != null) {
                    Networking.sendToNearby(world, pos, new PacketOneShotAnimation(pos));
                    Position iposition = getDispensePosition(new BlockSourceImpl(world, pos), tile);
                    FakePlayer fakePlayer = ANFakePlayer.getPlayer(world);
                    fakePlayer.setPos((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
                    EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(world, caster.getSpell(), fakePlayer, new TileCaster(tile, SpellContext.CasterType.TURRET)));
                    if (resolver.castType != null && ROT_TURRET_BEHAVIOR_MAP.containsKey(resolver.castType)) {
                        ((ITurretBehavior)ROT_TURRET_BEHAVIOR_MAP.get(resolver.castType)).onCast(resolver, world, pos, fakePlayer, iposition, orderedByNearest(tile)[0].getOpposite());
                        caster.playSound(pos, world, (Entity)null, caster.getCurrentSound(), SoundSource.BLOCKS);
                    }

                }
            }
        }
    }

    public static Position getDispensePosition(BlockSource coords, AutoTurretTile tile) {
        Vec3 direction = tile.getShootAngle().normalize();
        double d0 = coords.x() + (double)0.5F * direction.x();
        double d1 = coords.y() + (double)0.5F * direction.y();
        double d2 = coords.z() + (double)0.5F * direction.z();
        return new PositionImpl(d0, d1, d2);
    }

    public static Direction[] orderedByNearest(AutoTurretTile pEntity) {
        float f = pEntity.getRotationY() * (float)Math.PI / 180.0F;
        float f1 = (90.0F + pEntity.getRotationX()) * (float)Math.PI / 180.0F;
        float f2 = Mth.sin(f);
        float f3 = Mth.cos(f);
        float f4 = Mth.sin(f1);
        float f5 = Mth.cos(f1);
        boolean flag = f4 > 0.0F;
        boolean flag1 = f2 < 0.0F;
        boolean flag2 = f5 > 0.0F;
        float f6 = flag ? f4 : -f4;
        float f7 = flag1 ? -f2 : f2;
        float f8 = flag2 ? f5 : -f5;
        float f9 = f6 * f3;
        float f10 = f8 * f3;
        Direction direction = flag ? Direction.EAST : Direction.WEST;
        Direction direction1 = flag1 ? Direction.UP : Direction.DOWN;
        Direction direction2 = flag2 ? Direction.SOUTH : Direction.NORTH;
        if (f6 > f8) {
            if (f7 > f9) {
                return makeDirectionArray(direction1, direction, direction2);
            } else {
                return f10 > f7 ? makeDirectionArray(direction, direction2, direction1) : makeDirectionArray(direction, direction1, direction2);
            }
        } else if (f7 > f10) {
            return makeDirectionArray(direction1, direction2, direction);
        } else {
            return f9 > f7 ? makeDirectionArray(direction2, direction, direction1) : makeDirectionArray(direction2, direction1, direction);
        }
    }

    static Direction[] makeDirectionArray(Direction pFirst, Direction pSecond, Direction pThird) {
        return new Direction[]{pFirst, pSecond, pThird, pThird.getOpposite(), pSecond.getOpposite(), pFirst.getOpposite()};
    }

    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction orientation = placer == null ? Direction.WEST : Direction.orderedByNearest(placer)[0].getOpposite();
        BlockEntity var8 = world.getBlockEntity(pos);
        if (var8 instanceof AutoTurretTile) {
            AutoTurretTile turretTile = (AutoTurretTile) var8;
            switch (orientation) {
                case DOWN:
                    turretTile.rotationY = -90.0F;
                    break;
                case UP:
                    turretTile.rotationY = 90.0F;
                    break;
                case NORTH:
                    turretTile.rotationX = 270.0F;
                    break;
                case SOUTH:
                    turretTile.rotationX = 90.0F;
                case WEST:
                default:
                    break;
                case EAST:
                    turretTile.rotationX = 180.0F;
            }

        }
    }

    static {
        ROT_TURRET_BEHAVIOR_MAP.put(MethodProjectile.INSTANCE, new ITurretBehavior() {
            public void onCast(SpellResolver resolver, ServerLevel world, BlockPos pos, Player fakePlayer, Position iposition, Direction direction) {
                BlockEntity block = world.getBlockEntity(pos);
                if (block instanceof AutoTurretTile rotatingTurretTile) {
                    EntityProjectileSpell spell = new EntityProjectileSpell(world, resolver);
                    spell.setOwner(fakePlayer);
                    spell.setPos(iposition.x(), iposition.y(), iposition.z());
                    Vec3 vec3d = rotatingTurretTile.getShootAngle().normalize();
                    spell.shoot(vec3d.x(), vec3d.y(), vec3d.z(), 0.5F, 0.0F);
                    world.addFreshEntity(spell);
                }

            }
        });
        ROT_TURRET_BEHAVIOR_MAP.put(MethodTouch.INSTANCE, new ITurretBehavior() {
            public void onCast(SpellResolver resolver, ServerLevel serverLevel, BlockPos pos, Player fakePlayer, Position dispensePosition, Direction facingDir) {
                BlockPos touchPos = pos.relative(facingDir);
                BlockEntity aimVec = serverLevel.getBlockEntity(pos);
                if (aimVec instanceof AutoTurretTile rotatingTurretTile) {
                    Vec3 var12 = rotatingTurretTile.getShootAngle().add(rotatingTurretTile.getX() + (double)0.5F, rotatingTurretTile.getY() + (double)0.5F, rotatingTurretTile.getZ() + (double)0.5F);
                    List<LivingEntity> entityList = serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(touchPos));
                    if (!entityList.isEmpty()) {
                        LivingEntity entity = (LivingEntity)entityList.get(serverLevel.random.nextInt(entityList.size()));
                        resolver.onCastOnEntity(ItemStack.EMPTY, entity, InteractionHand.MAIN_HAND);
                    } else {
                        resolver.onCastOnBlock(new BlockHitResult(var12, facingDir, BlockPos.containing(var12.x(), var12.y(), var12.z()), true));
                    }

                }
            }
        });
    }
}
