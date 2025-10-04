package mypals.ml.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.moulberry.flashback.state.EditorState;
import com.moulberry.flashback.state.EditorStateManager;
import com.moulberry.flashback.visuals.ReplayVisuals;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mypals.ml.CompatibleIrisSkyOverride.render;

@Debug(export = true)
@Mixin(value = WorldRenderer.class, priority = 1600)
public abstract class LevelRendererMixin {

    @Shadow public abstract void renderSky(Matrix4f matrix4f, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback);

    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @TargetHandler(
            mixin = "com.moulberry.flashback.mixin.MixinLevelRenderer",
            name = "renderLevel_renderSky"
    )
    @WrapMethod(
            method = "@MixinSquared:Handler"
    )
    private boolean modifyRenderSkyCondition(WorldRenderer instance, Matrix4f matrix4f, Matrix4f matrix4f2, float f, Camera camera, boolean bl, Runnable runnable, Operation<Boolean> original) {
        EditorState editorState = EditorStateManager.getCurrent();

        if(editorState != null && !editorState.replayVisuals.renderSky && IrisApi.getInstance().isShaderPackInUse()) {
            return true;
        }else{
            return original.call(instance, matrix4f, matrix4f2, f, camera, bl, runnable);
        }
    }

    @Inject(method = {"renderSky"},at = @At(value = "HEAD"), cancellable = true)
    private void renderCustomSky(Matrix4f matrix4f, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback, CallbackInfo ci) {
        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState != null) {
            ReplayVisuals visuals = editorState.replayVisuals;
            if(!visuals.renderSky && IrisApi.getInstance().isShaderPackInUse()){
                render(this.bufferBuilders, visuals.skyColour);
                ci.cancel();
            }
        }
    }
}
