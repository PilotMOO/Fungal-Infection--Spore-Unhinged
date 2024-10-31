package mod.pilot.unhinged_spore;

import mod.pilot.unhinged_spore.entity.UnhingedEntities;
import mod.pilot.unhinged_spore.items.UnhingedCreativeTabs;
import mod.pilot.unhinged_spore.items.UnhingedItems;
import mod.pilot.unhinged_spore.sound.UnhingedSounds;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(UnhingedSpore.MOD_ID)
public class UnhingedSpore
{
    public static final String MOD_ID = "unhinged_spore";
    public UnhingedSpore()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        UnhingedEntities.register(modEventBus);
        UnhingedSounds.register(modEventBus);
        UnhingedItems.register(modEventBus);
        UnhingedCreativeTabs.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SERVER_SPEC, "unhinged_spore.toml");
        Config.loadConfig(Config.SERVER_SPEC, FMLPaths.CONFIGDIR.get().resolve("unhinged_spore.toml").toString());
    }
}
