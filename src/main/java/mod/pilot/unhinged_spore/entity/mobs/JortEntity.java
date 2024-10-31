package mod.pilot.unhinged_spore.entity.mobs;

import com.Harbinger.Spore.Sentities.BaseEntities.EvolvedInfected;
import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.pilot.unhinged_spore.sound.UnhingedSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class JortEntity extends EvolvedInfected implements GeoEntity {
    public JortEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public boolean isMoving(){
        Vec3 delta = getDeltaMovement();
        return delta.x != 0 || delta.z != 0;
    }

    public static final EntityDataAccessor<Boolean> Boom = SynchedEntityData.defineId(JortEntity.class, EntityDataSerializers.BOOLEAN);
    public boolean getBoom(){return entityData.get(Boom);}
    public void setBoom(boolean flag) {
        entityData.set(Boom, flag);
    }
    public static final EntityDataAccessor<Integer> BoomTicker = SynchedEntityData.defineId(JortEntity.class, EntityDataSerializers.INT);
    public int getBoomTicker(){return entityData.get(BoomTicker);}
    public void setBoomTicker(int count) {
        entityData.set(BoomTicker, count);
    }
    private final int maxBoomTicker = 20;

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Boom", getBoom());
        tag.putInt("BoomTicker", getBoomTicker());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setBoom(tag.getBoolean("Boom"));
        setBoomTicker(tag.getInt("BoomTicker"));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(Boom, false);
        entityData.define(BoomTicker, 0);
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "JortManager", event -> {
            if (getBoom()){
                return event.setAndContinue(RawAnimation.begin().thenLoop("chaos"));
            }
            else if (isMoving()){
                return event.setAndContinue(RawAnimation.begin().thenLoop("walk"));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));
    }

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static AttributeSupplier.Builder createAttributes(){
        return JortEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.ARMOR, 8)
                .add(Attributes.FOLLOW_RANGE, 8)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0D)
                .add(Attributes.ATTACK_SPEED, 2D);
    }

    @Override
    protected void addTargettingGoals() {}
    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new RandomStrollGoal(this, 1));
        goalSelector.addGoal(1, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean canStarve() {
        return false;
    }
    @Override
    public boolean removeWhenFarAway(double p_21542_) {
        return false;
    }
    @Override
    public boolean isPersistenceRequired() {
        return true;
    }


    @Override
    public void push(double pX, double pY, double pZ) {
        super.push(pX, pY, pZ);
        setBoom(true);
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        setBoom(true);
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        if (getBoom()){
            if (getBoomTicker() == 0){
                level().playSound(null, this, UnhingedSounds.JORT_SCREAM.get(), SoundSource.HOSTILE, 1f, 1);
            }
            setBoomTicker(getBoomTicker() + 1);
            if (getBoomTicker() >= maxBoomTicker){
                JortBoom();
            }
        }
    }
    public void JortBoom(){
        level().explode(this, null, null, position(), 20, true, Level.ExplosionInteraction.MOB);
        this.discard();
    }
}
