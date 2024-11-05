package mod.pilot.unhinged_spore.entity.AI;

import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.BaseEntities.UtilityEntity;
import com.Harbinger.Spore.Sentities.FallenMultipart.HowitzerArm;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@Deprecated
public class DEPRECATEDHowitzerArmBossfightGoal extends Goal {
    private final HowitzerArm arm;
    private final int blockThrowCDMax;
    private int blockThrowTicker = 0;

    private final int minBlocksPicked;
    private final int maxBlocksPicked;

    private final int blockSearchRange;

    private final ArrayList<FallingBlockEntity> fallingBlocks = new ArrayList<>();
    public DEPRECATEDHowitzerArmBossfightGoal(HowitzerArm arm, int throwCD, int defaultMinBlocks, int defaultMaxBlocks, int blockSearchRange){
        this.arm = arm;
        this.blockThrowCDMax = arm.getRight() ? throwCD : throwCD / 2;
        this.minBlocksPicked = arm.getRight() ? defaultMinBlocks * 2 : defaultMinBlocks;
        this.maxBlocksPicked = arm.getRight() ? defaultMaxBlocks * 2 : defaultMaxBlocks;
        this.blockSearchRange = blockSearchRange;
    }
    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = arm.getTarget();
        if (target == null){
            FindTarget();
            return;
        }

        blockThrowTicker++;
        if (blockThrowTicker >= blockThrowCDMax && arm.level() instanceof ServerLevel server){
            for (BlockPos bPos : getNearbyRandomBlocks()){
                FallingBlockEntity falling = FallingBlockEntity.fall(server, bPos, blockStateOf(bPos));
                falling.setNoGravity(true);
                falling.setDeltaMovement(0, 0.25, 0);

                arm.level().playSound(null, bPos, blockStateOf(bPos).getSoundType().getBreakSound(), SoundSource.BLOCKS);
                fallingBlocks.add(falling);
            }
            blockThrowTicker = 0;
            return;
        }

        if (fallingBlocks.size() == 0) return;
        ArrayList<FallingBlockEntity> toRemove = new ArrayList<>();
        for (FallingBlockEntity fBlock : fallingBlocks){
            if (fBlock.isRemoved()){
                toRemove.add(fBlock);
                continue;
            }
            if (fBlock.getDeltaMovement().y <= 0.1){
                ThrowBlockAtTarget(fBlock, arm.getTarget());
                toRemove.add(fBlock);
            }
        }
        fallingBlocks.removeAll(toRemove);
    }

    private void ThrowBlockAtTarget(FallingBlockEntity fBlock, LivingEntity target) {
        Vec3 directionTo = target.position().subtract(fBlock.position()).normalize();
        fBlock.setDeltaMovement(directionTo.scale(1));
        fBlock.setHurtsEntities(1, 30);
    }

    private void FindTarget() {
        AABB search = arm.getBoundingBox().inflate(64);
        LivingEntity target = null;
        double distance = Double.MAX_VALUE;
        for (LivingEntity LE : arm.level().getEntitiesOfClass(LivingEntity.class, search,
                (LE) -> !(LE instanceof Infected) && !(LE instanceof UtilityEntity) && arm.hasLineOfSight(LE) && !(LE instanceof Player p && p.isCreative()))){
            if (target == null || LE.distanceTo(arm) < distance){
                target = LE;
                distance = LE.distanceTo(arm);
            }
        }
        arm.setTarget(target);
    }

    private ArrayList<BlockPos> getNearbyRandomBlocks() {
        ArrayList<BlockPos> toReturn = new ArrayList<>();
        int blockCount = arm.getRandom().nextInt(minBlocksPicked, maxBlocksPicked + 1);
        int cycleTracker = 0;
        do{
            cycleTracker++;
            BlockPos newPos = LocateNearbyValidBlock(arm.getRandom());
            if (newPos == null) continue;
            toReturn.add(newPos);
        } while (toReturn.size() < blockCount && cycleTracker < blockCount + 5);
        return toReturn;
    }
    private BlockPos LocateNearbyValidBlock(RandomSource random) {
        Vec3 pos = arm.blockPosition().getCenter().add(
                random.nextInt(-blockSearchRange / 2, blockSearchRange / 2),
                0,
                random.nextInt(-blockSearchRange / 2, blockSearchRange / 2));
        return getSurfaceBlockOf(new BlockPos((int)pos.x, (int)pos.y, (int)pos.z));
    }
    private @Nullable BlockPos getSurfaceBlockOf(BlockPos check){
        BlockPos bPos = check;
        BlockState bState;
        int cycleTracker = 0;
        while (!blockStateOf(bPos.above()).isAir() && cycleTracker < arm.level().getMaxBuildHeight()){
            bPos = bPos.above();
            cycleTracker++;
        }
        bState = blockStateOf(bPos);
        while (bState.isAir() && cycleTracker > arm.level().getMinBuildHeight()){
            bPos = bPos.below();
            bState = blockStateOf(bPos);
            cycleTracker--;
        }
        if (!blockStateOf(bPos.above()).isAir() || bState.isAir()) return null;
        return bPos;
    }

    private BlockState blockStateOf(BlockPos bPos){
        return arm.level().getBlockState(bPos);
    }
}
