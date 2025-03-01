package mod.pilot.unhinged_spore.mixins.common;

import com.Harbinger.Spore.Sentities.BaseEntities.FallenMultipartEntity;
import com.Harbinger.Spore.Sentities.FallenMultipart.HowitzerArm;
import mod.pilot.unhinged_spore.Config;
import mod.pilot.unhinged_spore.entity.AI.HowitzerArmHopControl;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HowitzerArm.class)
public abstract class HowitzerArmBossfightMixin extends FallenMultipartEntity {
    public HowitzerArmBossfightMixin(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Unique private static final boolean sporeWithBullshit$bossfight = Config.SERVER.arm_bossfight.get();
    @Unique private static final int sporeWithBullshit$jumpRate = Config.SERVER.arm_jump_speed.get();
    @Unique private static final double sporeWithBullshit$jumpStrength = Config.SERVER.arm_jump_strength.get();
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void ConstructorInjectMoveControl(EntityType<? extends PathfinderMob> type, Level level, CallbackInfo ci){
        if (sporeWithBullshit$bossfight) moveControl = new HowitzerArmHopControl(this, sporeWithBullshit$jumpRate,
                (float)sporeWithBullshit$jumpStrength);
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource) {
        return super.causeFallDamage(pFallDistance, 0, pSource);
    }
}
