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

        public final ForgeConfigSpec.ConfigValue<Boolean> arm_bossfight;
        public final ForgeConfigSpec.ConfigValue<Integer> arm_jump_speed;
        public final ForgeConfigSpec.ConfigValue<Double> arm_jump_strength;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> spungus_rage_food;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> spungus_rage_blacklist;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> spungus_blacklist_item;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> spungus_break_tooth;
        public final ForgeConfigSpec.ConfigValue<Integer> spungus_rage_timer;

        public Server(ForgeConfigSpec.Builder builder){
            builder.push("Unhinged Spore configuration");

            builder.push("Replacement spawning");
            jort_chance = builder.defineInRange("Chance of a griefer transforming into a jort (set to 0 to disable Jort spawning)",
                    0.1, 0, 1);
            spungus_chance = builder.defineInRange("Chance of a brute transforming into a spungus (set to 0 to disable Spungus spawning)",
                    0.1, 0, 1);
            builder.pop();

            builder.push("Licker bossfight configuration");
            licker_bossfight = builder.define("Should lickers (dropped by gazenbrechers) turn into a bossfight?", true);
            licker_fly_Y = builder.defineInRange("The Y level above the target that the licker wants to fly around", 8,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
            licker_firerate = builder.defineInRange("The rate of fire for the licker's fireballs", 60,
                    1, Integer.MAX_VALUE);
            builder.pop();

            builder.push("Howitzer Arm bossfight configuration");
            arm_bossfight = builder.define("Should Howitzer Arms (dropped by Howitzers) turn into a bossfight?", true);
            arm_jump_speed = builder.defineInRange("How frequently the howitzer arm jumps (in ticks, do note that right arms jump 3x as quickly",
                    20, 0, Integer.MAX_VALUE);
            arm_jump_strength = builder.defineInRange("How strong the howitzer arm jumps (do note that right arm jumps are 3x weaker)",
                    1.25, 0, Double.MAX_VALUE);
            builder.pop();

            builder.push("Spungus configuration");
            spungus_rage_timer = builder.defineInRange("How long Spungus will go into a rage for when eating specific foods",
                    1200, 0, Integer.MAX_VALUE);
            spungus_rage_food = builder.defineList("Food that makes Spungus go on a rampage",
                    Lists.newArrayList("minecraft:poisonous_potato"), o -> o instanceof String);
            spungus_rage_blacklist = builder.defineList("Entities that Spungus will not attack when on a rampage",
                    Lists.newArrayList("unhinged_spore:spungus"), o -> o instanceof String);
            spungus_blacklist_item = builder.defineList("Food that Spungus HATES and WILL NOT EAT",
                    Lists.newArrayList("unhinged_spore:spungus_tooth"), o -> o instanceof String);
            spungus_break_tooth = builder.defineList("Food that makes Spungus loose a tooth",
                    Lists.newArrayList("minecraft:bedrock", "minecraft:netherite_ingot", "minecraft:netherite_block",
                            "minecraft:diamond_block"), o -> o instanceof String);
            builder.pop();

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