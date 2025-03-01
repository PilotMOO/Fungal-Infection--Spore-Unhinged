package mod.pilot.unhinged_spore.advancements;

import mod.pilot.unhinged_spore.UnhingedSpore;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;

public class UnhingedAdvancements {
    public static final CustomTriggerAdvancement U_BASE = new CustomTriggerAdvancement(new ResourceLocation(UnhingedSpore.MOD_ID,
            "on_join"));

    public static void RegisterAll(){
        CriteriaTriggers.register(U_BASE);
    }
}
