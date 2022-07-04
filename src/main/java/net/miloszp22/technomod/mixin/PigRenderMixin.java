package net.miloszp22.technomod.mixin;

import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PigRenderer.class)
public class PigRenderMixin {
    private static final ResourceLocation TECHNO_LOCATION = new ResourceLocation("textures/entity/pig/technoblade.png");
    @Inject(method = "getEntityTexture(Lnet/minecraft/entity/passive/PigEntity;)Lnet/minecraft/util/ResourceLocation;",at = @At("HEAD"),cancellable = true)
    public void getEntityTexture(PigEntity p_110775_1_, CallbackInfoReturnable<ResourceLocation> cir){
        String s = p_110775_1_.getName().getString();
        if (s != null && ("Techno".equals(s) || "Technoblade".equals(s) || "Alex".equals(s))) {
            cir.setReturnValue(TECHNO_LOCATION);
        }
    }
}
