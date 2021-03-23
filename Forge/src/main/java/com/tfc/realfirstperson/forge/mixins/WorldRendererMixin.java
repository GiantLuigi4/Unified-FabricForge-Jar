package com.tfc.realfirstperson.forge.mixins;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tfc.realfirstperson.forge.PlayerRenderer;
import com.tfc.realfirstperson.forge.RealFirstPerson;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.vector.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Inject(at = @At("HEAD"), method = "updateCameraAndRender")
	public void renderPre(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, ActiveRenderInfo camera, GameRenderer gameRenderer, LightTexture lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		PlayerRenderer.updateCamera(matrices);
		
		RealFirstPerson.updateConfigs();
	}
}
