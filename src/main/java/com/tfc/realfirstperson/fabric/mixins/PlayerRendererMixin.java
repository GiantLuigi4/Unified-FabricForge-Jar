package com.tfc.realfirstperson.fabric.mixins;

import com.tfc.realfirstperson.fabric.client.PlayerRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin {
	@Inject(at = @At("TAIL"), method = "setModelPose")
	public void postSetPose(AbstractClientPlayerEntity abstractClientPlayerEntity, CallbackInfo ci) {
		if (
				!((CameraAccessor)MinecraftClient.getInstance().getEntityRenderDispatcher().camera).RFP_isTrulyThirdPerson() &&
						MinecraftClient.getInstance().getCameraEntity() == abstractClientPlayerEntity
		) {
			PlayerRenderer.setHiddenFlags(((PlayerEntityRenderer)(Object)this).getModel());
		}
	}
}
