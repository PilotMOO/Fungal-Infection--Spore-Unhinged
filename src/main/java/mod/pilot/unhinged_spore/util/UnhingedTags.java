package mod.pilot.unhinged_spore.util;

import mod.pilot.unhinged_spore.UnhingedSpore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class UnhingedTags {
    public static class Blocks{
        private static TagKey<Block> tag(String name){
            return BlockTags.create(new ResourceLocation(UnhingedSpore.MOD_ID, name));
        }

        //public static final TagKey<Block> EXAMPLE_BLOCK = tag("example");
    }

    public static class Items{
        private static TagKey<Item> tag(String name){
            return ItemTags.create(new ResourceLocation(UnhingedSpore.MOD_ID, name));
        }

        public static final TagKey<Item> UNHINGED_ITEM = tag("unhinged_item");
    }
}
