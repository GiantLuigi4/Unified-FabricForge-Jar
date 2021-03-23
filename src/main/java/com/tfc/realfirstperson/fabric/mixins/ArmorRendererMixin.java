package com.tfc.realfirstperson.fabric.mixins;

import com.tfc.realfirstperson.fabric.client.RealFirstPersonClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
	@Inject(at = @At("HEAD"), method = "renderArmor", cancellable = true)
	public void onRenderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T livingEntity, EquipmentSlot equipmentSlot, int i, A bipedEntityModel, CallbackInfo ci) {
		if (
				equipmentSlot.equals(EquipmentSlot.HEAD) &&
						!((CameraAccessor) MinecraftClient.getInstance().getEntityRenderDispatcher().camera).RFP_isTrulyThirdPerson() &&
						MinecraftClient.getInstance().getCameraEntity() == livingEntity
		) {
			if (!RealFirstPersonClient.renderHelm) {
				ci.cancel();
			}
		}
	}
}
