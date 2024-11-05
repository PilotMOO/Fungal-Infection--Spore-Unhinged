package mod.pilot.unhinged_spore.entity.AI;

import com.Harbinger.Spore.Core.Sentities;
import com.Harbinger.Spore.Sentities.FallenMultipart.HowitzerArm;
import mod.pilot.unhinged_spore.Config;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;

@Deprecated
public class DEPRECATEDHowitzerArmCopyGoal extends Goal {
    private final HowitzerArm arm;
    private final int maxSplit;
    private int splitTracker = 0;
    private final boolean shouldCopy = Config.SERVER.arm_split_max.get() != 0;
    private final int maxCopies = Config.SERVER.arm_split_max.get();
    private int copyCount = 0;
    public DEPRECATEDHowitzerArmCopyGoal(HowitzerArm arm, int splitTimer){
        this.arm = arm;
        this.maxSplit = splitTimer;
    }
    @Override
    public boolean canUse() {
        return shouldCopy && copyCount < maxCopies;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return shouldCopy;
    }

    @Override
    public void tick() {
        splitTracker++;
        if (splitTracker >= maxSplit) Split();
    }

    private void Split() {
        HowitzerArm newArm = Sentities.HOWIT_ARM.get().create(arm.level());

        newArm.copyPosition(arm);
        newArm.setRight(arm.getRight());
        newArm.goalSelector.addGoal(1, new DEPRECATEDHowitzerArmBossfightGoal(newArm, Config.SERVER.arm_throw_speed.get(),
                Config.SERVER.arm_min_blocks.get(), Config.SERVER.arm_max_blocks.get(), 16));

        RandomSource random = arm.getRandom();
        newArm.setDeltaMovement(random.nextGaussian() * (random.nextBoolean() ? -1 : 1),
                1,
                random.nextGaussian() * (random.nextBoolean() ? -1 : 1));

        arm.level().addFreshEntity(newArm);
        arm.level().playSound(null, arm, SoundEvents.ITEM_PICKUP, SoundSource.HOSTILE, 1, 1.5f);

        splitTracker = 0;
        copyCount++;
    }
}
