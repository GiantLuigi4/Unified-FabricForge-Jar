package com.tfc.realfirstperson.forge.mixins;

import com.tfc.realfirstperson.forge.RealFirstPerson;
import net.minecraft.client.renderer.ActiveRenderInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ActiveRenderInfo.class)
public class CameraMixin {
	@Shadow
	private boolean thirdPerson;
	
	/**
	 * @author
	 */
	@Overwrite
	public boolean isThirdPerson() {
		return RealFirstPerson.enabled || thirdPerson;
	}
}
