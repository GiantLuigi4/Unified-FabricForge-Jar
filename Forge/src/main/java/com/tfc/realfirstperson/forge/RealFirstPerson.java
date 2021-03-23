package com.tfc.realfirstperson.forge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("realfirstperson")
public class RealFirstPerson {
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public static JsonObject configJson;
	
	public static boolean renderHelm = false;
	public static boolean renderHeadware = false;
	public static boolean enabled = false;
	public static boolean trueCam = false;
	public static boolean useTranforms = false;
	public static float camDist = 0;
	
	private static long lastConfigModifyTime = 0;
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public RealFirstPerson() {
		if (!FMLEnvironment.dist.isClient()) return;
		MinecraftForge.EVENT_BUS.addListener(RealFirstPerson::onRenderHand);
	}
	
	public static void onRenderHand(RenderHandEvent event) {
		if (enabled && event.isCancelable()) event.setCanceled(event.isCancelable());
	}
	
	static {
		File config = new File("config/realFirstPerson.json");
		try {
			if (!config.exists()) {
				config.getParentFile().mkdirs();
				config.createNewFile();
				enabled = true;
				trueCam = true;
				useTranforms = true;
				camDist = 0.2f;
			} else {
				updateConfigs();
			}
			JsonObject jsonObject = new JsonObject();
			addEntry(jsonObject, "renderHelmetInFirstPerson", String.valueOf(renderHelm));
			addEntry(jsonObject, "enabled", String.valueOf(enabled));
			addEntry(jsonObject, "trueCamera", String.valueOf(trueCam));
			addEntry(jsonObject, "useCameraTransforms", String.valueOf(useTranforms));
			addEntry(jsonObject, "cameraOffset", String.valueOf(camDist));
			addEntry(jsonObject, "renderHeadwearInFirstPerson", String.valueOf(renderHeadware));
			String text = gson.toJson(jsonObject);
			FileOutputStream stream = new FileOutputStream(config);
			stream.write(text.getBytes());
			IOUtils.closeQuietly(stream);
		} catch (Throwable err) {
		}
		updateConfigs();
	}
	
	private static void addEntry(JsonObject object, String name, String defaultValue) {
		if (!object.has(name)) {
			object.addProperty(name, defaultValue);
		}
	}
	
	public static void updateConfigs() {
		File config = new File("config/realFirstPerson.json");
		if (config.lastModified() > lastConfigModifyTime) {
			lastConfigModifyTime = config.lastModified();
			try {
				FileInputStream stream = new FileInputStream(config);
				byte[] bytes = new byte[stream.available()];
				stream.read(bytes);
				IOUtils.closeQuietly(stream);
				configJson = gson.fromJson(new String(bytes), JsonObject.class);
				trueCam = configJson.getAsJsonPrimitive("trueCamera").getAsBoolean();
				renderHelm = configJson.getAsJsonPrimitive("renderHelmetInFirstPerson").getAsBoolean();
				enabled = configJson.getAsJsonPrimitive("enabled").getAsBoolean();
				useTranforms = configJson.getAsJsonPrimitive("useCameraTransforms").getAsBoolean();
				renderHeadware = configJson.getAsJsonPrimitive("renderHeadwearInFirstPerson").getAsBoolean();
				camDist = configJson.getAsJsonPrimitive("cameraOffset").getAsFloat();
			} catch (Throwable err) {
			}
		}
	}
}
