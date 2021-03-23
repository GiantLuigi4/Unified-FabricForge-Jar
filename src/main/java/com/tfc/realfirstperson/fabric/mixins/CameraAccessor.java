package com.tfc.realfirstperson.fabric.mixins;

import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface CameraAccessor {
	@Accessor("thirdPerson")
	boolean RFP_isTrulyThirdPerson();
}
