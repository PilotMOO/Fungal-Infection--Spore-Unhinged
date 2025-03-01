package mod.pilot.unhinged_spore.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

//"heavily referenced" the achievement system inside the Alex's Caves GitHub.
//Thanks, Alex's Caves dev team for having a public repo, I use it alot :]
public class CustomTriggerAdvancement extends SimpleCriterionTrigger<CustomTriggerAdvancement.Instance> {
    public final ResourceLocation rLocation;
    public CustomTriggerAdvancement(ResourceLocation rLocation){
        this.rLocation = rLocation;
    }
    @Override
    protected @NotNull Instance createInstance(@NotNull JsonObject pJson, @NotNull ContextAwarePredicate pPredicate,
                                               @NotNull DeserializationContext pDeserializationContext) {
        return new Instance(rLocation, pPredicate);
    }
    @Override
    public @NotNull ResourceLocation getId() {
        return rLocation;
    }

    public void trigger(ServerPlayer p_192180_1_) {
        this.trigger(p_192180_1_, (p_226308_1_) -> {
            return true;
        });
    }

    public static class Instance extends AbstractCriterionTriggerInstance{

        public Instance(ResourceLocation pCriterion, ContextAwarePredicate pPlayer) {
            super(pCriterion, pPlayer);
        }
    }
}
