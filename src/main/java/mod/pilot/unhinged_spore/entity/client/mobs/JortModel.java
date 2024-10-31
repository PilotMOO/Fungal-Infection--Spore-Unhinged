package mod.pilot.unhinged_spore.entity.client.mobs;

import mod.azure.azurelib.model.GeoModel;
import mod.pilot.unhinged_spore.UnhingedSpore;
import mod.pilot.unhinged_spore.entity.mobs.JortEntity;
import net.minecraft.resources.ResourceLocation;

public class JortModel extends GeoModel<JortEntity> {
    private static final ResourceLocation model = new ResourceLocation(UnhingedSpore.MOD_ID, "geo/entity/mob/jort.geo.json");
    private static final ResourceLocation texture = new ResourceLocation(UnhingedSpore.MOD_ID, "textures/entity/mob/jort.png");
    private static final ResourceLocation animation = new ResourceLocation(UnhingedSpore.MOD_ID, "animations/entity/mob/jort.animation.json");


    @Override
    public ResourceLocation getModelResource(JortEntity animatable) {
        return model;
    }
    @Override
    public ResourceLocation getTextureResource(JortEntity animatable) {
        return texture;
    }
    @Override
    public ResourceLocation getAnimationResource(JortEntity animatable) {
        return animation;
    }
}
