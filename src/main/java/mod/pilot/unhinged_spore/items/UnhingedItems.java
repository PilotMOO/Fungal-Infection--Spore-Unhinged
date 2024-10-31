package mod.pilot.unhinged_spore.items;

import mod.pilot.unhinged_spore.UnhingedSpore;
import mod.pilot.unhinged_spore.entity.UnhingedEntities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class UnhingedItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UnhingedSpore.MOD_ID);

    public static final RegistryObject<Item> JORT_SPAWN = ITEMS.register("jort_spawn",
            () -> new ForgeSpawnEggItem(UnhingedEntities.JORT, -1, -1, new Item.Properties()));
    public static final RegistryObject<Item> SPUNGUS_SPAWN = ITEMS.register("spungus_spawn",
            () -> new ForgeSpawnEggItem(UnhingedEntities.SPUNGUS, -1, -1, new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
