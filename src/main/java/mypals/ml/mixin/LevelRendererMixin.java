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

import static mypals.ml.CompatibleIrisSkyOverride.render;

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
                render(this.bufferBuilders, visuals.skyColour);
                ci.cancel();
            }
        }
    }
}
