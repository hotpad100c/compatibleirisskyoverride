package mypals.ml.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.moulberry.flashback.Flashback;
import com.moulberry.flashback.state.EditorState;
import com.moulberry.flashback.state.EditorStateManager;
import com.moulberry.flashback.visuals.ReplayVisuals;
import mypals.ml.CompatibleIrisSkyOverride;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Handle;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(value = WorldRenderer.class, priority = 900)
public class LevelRendererMixin {
    @Shadow @Final private BufferBuilderStorage bufferBuilders;



    @Inject(method = {"method_62215"}, require = 1,
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFog" +
                            "(Lnet/minecraft/client/render/Fog;)V", shift = At.Shift.AFTER),
            cancellable = true)
    private void renderCustomSky(CallbackInfo ci) {
        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState != null) {
            ReplayVisuals visuals = editorState.replayVisuals;
            //CompatibleIrisSkyOverride.LOGGER.info("Checking if sky disc should be rendered: " + visuals.renderSky);
            if(!visuals.renderSky && IrisApi.getInstance().isShaderPackInUse()){
                render(visuals.skyColour);
                ci.cancel();
            }
        }
    }
    @Inject(method = "method_62212", require = 1,
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFog" +
                            "(Lnet/minecraft/client/render/Fog;)V", shift = At.Shift.AFTER),
            cancellable = true)
    private void renderCustomSky2(CallbackInfo ci) {
        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState != null) {
            ReplayVisuals visuals = editorState.replayVisuals;
            //CompatibleIrisSkyOverride.LOGGER.info("Checking if sky disc should be rendered: " + visuals.renderSky);
            if(!visuals.renderSky && IrisApi.getInstance().isShaderPackInUse()){
                render(visuals.skyColour);
                //ci.cancel();
            }
        }
    }
    @Unique
    public void render(float[] skyColour) {
        float a = 1.0F;
        RenderSystem.setShaderColor(skyColour[0], skyColour[1], skyColour[2], a);
        VertexConsumer vertexConsumer = this.bufferBuilders.getEntityVertexConsumers().getBuffer(RenderLayer.getSky());

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
        this.bufferBuilders.getEntityVertexConsumers().draw(RenderLayer.getSky());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
