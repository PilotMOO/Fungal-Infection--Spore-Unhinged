package mod.pilot.unhinged_spore.entity.client.mobs;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.pilot.unhinged_spore.entity.mobs.JortEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class JortRenderer extends GeoEntityRenderer<JortEntity> {
    public JortRenderer(EntityRendererProvider.Context renderManager){
        super(renderManager, new JortModel());
    }
}
