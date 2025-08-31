package mypals.ml.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.moulberry.flashback.state.EditorState;
import com.moulberry.flashback.state.EditorStateManager;
import com.moulberry.flashback.visuals.ReplayVisuals;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(value = WorldRenderer.class, priority = 2000)
public class LevelRendererMixin {
    @Shadow @Final private BufferBuilderStorage bufferBuilders;



    @Inject(method = {"render"}, require = 1,allow = 1,
    at = @At(value = "INVOKE",shift = At.Shift.BEFORE,
                    target = "Lnet/minecraft/client/render/WorldRenderer;renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V"))
    private void renderCustomSky(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState != null) {
            ReplayVisuals visuals = editorState.replayVisuals;
            //CompatibleIrisSkyOverride.LOGGER.info("Checking if sky disc should be rendered: " + visuals.renderSky);
            if(!visuals.renderSky && IrisApi.getInstance().isShaderPackInUse()){
                render(visuals.skyColour);
            }
        }
    }

    @Unique
    public void render(float[] skyColour) {
        float a = 1.0F;
        RenderSystem.setShaderColor(skyColour[0], skyColour[1], skyColour[2], a);
        BufferBuilder bufferBuilder = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
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
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
