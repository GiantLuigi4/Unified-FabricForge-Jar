package com.tfc.realfirstperson.forge.mixins;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tfc.realfirstperson.forge.RealFirstPerson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedArmorLayer.class)
public class ArmorRendererMixin<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> {
	@Inject(at = @At("HEAD"), method = "func_241739_a_", cancellable = true)
	public void onRenderArmor(MatrixStack matrices, IRenderTypeBuffer vertexConsumers, T livingEntity, EquipmentSlotType equipmentSlot, int i, A bipedEntityModel, CallbackInfo ci) {
		if (
				equipmentSlot.equals(EquipmentSlotType.HEAD) &&
						!((CameraAccessor) Minecraft.getInstance().getRenderManager().info).RFP_isTrulyThirdPerson() &&
						Minecraft.getInstance().getRenderViewEntity() == livingEntity
		) {
			if (!RealFirstPerson.renderHelm) {
				ci.cancel();
			}
		}
	}
}
