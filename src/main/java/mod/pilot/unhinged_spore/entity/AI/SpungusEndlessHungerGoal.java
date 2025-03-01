package mod.pilot.unhinged_spore.entity.AI;

import mod.pilot.unhinged_spore.Config;
import mod.pilot.unhinged_spore.entity.mobs.SpungusEntity;
import mod.pilot.unhinged_spore.items.UnhingedItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class SpungusEndlessHungerGoal extends Goal {
    private final SpungusEntity spungus;
    private ItemEntity targetItem;
    public SpungusEndlessHungerGoal(SpungusEntity spungus){
        this.spungus = spungus;
    }
    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private static final List<? extends String> blacklist = Config.SERVER.spungus_blacklist_item.get();
    private static final List<? extends String> rageList = Config.SERVER.spungus_rage_food.get();
    private static final int rageTime = Config.SERVER.spungus_rage_timer.get();
    private static final List<? extends String> toothList = Config.SERVER.spungus_break_tooth.get();

    @Override
    public void tick() {
        if (targetItem == null || spungus.tickCount % 80 == 0){
            ArrayList<ItemEntity> edibles = (ArrayList<ItemEntity>)spungus.level().getEntitiesOfClass(ItemEntity.class,
                    spungus.getBoundingBox().inflate(32),
                    (i) -> {
                for (String s : blacklist){
                    if (i.getItem().is(ForgeRegistries.ITEMS.getValue(new ResourceLocation(s)))) return false;
                }
                return true;
            });
            ItemEntity closest = null;
            double distance = Double.MAX_VALUE;
            for (ItemEntity food : edibles){
                if (food.distanceTo(spungus) < distance){
                    closest = food;
                    distance = food.distanceTo(spungus);
                }
            }
            targetItem = closest;
        }
        else{
            spungus.getNavigation().moveTo(targetItem, 1.5);
            if (spungus.distanceTo(targetItem) < spungus.getBbWidth() + 1){
                EatItem();
            }
        }
    }

    private void EatItem() {
        if (targetItem == null) return;

        spungus.Yummers();
        spungus.level().playSound(null, spungus, SoundEvents.GENERIC_EAT, SoundSource.HOSTILE, 2, 0.75f);

        RandomSource random = spungus.getRandom();
        Vec3 itemPos = targetItem.position();
        if (spungus.level() instanceof ServerLevel s){
            for (int l = 0 ; l< random.nextInt(3,6);l++){
                s.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, targetItem.getItem()), itemPos.x, itemPos.y, itemPos.z, 3,
                        ((double) random.nextFloat() - 1D) * 0.08D, ((double) random.nextFloat() - 1D) * 0.08D, ((double) random.nextFloat() - 1D) * 0.08D, 0.15F);
            }
        }

        for (String rageFood : rageList){
            if (targetItem.getItem().is(ForgeRegistries.ITEMS.getValue(new ResourceLocation(rageFood)))){
                spungus.setSpungusRAGE(spungus.getSpungusRAGE() + rageTime);
                break;
            }
        }
        for (String tooth : toothList){
            if (targetItem.getItem().is(ForgeRegistries.ITEMS.getValue(new ResourceLocation(tooth)))){
                Vec3 pos = spungus.getEyePosition();
                ItemEntity iEntity = new ItemEntity(spungus.level(), pos.x, pos.y, pos.z, new ItemStack(UnhingedItems.SPUNGUS_TOOTH.get()));
                iEntity.setDeltaMovement(
                        new Vec3(random.nextDouble() * 0.25 * (random.nextBoolean() ? -1 : 1),
                                0.5,
                                random.nextDouble() * 0.25 * (random.nextBoolean() ? -1 : 1)));
                iEntity.setGlowingTag(true);
                spungus.playSound(SoundEvents.ITEM_BREAK, 1f, 0.5f);
                spungus.level().addFreshEntity(iEntity);
                break;
            }
        }


        targetItem.getItem().shrink(1);

        if (targetItem.getItem().getCount() <= 0){
            targetItem.discard();
            targetItem = null;
        }
    }
}
