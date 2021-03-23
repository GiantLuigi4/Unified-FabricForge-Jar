package com.tfc.realfirstperson.fabric.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Environment(EnvType.CLIENT)
public class RealFirstPersonClient implements ClientModInitializer {
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public static JsonObject configJson;
	
	public static boolean renderHelm = false;
	public static boolean renderHeadware = false;
	public static boolean enabled = false;
	public static boolean trueCam = false;
	public static boolean useTranforms = false;
	public static float camDist = 0;
	
	private static long lastConfigModifyTime = 0;
	
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
	
	@Override
	public void onInitializeClient() {
	}
}
