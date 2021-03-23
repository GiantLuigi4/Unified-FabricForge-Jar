package com.tfc.realfirstperson.forge.mixins;

import net.minecraft.client.renderer.ActiveRenderInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ActiveRenderInfo.class)
public interface CameraAccessor {
	@Accessor("thirdPerson")
	boolean RFP_isTrulyThirdPerson();
}
