package mod.pilot.unhinged_spore;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;


@Mod.EventBusSubscriber(modid = UnhingedSpore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    public static class Server{
        public final ForgeConfigSpec.ConfigValue<Double> jort_chance;

        public final ForgeConfigSpec.ConfigValue<Boolean> licker_bossfight;
        public final ForgeConfigSpec.ConfigValue<Integer> licker_fly_Y;
        public final ForgeConfigSpec.ConfigValue<Integer> licker_firerate;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> spungus_rage_food;
        public final ForgeConfigSpec.ConfigValue<Integer> spungus_rage_timer;

        public Server(ForgeConfigSpec.Builder builder){
            builder.push("Unhinged Spore configuration");

            jort_chance = builder.defineInRange("Chance of a griefer transforming into a jort (set to 0 to disable Jort)", 0.1, 0,
                    1);

            licker_bossfight = builder.define("Should lickers (dropped by gazenbrechers) turn into a bossfight?", true);
            licker_fly_Y = builder.defineInRange("The Y level above the target that the licker wants to fly around", 8,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
            licker_firerate = builder.defineInRange("The rate of fire for the licker's fireballs", 60,
                    1, Integer.MAX_VALUE);

            spungus_rage_timer = builder.defineInRange("How long Spungus will go into a rage for when eating specific foods",
                    1200, 0, Integer.MAX_VALUE);
            spungus_rage_food = builder.defineList("Food that makes Spungus go on a rampage",
                    Lists.newArrayList("minecraft:poisonous_potato"), o -> o instanceof String);

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