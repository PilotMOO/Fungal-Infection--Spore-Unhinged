package mod.pilot.unhinged_spore.entity.client.mobs;

import mod.azure.azurelib.model.GeoModel;
import mod.pilot.unhinged_spore.UnhingedSpore;
import mod.pilot.unhinged_spore.entity.mobs.JortEntity;
import mod.pilot.unhinged_spore.entity.mobs.SpungusEntity;
import net.minecraft.resources.ResourceLocation;

public class SpungusModel extends GeoModel<SpungusEntity> {
    private static final ResourceLocation model = new ResourceLocation(UnhingedSpore.MOD_ID, "geo/entity/mob/spungus.geo.json");
    private static final ResourceLocation texture = new ResourceLocation(UnhingedSpore.MOD_ID, "textures/entity/mob/spungus.png");
    private static final ResourceLocation animation = new ResourceLocation(UnhingedSpore.MOD_ID, "animations/entity/mob/spungus.animation.json");


    @Override
    public ResourceLocation getModelResource(SpungusEntity animatable) {
        return model;
    }
    @Override
    public ResourceLocation getTextureResource(SpungusEntity animatable) {
        return texture;
    }
    @Override
    public ResourceLocation getAnimationResource(SpungusEntity animatable) {
        return animation;
    }
}
