package mod.pilot.unhinged_spore.entity.AI;

import com.Harbinger.Spore.Core.Ssounds;
import com.Harbinger.Spore.Damage.SdamageTypes;
import com.Harbinger.Spore.Sentities.FallenMultipart.HowitzerArm;
import mod.pilot.unhinged_spore.sound.UnhingedSounds;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class HowitzerArmHopControl extends MoveControl {
    private float yRot;
    private int jumpDelay;
    private final HowitzerArm arm;
    private final int HopDelay;
    private final float JumpStrength;

    private boolean wasOnGround;

    public HowitzerArmHopControl(PathfinderMob arm, int hopDelay, float jumpStrength) {
        super(arm);
        if (arm instanceof HowitzerArm arm1){
            this.arm = arm1;
            this.yRot = 180.0F * arm.getYRot() / (float)Math.PI;
            this.HopDelay = hopDelay;
            this.JumpStrength = jumpStrength;
        }
        else throw new RuntimeException("ERROR! Attempted to create a HowitzerArmHopControl for an entity which is NOT a HowitzerArm!");
    }

    public void setDirection(float pYRot) {
        this.yRot = pYRot;
    }

    public void setWantedMovement(double pSpeed) {
        this.speedModifier = pSpeed;
        this.operation = MoveControl.Operation.MOVE_TO;
    }

    public void tick() {
        if (arm.onGround() && !wasOnGround && arm.getTarget() != null){
            arm.playSound(Ssounds.LANDING.get(), (float)(arm.getRight() ? 0.75 : 1.25), (float) (arm.getRight() ? 1.5 : 0.75));
            for (LivingEntity e : arm.level().getEntitiesOfClass(LivingEntity.class, arm.getBoundingBox()
                            .inflate(arm.getRight() ? 1.5 : 3),
                    (e) -> arm.TARGET_SELECTOR.test(e))){
                switch (arm.getRandom().nextInt(1, 4)){
                    case 1 : e.hurt(SdamageTypes.calamity_damage1(arm),
                            arm.getRandom().nextInt(2, 5) * (arm.getRight() ? 1 : 3));
                    case 2 : e.hurt(SdamageTypes.calamity_damage2(arm),
                            arm.getRandom().nextInt(2, 5) * (arm.getRight() ? 1 : 3));
                    case 3 : e.hurt(SdamageTypes.calamity_damage3(arm),
                            arm.getRandom().nextInt(2, 5) * (arm.getRight() ? 1 : 3));
                }
            }
        }
        wasOnGround = arm.onGround();

        if (arm.onGround()){
            if (arm.getTarget() == null){
                arm.setDeltaMovement(Vec3.ZERO);
                this.operation = Operation.WAIT;
            }
            else{
                arm.lookAt(arm.getTarget(), 30, 30);
                setDirection(arm.getYRot());
                if (arm.getNavigation().isDone()) arm.getNavigation().moveTo(arm.getTarget(), 1);
            }
        }

        this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
        this.mob.yHeadRot = this.mob.getYRot();
        this.mob.yBodyRot = this.mob.getYRot();
        if (this.operation != MoveControl.Operation.MOVE_TO) {
            this.mob.setZza(0.0F);
        } else {
            this.operation = MoveControl.Operation.WAIT;
            if (this.mob.onGround()) {

                this.mob.setSpeed((float)(this.speedModifier * (arm.getRight() ? 0.9 : 1.25)));
                if (this.jumpDelay-- <= 0) {
                    this.jumpDelay = HopDelay;
                    if (arm.getRight()) {
                        this.jumpDelay /= 3;
                    }

                    Jump();
                } else {
                    this.arm.xxa = 0.0F;
                    this.arm.zza = 0.0F;
                    this.mob.setSpeed(0.0F);
                }
            } else {
                this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            }
        }
    }

    private void Jump(){
        Vec3 vec3 = arm.getDeltaMovement();
        arm.setDeltaMovement(vec3.x, arm.getRight() ? JumpStrength / 3 : JumpStrength, vec3.z);
        if (arm.isSprinting()) {
            float f = arm.getYRot() * ((float)Math.PI / 180F);
            arm.setDeltaMovement(arm.getDeltaMovement().add((double)(-Mth.sin(f) * 0.2F),
                    0.0D,
                    (double)(Mth.cos(f) * 0.2F))/*.scale(arm.getRight() ? 0.5 : 1.5)*/);
        }

        arm.hasImpulse = true;
        net.minecraftforge.common.ForgeHooks.onLivingJump(arm);
        this.arm.playSound(UnhingedSounds.BOING.get());
    }
}
