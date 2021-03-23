package com.tfc.realfirstperson.fabric.mixins;

import com.tfc.realfirstperson.fabric.client.RealFirstPersonClient;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Camera.class)
public class CameraMixin {
	@Shadow
	private boolean thirdPerson;
	
	/**
	 * @author
	 */
	@Overwrite
	public boolean isThirdPerson() {
		return RealFirstPersonClient.enabled || thirdPerson;
	}
}
