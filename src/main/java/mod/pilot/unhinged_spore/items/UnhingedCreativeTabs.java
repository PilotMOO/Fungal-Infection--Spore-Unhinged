package mod.pilot.unhinged_spore.items;

import com.Harbinger.Spore.Core.Sitems;
import mod.pilot.unhinged_spore.UnhingedSpore;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class UnhingedCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            UnhingedSpore.MOD_ID);
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static final RegistryObject<CreativeModeTab> UNHINGED_TAB = CREATIVE_MODE_TABS.register("unhinged_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 3).icon(() -> new ItemStack(Sitems.ALTERED_SPLEEN.get()))
                    .title(Component.translatable("creativetab.unhinged_tab"))
                    .displayItems((something, register) ->{
                        register.accept(UnhingedItems.JORT_SPAWN.get());
                        register.accept(UnhingedItems.SPUNGUS_SPAWN.get());
                    })
                    .build());
}
