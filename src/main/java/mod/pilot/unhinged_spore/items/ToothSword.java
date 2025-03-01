package mod.pilot.unhinged_spore.items;

import com.Harbinger.Spore.Core.Ssounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ToothSword extends SwordItem {
    public ToothSword(Properties pProperties) {
        super(Tiers.NETHERITE, -2, 20, pProperties.defaultDurability(-1));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level pLevel,
                                @NotNull List<Component> components, @NotNull TooltipFlag advanced) {
        components.add(Component.translatable("item.unhinged_spore.tooth_sword.tooltip"));
        if (advanced.isAdvanced()){
            components.add(Component.translatable("item.unhinged_spore.tooth_sword.tooltip_advanced"));
        } else{
            components.add(Component.translatable("item.unhinged_spore.advanced_tooltip_hint"));
        }
        super.appendHoverText(stack, pLevel, components, advanced);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity user) {
        target.invulnerableTime = 0;
        target.playSound(SoundEvents.SKELETON_HURT, 1.5f,
                (float) (0.75 + (user.getRandom().nextDouble() - 0.25) * (user.getRandom().nextBoolean() ? -1 : 1)));
        return super.hurtEnemy(stack, target, user);
    }
}
