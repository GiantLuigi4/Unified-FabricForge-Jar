package com.tfc.realfirstperson.fabric.mixins;

import com.tfc.realfirstperson.fabric.client.PlayerRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow private boolean renderingPanorama;
	
	@Shadow public abstract void loadProjectionMatrix(Matrix4f matrix4f);
	
	@Shadow public abstract Matrix4f getBasicProjectionMatrix(Camera camera, float f, boolean bl);
	
	@Shadow protected abstract void bobViewWhenHurt(MatrixStack matrixStack, float f);
	
	@Shadow @Final private MinecraftClient client;
	
	@Shadow protected abstract void bobView(MatrixStack matrixStack, float f);
	
	@Shadow @Final private LightmapTextureManager lightmapTextureManager;
	@Shadow @Final public HeldItemRenderer firstPersonRenderer;
	@Shadow @Final private BufferBuilderStorage buffers;
	
	/**
	 * @author
	 */
	//TODO: migrate to redirect
	@Overwrite
	private void renderHand(MatrixStack matrices, Camera camera, float tickDelta) {
		if (!this.renderingPanorama) {
			this.loadProjectionMatrix(this.getBasicProjectionMatrix(camera, tickDelta, false));
			MatrixStack.Entry entry = matrices.peek();
			entry.getModel().loadIdentity();
			entry.getNormal().loadIdentity();
			matrices.push();
			this.bobViewWhenHurt(matrices, tickDelta);
			if (this.client.options.bobView) {
				this.bobView(matrices, tickDelta);
			}
			
			boolean bl = this.client.getCameraEntity() instanceof LivingEntity && ((LivingEntity) this.client.getCameraEntity()).isSleeping();
			if (this.client.options.getPerspective().isFirstPerson() && !bl && !this.client.options.hudHidden && this.client.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR) {
				this.lightmapTextureManager.enable();
				PlayerRenderer.doRender(tickDelta, matrices, buffers, client, firstPersonRenderer);
				this.lightmapTextureManager.disable();
			}
			
			matrices.pop();
			if (this.client.options.getPerspective().isFirstPerson() && !bl) {
				InGameOverlayRenderer.renderOverlays(this.client, matrices);
				this.bobViewWhenHurt(matrices, tickDelta);
			}
			
			if (this.client.options.bobView) {
				this.bobView(matrices, tickDelta);
			}
			
		}
	}
}
