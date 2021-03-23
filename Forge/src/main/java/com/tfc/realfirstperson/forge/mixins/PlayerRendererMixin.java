package com.tfc.realfirstperson.forge.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {
	@Inject(at = @At("TAIL"), method = "setModelVisibilities")
	public void postSetPose(AbstractClientPlayerEntity abstractClientPlayerEntity, CallbackInfo ci) {
		if (
				!((CameraAccessor) Minecraft.getInstance().getRenderManager().info).RFP_isTrulyThirdPerson() &&
						Minecraft.getInstance().getRenderViewEntity() == abstractClientPlayerEntity
		) {
			com.tfc.realfirstperson.forge.PlayerRenderer.setHiddenFlags(((PlayerRenderer) (Object) this).getEntityModel());
		}
	}
}
