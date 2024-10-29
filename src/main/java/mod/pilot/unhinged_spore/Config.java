package mod.pilot.unhinged_spore;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;


@Mod.EventBusSubscriber(modid = UnhingedSpore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    public static class Server{
        public final ForgeConfigSpec.ConfigValue<Boolean> should_griefers_blow_up_now;

        public final ForgeConfigSpec.ConfigValue<Boolean> licker_bossfight;
        public final ForgeConfigSpec.ConfigValue<Integer> licker_fly_Y;
        public final ForgeConfigSpec.ConfigValue<Integer> licker_firerate;

        public Server(ForgeConfigSpec.Builder builder){
            builder.push("Unhinged Spore configuration");

            should_griefers_blow_up_now = builder.define("Should griefers just explode instantly?" +
                    "(this does not give a shit about what kind of griefer)", false);

            licker_bossfight = builder.define("Should lickers (dropped by gazenbrechers) turn into a bossfight?", true);
            licker_fly_Y = builder.defineInRange("The Y level above the target that the licker wants to fly around", 8,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
            licker_firerate = builder.defineInRange("The rate of fire for the licker's fireballs", 60,
                    1, Integer.MAX_VALUE);

            builder.pop();
        }
    }

    static {
        Pair<Server, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER = commonSpecPair.getLeft();
        SERVER_SPEC = commonSpecPair.getRight();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
}