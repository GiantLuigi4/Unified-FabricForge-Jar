package com.tfc.realfirstperson.fabric.client;

import com.tfc.realfirstperson.fabric.mixins.CameraAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Quaternion;

public class PlayerRenderer {
	public static void doRender(float tickDelta, MatrixStack matrices, BufferBuilderStorage buffers, MinecraftClient client, HeldItemRenderer firstPersonRenderer) {
		if (!RealFirstPersonClient.enabled) {
			firstPersonRenderer.renderItem(tickDelta, matrices, buffers.getEntityVertexConsumers(), client.player, client.getEntityRenderDispatcher().getLight(client.player, tickDelta));
		}
	}
	
	public static void updateCamera(MatrixStack matrices) {
		if (!RealFirstPersonClient.enabled || !RealFirstPersonClient.useTranforms) return;
		if (
				MinecraftClient.getInstance().getEntityRenderDispatcher().camera != null &&
						MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getRotation() != null &&
						!((CameraAccessor) MinecraftClient.getInstance().getEntityRenderDispatcher().camera).RFP_isTrulyThirdPerson()
		) {
			Quaternion quaternion = MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getRotation();
			MatrixStack stack = new MatrixStack();
			float amt;
			if (!(MinecraftClient.getInstance().getCameraEntity() instanceof PlayerEntity))
				amt = (((MinecraftClient.getInstance().getCameraEntity().getHeight() - MinecraftClient.getInstance().getCameraEntity().getEyeHeight(MinecraftClient.getInstance().cameraEntity.getPose())) * (1f / 0.17999995f)) * 2) / 10f;
			else
				amt = ((MinecraftClient.getInstance().cameraEntity.getHeight()) / 1.8f) * RealFirstPersonClient.camDist;
			if (RealFirstPersonClient.trueCam)
				matrices.translate(0, (MinecraftClient.getInstance().cameraEntity.isSneaking() ? 0.275 : 0.2125f) * amt / RealFirstPersonClient.camDist, 0);
			stack.multiply(quaternion);
			Vector4f vector4f;
			if (RealFirstPersonClient.trueCam) {
//				vector4f = new Vector4f(0, -0.2f, -0.2f, 0);
				vector4f = new Vector4f(0, -amt, -amt, 0);
//				float angle = (float)Math.toRadians(MinecraftClient.getInstance().cameraEntity.getHeadYaw() + 90);
//				matrices.translate(-Math.cos(angle) * amt, 0, -Math.sin(angle) * amt);
				if (MinecraftClient.getInstance().getCameraEntity().isInSwimmingPose() || ((PlayerEntity)MinecraftClient.getInstance().getCameraEntity()).isFallFlying()) {
					Vector4f vector4f1 = new Vector4f(0, 0, -(amt) / RealFirstPersonClient.camDist * 2, 0);
					vector4f1.transform(stack.peek().getModel());
					matrices.translate(vector4f1.getX(), vector4f1.getY(), vector4f1.getZ());
				}
			} else vector4f = new Vector4f(0, -0.25f * amt / RealFirstPersonClient.camDist, -0.4f * amt / RealFirstPersonClient.camDist, 0);
			vector4f.transform(stack.peek().getModel());
			matrices.translate(vector4f.getX(), vector4f.getY(), vector4f.getZ());
		}
	}
	
	public static void setHiddenFlags(PlayerEntityModel model) {
		if (RealFirstPersonClient.enabled)
			model.head.visible = false;
		if (!RealFirstPersonClient.renderHeadware)
			model.helmet.visible = false;
	}
}
