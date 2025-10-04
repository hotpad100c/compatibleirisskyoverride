package mypals.ml;

import com.mojang.blaze3d.systems.RenderSystem;
import com.moulberry.flashback.state.EditorState;
import com.moulberry.flashback.state.EditorStateManager;
import com.moulberry.flashback.visuals.ReplayVisuals;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompatibleIrisSkyOverride implements ModInitializer {
	public static final String MOD_ID = "compatibleirisskyoverride";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//LOGGER.info("Hello Fabric world!");
	}
	public static void render(BufferBuilderStorage bufferBuilderStorage,float[] skyColour) {
		float a = 1.0F;
		RenderSystem.setShaderColor(skyColour[0], skyColour[1], skyColour[2], a);
		VertexConsumer vertexConsumer = bufferBuilderStorage.getEntityVertexConsumers().getBuffer(RenderLayer.getSky());

		for (int i = 0; i < 6; i++) {
			Matrix4f matrix4f = new Matrix4f();
			switch (i) {
				case 1:
					matrix4f.rotationX((float) (Math.PI / 2));
					break;
				case 2:
					matrix4f.rotationX((float) (-Math.PI / 2));
					break;
				case 3:
					matrix4f.rotationX((float) Math.PI);
					break;
				case 4:
					matrix4f.rotationZ((float) (Math.PI / 2));
					break;
				case 5:
					matrix4f.rotationZ((float) (-Math.PI / 2));
			}

			vertexConsumer.vertex(matrix4f, -100.0F, -100.0F, -100.0F)
					.color(skyColour[0], skyColour[1], skyColour[2], a);
			vertexConsumer.vertex(matrix4f, -100.0F, -100.0F, 100.0F)
					.color(skyColour[0], skyColour[1], skyColour[2], a);
			vertexConsumer.vertex(matrix4f, 100.0F, -100.0F, 100.0F)
					.color(skyColour[0], skyColour[1], skyColour[2], a);
			vertexConsumer.vertex(matrix4f, 100.0F, -100.0F, -100.0F)
					.color(skyColour[0], skyColour[1], skyColour[2], a);
		}
		bufferBuilderStorage.getEntityVertexConsumers().draw(RenderLayer.getSky());
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
}