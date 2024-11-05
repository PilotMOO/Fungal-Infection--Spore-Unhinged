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
        public final ForgeConfigSpec.ConfigValue<Double> spungus_chance;

        public final ForgeConfigSpec.ConfigValue<Boolean> licker_bossfight;
        public final ForgeConfigSpec.ConfigValue<Integer> licker_fly_Y;
        public final ForgeConfigSpec.ConfigValue<Integer> licker_firerate;

        /*public final ForgeConfigSpec.ConfigValue<Boolean> arm_bossfight;
        public final ForgeConfigSpec.ConfigValue<Integer> arm_split_rate;
        public final ForgeConfigSpec.ConfigValue<Integer> arm_split_max;

        public final ForgeConfigSpec.ConfigValue<Integer> arm_throw_speed;
        public final ForgeConfigSpec.ConfigValue<Integer> arm_min_blocks;
        public final ForgeConfigSpec.ConfigValue<Integer> arm_max_blocks;*/

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> spungus_rage_food;
        public final ForgeConfigSpec.ConfigValue<Integer> spungus_rage_timer;

        public Server(ForgeConfigSpec.Builder builder){
            builder.push("Unhinged Spore configuration");

            jort_chance = builder.defineInRange("Chance of a griefer transforming into a jort (set to 0 to disable Jort spawning)",
                    0.1, 0, 1);
            spungus_chance = builder.defineInRange("Chance of a brute transforming into a spungus (set to 0 to disable Spungus spawning)",
                    0.1, 0, 1);


            licker_bossfight = builder.define("Should lickers (dropped by gazenbrechers) turn into a bossfight?", true);
            licker_fly_Y = builder.defineInRange("The Y level above the target that the licker wants to fly around", 8,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
            licker_firerate = builder.defineInRange("The rate of fire for the licker's fireballs", 60,
                    1, Integer.MAX_VALUE);

            /*arm_bossfight = builder.define("Should Howitzer Arms (dropped by Howitzers) turn into a bossfight?", true);
            arm_split_rate = builder.defineInRange("The rate (in ticks) at which the Howitzer Arm reproduces", 2400,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
            arm_split_max = builder.defineInRange("The maximum amount of arm clones the Howitzer Arm can create. Set to 0 to disable, -1 for infinite",
                    8, -1, Integer.MAX_VALUE);
            arm_throw_speed = builder.defineInRange("The rate (in ticks) at which the Howitzer Arm throws blocks",
                    200, 1, Integer.MAX_VALUE);
            arm_min_blocks = builder.defineInRange("The minimum amount of blocks the howitzer arm will attempt to throw at once (MUST BE LOWER THAN THE MAX)",
                    1, 1, Integer.MAX_VALUE);
            arm_max_blocks = builder.defineInRange("The maximum amount of blocks the howitzer arm will attempt to throw at once (MUST BE HIGHER THAN THE MIN)",
                    3, 1, Integer.MAX_VALUE);*/

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