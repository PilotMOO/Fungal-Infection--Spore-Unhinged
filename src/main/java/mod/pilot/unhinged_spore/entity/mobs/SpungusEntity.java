package mod.pilot.unhinged_spore.entity.mobs;

import com.Harbinger.Spore.Core.SConfig;
import com.Harbinger.Spore.Sentities.AI.HurtTargetGoal;
import com.Harbinger.Spore.Sentities.BaseEntities.EvolvedInfected;
import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.pilot.unhinged_spore.entity.AI.SpungusEndlessHungerGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class SpungusEntity extends EvolvedInfected implements GeoEntity {
    public SpungusEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static final EntityDataAccessor<Integer> SpungusHunger = SynchedEntityData.defineId(SpungusEntity.class, EntityDataSerializers.INT);
    public int getSpungusHunger(){
        return entityData.get(SpungusHunger);
    }
    public void setSpungusHunger(int newHunger){
        entityData.set(SpungusHunger, newHunger);
    }
    public void Yummers(int count){
        setSpungusHunger(getSpungusHunger() + count);

        AttributeInstance mHealth = getAttribute(Attributes.MAX_HEALTH);
        if (mHealth == null) return;
        for (int i = 0; i < count; i++){
            mHealth.setBaseValue(getMaxHealth() + 1);
            heal(1);
        }
        int duration = 100;
        int amp = 1;
        MobEffectInstance instance = getEffect(MobEffects.REGENERATION);
        if (instance != null) {
            duration += instance.getDuration();
            amp = Math.max(amp, instance.getAmplifier());
        }
        addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration, amp));
    }
    public void Yummers(){
        Yummers(1);
    }

    public static final EntityDataAccessor<Integer> SpungusRAGE = SynchedEntityData.defineId(SpungusEntity.class, EntityDataSerializers.INT);
    public int getSpungusRAGE(){
        return entityData.get(SpungusRAGE);
    }
    public void setSpungusRAGE(int newHunger){
        entityData.set(SpungusRAGE, newHunger);
    }
    public boolean isRaging(){
        return getSpungusRAGE() > 0;
    }

    public boolean isMoving(){
        Vec3 delta = getDeltaMovement();
        return delta.x != 0 || delta.z != 0;
    }


    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SpungusHunger", getSpungusHunger());
        tag.putInt("SpungusRAGE", getSpungusRAGE());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSpungusHunger(tag.getInt("SpungusHunger"));
        setSpungusRAGE(tag.getInt("SpungusRAGE"));
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(SpungusHunger, 0);
        entityData.define(SpungusRAGE, 0);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "SpungusManager", event -> {
            if (isRaging()){
                return event.setAndContinue(RawAnimation.begin().thenLoop("rage"));
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
        return SpungusEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40D)
                .add(Attributes.ARMOR, 8)
                .add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 8D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2D)
                .add(Attributes.ATTACK_SPEED, 4D);
    }

    @Override
    protected void addTargettingGoals() {
        this.goalSelector.addGoal(2, (new HurtTargetGoal(this, (livingEntity) -> {
            return this.TARGET_SELECTOR.test(livingEntity);
        }, Infected.class)).setAlertOthers(Infected.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, (livingEntity) -> {
            return livingEntity instanceof Player || SConfig.SERVER.whitelist.get().contains(livingEntity.getEncodeId());
        }));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, (livingEntity) -> {
            return SConfig.SERVER.at_mob.get() && (this.TARGET_SELECTOR.test(livingEntity) || isRaging());
        }));

        this.targetSelector.addGoal(1, new MeleeAttackGoal(this, 1, true));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, JortEntity.class, false));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, (LE) -> isRaging()));
    }
    @Override
    protected void addRegularGoals() {
        super.addRegularGoals();
        this.goalSelector.addGoal(2, new SpungusEndlessHungerGoal(this));
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
    public boolean doHurtTarget(Entity entity) {
        boolean flag = super.doHurtTarget(entity);
        if (flag) entity.setDeltaMovement(entity.getDeltaMovement().add(0, isRaging() ? 0.5 : 1, 0));
        if (isRaging()) {
            entity.invulnerableTime = 0;
            if (entity instanceof Mob m){
                m.setTarget(this);
            }
        }
        return flag;
    }

    @Override
    public void tick() {
        super.tick();
        if (isRaging()){
            addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 2));
            addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 2));

            if (tickCount % 40 == 0){
                BreakBlocksNearby();
            }
            if (level() instanceof ServerLevel s && tickCount % 20 == 0){
                Vec3 pos = position();
                s.sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.x, pos.y + getBbHeight() * 0.75, pos.z, random.nextInt(3, 6),
                        random.nextInt(-1, 1), random.nextInt(-1, 1), random.nextInt(-1, 1), 0.5);
            }

            if (getTarget() == null && getNavigation().isDone()){
                Vec3 newPos;
                int cycleTracker = 0;
                do{
                    newPos = DefaultRandomPos.getPos(this, 16, 2);
                    cycleTracker++;
                } while (newPos == null && cycleTracker < 10);

                if (newPos != null){
                    getNavigation().moveTo(newPos.x, newPos.y, newPos.z, 1);
                }
            }

            setSpungusRAGE(getSpungusRAGE() - 1);
        }
    }

    private void BreakBlocksNearby() {
        AABB breakBox = getBoundingBox().inflate(1.2).move(0, 1, 0);
        Level level = level();
        for (BlockPos pos : BlockPos.betweenClosed((int)breakBox.minX, (int)breakBox.minY, (int)breakBox.minZ,
                (int)breakBox.maxX, (int)breakBox.maxY, (int)breakBox.maxZ)){
            BlockState state = level.getBlockState(pos);
            if (state.isAir() || state.getDestroySpeed(level, pos) == -1) continue;
            level.removeBlock(pos, false);
            level.levelEvent(2001, pos, Block.getId(level.getBlockState(pos)));
            level.playSound(null, pos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.5f, 1.25f);
        }
    }
}
