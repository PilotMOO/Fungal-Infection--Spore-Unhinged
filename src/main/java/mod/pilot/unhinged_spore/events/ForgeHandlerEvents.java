package mod.pilot.unhinged_spore.events;

import com.Harbinger.Spore.Sentities.BaseEntities.Infected;
import com.Harbinger.Spore.Sentities.BaseEntities.UtilityEntity;
import com.Harbinger.Spore.Sentities.EvolvedInfected.Griefer;
import com.Harbinger.Spore.Sentities.FallenMultipart.Licker;
import mod.pilot.unhinged_spore.Config;
import mod.pilot.unhinged_spore.UnhingedSpore;
import mod.pilot.unhinged_spore.entity.AI.LickerBossfightGoal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UnhingedSpore.MOD_ID)
public class ForgeHandlerEvents {
    @SubscribeEvent
    public static void JoinLevel(EntityJoinLevelEvent event){
        Entity E = event.getEntity();
        if (event.getEntity() instanceof Griefer G && Config.SERVER.should_griefers_blow_up_now.get()){
            G.level().explode(G, G.getCustomDamage(G), null, G.position(), 4, false, Level.ExplosionInteraction.MOB);
            G.kill();
            return;
        }
        if (E instanceof Licker L && Config.SERVER.licker_bossfight.get()){
            L.goalSelector.addGoal(1, new LickerBossfightGoal(L, Config.SERVER.licker_fly_Y.get(), Config.SERVER.licker_firerate.get()));
        }
    }
}
