package mod.pilot.unhinged_spore.events;

import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.BaseEntities.UtilityEntity;
import com.Harbinger.Spore.Sentities.EvolvedInfected.Brute;
import com.Harbinger.Spore.Sentities.EvolvedInfected.Griefer;
import com.Harbinger.Spore.Sentities.FallenMultipart.HowitzerArm;
import com.Harbinger.Spore.Sentities.FallenMultipart.Licker;
import mod.pilot.unhinged_spore.Config;
import mod.pilot.unhinged_spore.UnhingedSpore;
import mod.pilot.unhinged_spore.advancements.UnhingedAdvancements;
import mod.pilot.unhinged_spore.entity.AI.LickerBossfightGoal;
import mod.pilot.unhinged_spore.entity.UnhingedEntities;
import mod.pilot.unhinged_spore.entity.mobs.JortEntity;
import mod.pilot.unhinged_spore.entity.mobs.SpungusEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = UnhingedSpore.MOD_ID)
public class ForgeHandlerEvents {
    @SubscribeEvent
    public static void JoinLevel(EntityJoinLevelEvent event){
        Entity E = event.getEntity();
        if (E instanceof ServerPlayer sP){
            UnhingedAdvancements.U_BASE.trigger(sP);
            return;
        }
        if (E instanceof Licker L && Config.SERVER.licker_bossfight.get()){
            L.goalSelector.addGoal(1, new LickerBossfightGoal(L, Config.SERVER.licker_fly_Y.get(), Config.SERVER.licker_firerate.get()));
        }
        if (E instanceof HowitzerArm A && Config.SERVER.arm_bossfight.get()){
            //The MoveControl application is managed via a Mixin. See HowitzerArmBossfightMixin
            A.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(A, LivingEntity.class, false,
                    (LE) -> !(LE instanceof Infected) && !(LE instanceof UtilityEntity) && A.hasLineOfSight(LE)
                            && !(LE instanceof Player p && p.isCreative())){
                @Override
                protected @NotNull AABB getTargetSearchArea(double pTargetDistance) {
                    return mob.getBoundingBox().inflate(64, 16, 64);
                }
            });
        }
        if (E instanceof Griefer G && G.getRandom().nextDouble() < Config.SERVER.jort_chance.get()){
            if (!event.getLevel().getServer().isReady()) return;
            Level level = event.getLevel();

            JortEntity jort = UnhingedEntities.JORT.get().create(level);
            jort.copyPosition(G);
            level.addFreshEntity(jort);
            level.playSound(null, jort, SoundEvents.ITEM_PICKUP, SoundSource.HOSTILE, 1f, 1.5f);

            event.setCanceled(true);
            //G.setPos(G.position().add(0, -400, 0));
        }
        if (E instanceof Brute B && B.getRandom().nextDouble() < Config.SERVER.spungus_chance.get()){
            if (!event.getLevel().getServer().isReady()) return;
            Level level = event.getLevel();

            SpungusEntity spungus = UnhingedEntities.SPUNGUS.get().create(level);
            spungus.copyPosition(B);
            level.addFreshEntity(spungus);
            level.playSound(null, spungus, SoundEvents.ITEM_PICKUP, SoundSource.HOSTILE, 1f, 1.5f);

            event.setCanceled(true);
            //B.setPos(B.position().add(0, -400, 0));
        }
    }
}
