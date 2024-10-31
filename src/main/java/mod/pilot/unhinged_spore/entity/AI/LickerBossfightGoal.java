package mod.pilot.unhinged_spore.entity.AI;

import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.BaseEntities.UtilityEntity;
import com.Harbinger.Spore.Sentities.FallenMultipart.Licker;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class LickerBossfightGoal extends Goal {
    private final Licker licker;
    private final int desiredYOffset;
    private int fireballCD;
    private final int fireballCDMax;
    private final ArrayList<Fireball> fireballs = new ArrayList<>();
    private boolean Crispy(){
        return licker.getBurned();
    }
    public LickerBossfightGoal(Licker licker, int wantedY, int fireballCD){
        this.licker = licker;
        this.desiredYOffset = wantedY;
        this.fireballCDMax = fireballCD;
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
        licker.resetFallDistance();
        ArrayList<Fireball> toRemove = new ArrayList<>();
        for (Fireball fireball : fireballs){
            if (fireball.isRemoved() || fireball.tickCount > 300){
                licker.level().explode(fireball, null, null, fireball.position(),
                        1, true, Level.ExplosionInteraction.BLOCK);
                fireball.discard();
                toRemove.add(fireball);
            }
        }
        fireballs.removeAll(toRemove);
        LivingEntity target = licker.getTarget();
        if (target == null){
            FindTarget();
            if (!licker.onGround()){
                licker.setDeltaMovement(0, -0.1 * (Crispy() ? 2 : 1), 0);
            }
            return;
        }
        else if (target.isDeadOrDying() || target.distanceTo(licker) > 128){
            licker.setTarget(null);
            return;
        }

        licker.lookAt(target, 10f, 10f);
        if ((licker.tickCount % 10) / (Crispy() ? 2 : 1) == 0) BreakBlocksNearby();

        if (licker.position().y < target.position().y + desiredYOffset){
            licker.setDeltaMovement(0, 0.1 * (Crispy() ? 2 : 1), 0);
        }
        else if (licker.position().y > target.position().y + desiredYOffset + 4){
            licker.setDeltaMovement(0, -0.1 * (Crispy() ? 2 : 1), 0);
        }
        else{
            licker.setDeltaMovement(licker.getDeltaMovement().multiply(1, 0, 1));
        }

        if (Math.sqrt(licker.distanceToSqr(target.position().add(0, desiredYOffset, 0))) > 10){
            licker.setDeltaMovement(licker.getDeltaMovement().multiply(0, 1, 0)
                    .add(target.position().subtract(licker.position()).normalize().multiply(1, 0, 1)
                    .scale(licker.getAttributeValue(Attributes.MOVEMENT_SPEED) * (Crispy() ? 0.75 : 0.5))));
        }
        else if (Math.sqrt(licker.distanceToSqr(target.position().add(0, desiredYOffset, 0))) < 8){
            licker.setDeltaMovement(licker.getDeltaMovement().multiply(0, 1, 0)
                    .add(target.position().subtract(licker.position()).normalize().multiply(1, 0, 1)
                    .scale(licker.getAttributeValue(Attributes.MOVEMENT_SPEED) * (Crispy() ? 0.75 : 0.5)).reverse()));
        }
        else{
            Orbit(target);
        }

        fireballCD = fireballCD >= fireballCDMax ? 0 : fireballCD + (licker.getBurned() ? 2 : 1);
        if (fireballCD == 0){
            FireFireball(target);
        }
    }

    private void Orbit(LivingEntity target) {
        licker.setDeltaMovement(licker.getDeltaMovement().multiply(0, 1, 0)
                .add(target.position().subtract(licker.position()).normalize().multiply(1, 0, 1)
                        .yRot(90)).scale(licker.getAttributeValue(Attributes.MOVEMENT_SPEED) * (Crispy() ? 0.75 : 0.5)));
    }

    private void FireFireball(LivingEntity target) {
        RandomSource random = licker.getRandom();
        LargeFireball fireball = EntityType.FIREBALL.create(licker.level());
        fireball.setPos(licker.position().add(licker.getForward()).add(random.nextDouble(), random.nextDouble(), random.nextDouble()));
        fireball.setDeltaMovement(target.position().subtract(licker.position()).normalize().scale((random.nextDouble() * 2) + (Crispy() ? 2 : 1.5)));
        fireball.setOwner(licker);
        fireballs.add(fireball);

        licker.level().addFreshEntity(fireball);
    }

    private void BreakBlocksNearby() {
        AABB breakBox = licker.getBoundingBox().inflate(1.2);
        Level level = licker.level();
        for (BlockPos pos : BlockPos.betweenClosed((int)breakBox.minX, (int)breakBox.minY, (int)breakBox.minZ,
                (int)breakBox.maxX, (int)breakBox.maxY, (int)breakBox.maxZ)){
            BlockState state = level.getBlockState(pos);
            if (state.isAir() || state.getDestroySpeed(level, pos) == -1) continue;
            level.removeBlock(pos, false);
            level.levelEvent(2001, pos, Block.getId(level.getBlockState(pos)));
            level.playSound(null, pos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.5f, 1.25f);
        }
    }

    private void FindTarget() {
        AABB search = licker.getBoundingBox().inflate(64);
        LivingEntity target = null;
        double distance = Double.MAX_VALUE;
        for (LivingEntity LE : licker.level().getEntitiesOfClass(LivingEntity.class, search,
                (LE) -> !(LE instanceof Infected) && !(LE instanceof UtilityEntity) && licker.hasLineOfSight(LE) && !(LE instanceof Player p && p.isCreative()))){
            if (target == null || LE.distanceTo(licker) < distance){
                target = LE;
                distance = LE.distanceTo(licker);
            }
        }
        licker.setTarget(target);
    }

    private double calculateHorizontalDistance(LivingEntity target){
        Vec3 lickerPos = licker.position().multiply(1, 0, 1);
        Vec3 targetPos = target.position().multiply(1, 0, 1);
        return lickerPos.distanceTo(targetPos);
    }

    @Override
    public void start() {
        licker.setNoGravity(true);
    }

    @Override
    public void stop() {
        licker.setNoGravity(false);
        ArrayList<Fireball> toRemove = new ArrayList<>();
        for (Fireball fireball : fireballs){
            licker.level().explode(fireball, null, null, fireball.position(),
                    1, true, Level.ExplosionInteraction.BLOCK);
            fireball.discard();
            toRemove.add(fireball);
        }
        fireballs.removeAll(toRemove);
    }
}
