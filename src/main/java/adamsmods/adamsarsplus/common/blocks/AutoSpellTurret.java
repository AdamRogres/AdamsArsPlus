package adamsmods.adamsarsplus.common.blocks;

import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.registry.SpellCasterRegistry;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.block.RotatingSpellTurret;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

import static net.minecraft.world.item.Items.COMMAND_BLOCK;

public class AutoSpellTurret extends RotatingSpellTurret {
    public static HashMap<AbstractCastMethod, ITurretBehavior> ROT_TURRET_BEHAVIOR_MAP = new HashMap();

    public AutoSpellTurret() {
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new AutoTurretTile(pos, state);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult pHitResult) {

        ItemStack stack = player.getItemInHand(handIn);
        if (handIn == InteractionHand.MAIN_HAND) {
            if (stack.getItem() instanceof ICasterTool || worldIn.isClientSide) {
                if (handIn != InteractionHand.MAIN_HAND) {
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                } else if (worldIn.isClientSide) {
                    return ItemInteractionResult.SUCCESS;
                } else {
                    if (SpellCasterRegistry.from(stack) != null) {
                        Spell spell = SpellCasterRegistry.from(stack).getSpell();
                        if (!spell.isEmpty()) {
                            if (spell.getCastMethod() == null) {
                                PortUtil.sendMessage(player, Component.translatable("ars_nouveau.alert.turret_needs_form"));
                                return ItemInteractionResult.SUCCESS;
                            }

                            if (!TURRET_BEHAVIOR_MAP.containsKey(spell.getCastMethod())) {
                                PortUtil.sendMessage(player, Component.translatable("ars_nouveau.alert.turret_type"));
                                return ItemInteractionResult.SUCCESS;
                            }

                            BlockEntity var10 = worldIn.getBlockEntity(pos);
                            if (var10 instanceof AutoTurretTile) {
                                AutoTurretTile tile = (AutoTurretTile)var10;
                                if(!tile.creative){
                                    tile.setSpell(spell);
                                    tile.setPlayer(player.getUUID());
                                    tile.updateBlock();
                                    PortUtil.sendMessage(player, Component.translatable("ars_nouveau.alert.spell_set"));
                                    worldIn.sendBlockUpdated(pos, state, state, 2);
                                }
                            }
                        }
                    }
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                }
            }

            BlockEntity var9 = worldIn.getBlockEntity(pos);
            if (var9 instanceof AutoTurretTile) {
                AutoTurretTile autoSpellTurretTile = (AutoTurretTile) var9;

                if(stack.getItem() == COMMAND_BLOCK.asItem()){
                    if(autoSpellTurretTile.creative){
                        autoSpellTurretTile.creative = false;
                        PortUtil.sendMessage(player, Component.literal("Turret set as Survival Mode."));
                        return ItemInteractionResult.SUCCESS;
                    } else {
                        autoSpellTurretTile.creative = true;
                        PortUtil.sendMessage(player, Component.literal("Turret set as Creative Mode."));
                        return ItemInteractionResult.SUCCESS;
                    }
                } else if(!autoSpellTurretTile.creative){
                    autoSpellTurretTile.mode++;
                    if(autoSpellTurretTile.mode >= 3){
                        autoSpellTurretTile.mode = 0;
                    }
                    autoSpellTurretTile.target = null;

                    if(autoSpellTurretTile.mode == 0){
                        PortUtil.sendMessage(player, Component.literal("Targeting: Players"));
                    } else if(autoSpellTurretTile.mode == 1){
                        PortUtil.sendMessage(player, Component.literal("Targeting: Non-Players"));
                    } else if(autoSpellTurretTile.mode == 2){
                        PortUtil.sendMessage(player, Component.literal("Targeting: Monsters"));
                    }

                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(pStack, state, worldIn, pos, player, handIn, pHitResult);
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
                if (world.getBlockEntity(pos) instanceof AutoTurretTile rotatingTurretTile) {
                    EntityProjectileSpell spell = new EntityProjectileSpell(world, resolver);
                    spell.setOwner(fakePlayer);
                    spell.setPos(iposition.x(), iposition.y() - 0.25, iposition.z());
                    Vec3 vec3d = rotatingTurretTile.getShootAngle().normalize();
                    SpellStats stats = resolver.getCastStats();
                    float velocity = Math.max(0.1f, 0.75f + stats.getAccMultiplier() / 2);
                    spell.shoot(vec3d.x(), vec3d.y(), vec3d.z(), velocity, 0);
                    world.addFreshEntity(spell);
                }

            }
        });
        ROT_TURRET_BEHAVIOR_MAP.put(MethodTouch.INSTANCE, new ITurretBehavior() {
            public void onCast(SpellResolver resolver, ServerLevel serverLevel, BlockPos pos, Player fakePlayer, Position dispensePosition, Direction facingDir) {
                BlockPos touchPos = pos.relative(facingDir);
                if (!(serverLevel.getBlockEntity(pos) instanceof AutoTurretTile rotatingTurretTile)) {
                    return;
                }
                Vec3 aimVec = rotatingTurretTile.getShootAngle().add(rotatingTurretTile.getX() + 0.5, rotatingTurretTile.getY() + 0.5, rotatingTurretTile.getZ() + 0.5);
                List<LivingEntity> entityList = serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(touchPos));
                if (!entityList.isEmpty()) {
                    LivingEntity entity = entityList.get(serverLevel.random.nextInt(entityList.size()));
                    resolver.onCastOnEntity(ItemStack.EMPTY, entity, InteractionHand.MAIN_HAND);
                } else {
                    resolver.onCastOnBlock(new BlockHitResult(aimVec, facingDir, BlockPos.containing(aimVec.x(), aimVec.y(), aimVec.z()), true));
                }
            }
        });
    }
}
