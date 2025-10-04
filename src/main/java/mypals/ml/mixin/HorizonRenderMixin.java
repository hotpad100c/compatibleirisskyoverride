package mypals.ml.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.moulberry.flashback.state.EditorState;
import com.moulberry.flashback.state.EditorStateManager;
import com.moulberry.flashback.visuals.ReplayVisuals;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.mixin.MixinGameRenderer;
import net.irisshaders.iris.pathways.HorizonRenderer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mypals.ml.CompatibleIrisSkyOverride.render;

@Mixin(HorizonRenderer.class)
public class HorizonRenderMixin {
    @WrapMethod(method = {"renderHorizon"},remap = false)
    private void renderCustomSky(Matrix4fc modelView, Matrix4fc projection, ShaderProgram shader, Operation<Void> original) {
        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState != null) {
            ReplayVisuals visuals = editorState.replayVisuals;
            if(!visuals.renderSky && IrisApi.getInstance().isShaderPackInUse()){

            }else{
                original.call(modelView, projection, shader);
            }
        }
    }
}
