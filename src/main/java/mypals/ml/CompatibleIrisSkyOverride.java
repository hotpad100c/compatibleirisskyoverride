package mypals.ml;

import com.mojang.blaze3d.systems.RenderSystem;
import com.moulberry.flashback.state.EditorState;
import com.moulberry.flashback.state.EditorStateManager;
import com.moulberry.flashback.visuals.ReplayVisuals;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Unique;

import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;

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

	public static void render(BufferBuilderStorage bufferBuilderStorage, float[] skyColour) {

		float a = 1.0f;

		BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS,
				VertexFormats.POSITION_COLOR);

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
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F)
					.color(skyColour[0], skyColour[1], skyColour[2], a);
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F)
					.color(skyColour[0], skyColour[1], skyColour[2], a);
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F)
					.color(skyColour[0], skyColour[1], skyColour[2], a);
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F)
					.color(skyColour[0], skyColour[1], skyColour[2], a);
		}
		//RenderLayer.getDebugSectionQuads().draw(bufferBuilder.end());

		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
	}
}