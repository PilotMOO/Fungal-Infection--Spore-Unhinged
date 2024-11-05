package mod.pilot.unhinged_spore.events;

import com.Harbinger.Spore.Sentities.EvolvedInfected.Brute;
import com.Harbinger.Spore.Sentities.EvolvedInfected.Griefer;
import com.Harbinger.Spore.Sentities.FallenMultipart.HowitzerArm;
import com.Harbinger.Spore.Sentities.FallenMultipart.Licker;
import mod.pilot.unhinged_spore.Config;
import mod.pilot.unhinged_spore.UnhingedSpore;
import mod.pilot.unhinged_spore.entity.AI.DEPRECATEDHowitzerArmBossfightGoal;
import mod.pilot.unhinged_spore.entity.AI.DEPRECATEDHowitzerArmCopyGoal;
import mod.pilot.unhinged_spore.entity.AI.LickerBossfightGoal;
import mod.pilot.unhinged_spore.entity.UnhingedEntities;
import mod.pilot.unhinged_spore.entity.mobs.JortEntity;
import mod.pilot.unhinged_spore.entity.mobs.SpungusEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UnhingedSpore.MOD_ID)
public class ForgeHandlerEvents {
    @SubscribeEvent
    public static void JoinLevel(EntityJoinLevelEvent event){
        Entity E = event.getEntity();
        if (E instanceof Licker L && Config.SERVER.licker_bossfight.get()){
            L.goalSelector.addGoal(1, new LickerBossfightGoal(L, Config.SERVER.licker_fly_Y.get(), Config.SERVER.licker_firerate.get()));
        }
        /*if (E instanceof HowitzerArm A && Config.SERVER.arm_bossfight.get()){
            for (Goal G : A.goalSelector.getAvailableGoals()){
                if (G.getClass() == DEPRECATEDHowitzerArmBossfightGoal.class) return;
            }
            A.goalSelector.addGoal(1, new DEPRECATEDHowitzerArmCopyGoal(A, Config.SERVER.arm_split_rate.get()));
            A.goalSelector.addGoal(1, new DEPRECATEDHowitzerArmBossfightGoal(A, Config.SERVER.arm_throw_speed.get(),
                    Config.SERVER.arm_min_blocks.get(), Config.SERVER.arm_max_blocks.get(), 16));
        }*/
        if (E instanceof Griefer G && G.getRandom().nextDouble() < Config.SERVER.jort_chance.get()){
            if (!event.getLevel().getServer().isReady()) return;
            Level level = event.getLevel();

            JortEntity jort = UnhingedEntities.JORT.get().create(level);
            jort.copyPosition(G);
            level.addFreshEntity(jort);
            level.playSound(null, jort, SoundEvents.ITEM_PICKUP, SoundSource.HOSTILE, 1f, 1.5f);

            G.setPos(G.position().add(0, -400, 0));
        }
        if (E instanceof Brute B && B.getRandom().nextDouble() < Config.SERVER.spungus_chance.get()){
            if (!event.getLevel().getServer().isReady()) return;
            Level level = event.getLevel();

            SpungusEntity spungus = UnhingedEntities.SPUNGUS.get().create(level);
            spungus.copyPosition(B);
            level.addFreshEntity(spungus);
            level.playSound(null, spungus, SoundEvents.ITEM_PICKUP, SoundSource.HOSTILE, 1f, 1.5f);

            B.setPos(B.position().add(0, -400, 0));
        }
    }
}
