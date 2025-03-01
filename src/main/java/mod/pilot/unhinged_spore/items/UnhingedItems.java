package mod.pilot.unhinged_spore.items;

import mod.pilot.unhinged_spore.UnhingedSpore;
import mod.pilot.unhinged_spore.entity.UnhingedEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UnhingedItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UnhingedSpore.MOD_ID);

    public static final RegistryObject<Item> JORT_SPAWN = ITEMS.register("jort_spawn",
            () -> new ForgeSpawnEggItem(UnhingedEntities.JORT, -1, -1, new Item.Properties()));
    public static final RegistryObject<Item> SPUNGUS_SPAWN = ITEMS.register("spungus_spawn",
            () -> new ForgeSpawnEggItem(UnhingedEntities.SPUNGUS, -1, -1, new Item.Properties()));

    public static final RegistryObject<Item> SPUNGUS_TOOTH = ITEMS.register("spungus_tooth",
            () -> new Item(new Item.Properties()){
                @Override
                public void appendHoverText(ItemStack stack, @Nullable Level pLevel,
                                            @NotNull List<Component> components, @NotNull TooltipFlag advanced) {
                    components.add(Component.translatable("item.unhinged_spore.spungus_tooth.tooltip"));
                    if (advanced.isAdvanced()){
                        components.add(Component.translatable("item.unhinged_spore.spungus_tooth.tooltip_advanced"));
                    } else{
                        components.add(Component.translatable("item.unhinged_spore.advanced_tooltip_hint"));
                    }
                    super.appendHoverText(stack, pLevel, components, advanced);
                }
            });
    public static final RegistryObject<Item> SUPER_GLUE = ITEMS.register("super_glue",
            () -> new SuperGlueItem(new Item.Properties().food(new FoodProperties.Builder().build())));
    public static final RegistryObject<Item> TOOTH_SWORD = ITEMS.register("tooth_sword",
            () -> new ToothSword(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
