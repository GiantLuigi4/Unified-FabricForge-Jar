package com.tfc.realfirstperson.forge;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tfc.realfirstperson.forge.mixins.CameraAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector4f;

public class PlayerRenderer {
	public static void updateCamera(MatrixStack matrices) {
		if (!RealFirstPerson.enabled || !RealFirstPerson.useTranforms) return;
		if (
				Minecraft.getInstance().getRenderManager().info != null &&
						Minecraft.getInstance().getRenderManager().info.getRotation() != null &&
						!((CameraAccessor) Minecraft.getInstance().getRenderManager().info).RFP_isTrulyThirdPerson()
		) {
			Quaternion quaternion = Minecraft.getInstance().getRenderManager().info.getRotation();
			MatrixStack stack = new MatrixStack();
			float amt;
			if (!(Minecraft.getInstance().getRenderViewEntity() instanceof PlayerEntity))
				amt = (((Minecraft.getInstance().getRenderViewEntity().getHeight() - Minecraft.getInstance().getRenderViewEntity().getEyeHeight(Minecraft.getInstance().getRenderViewEntity().getPose())) * (1f / 0.17999995f)) * 2) / 10f;
			else
				amt = ((Minecraft.getInstance().getRenderViewEntity().getHeight()) / 1.8f) * RealFirstPerson.camDist;
			if (RealFirstPerson.trueCam)
				matrices.translate(0, (Minecraft.getInstance().getRenderViewEntity().isSneaking() ? 0.275 : 0.2125f) * amt / RealFirstPerson.camDist, 0);
			stack.rotate(quaternion);
			Vector4f vector4f;
			if (RealFirstPerson.trueCam) {
//				vector4f = new Vector4f(0, -0.2f, -0.2f, 0);
				vector4f = new Vector4f(0, -amt, -amt, 0);
//				float angle = (float)Math.toRadians(Minecraft.getInstance().getRenderViewEntity().getHeadYaw() + 90);
//				matrices.translate(-Math.cos(angle) * amt, 0, -Math.sin(angle) * amt);
				if (Minecraft.getInstance().getRenderViewEntity().isVisuallySwimming() || ((PlayerEntity) Minecraft.getInstance().getRenderViewEntity()).isElytraFlying()) {
					Vector4f vector4f1 = new Vector4f(0, 0, -(amt) / RealFirstPerson.camDist * 2, 0);
					vector4f1.transform(stack.getLast().getMatrix());
					matrices.translate(vector4f1.getX(), vector4f1.getY(), vector4f1.getZ());
				}
			} else
				vector4f = new Vector4f(0, -0.25f * amt / RealFirstPerson.camDist, -0.4f * amt / RealFirstPerson.camDist, 0);
			vector4f.transform(stack.getLast().getMatrix());
			matrices.translate(vector4f.getX(), vector4f.getY(), vector4f.getZ());
		}
		
	}
	
	public static void setHiddenFlags(PlayerModel<?> model) {
		if (RealFirstPerson.enabled)
			model.bipedHead.showModel = false;
		if (!RealFirstPerson.renderHeadware)
			model.bipedHeadwear.showModel = false;
	}
}
