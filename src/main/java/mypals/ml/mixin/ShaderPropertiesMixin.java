package mypals.ml.mixin;

import com.moulberry.flashback.state.EditorState;
import com.moulberry.flashback.state.EditorStateManager;
import com.moulberry.flashback.visuals.ReplayVisuals;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.helpers.OptionalBoolean;
import net.irisshaders.iris.shaderpack.properties.ShaderProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShaderProperties.class)
public class ShaderPropertiesMixin {
    @Inject(method = "getSky", at = @At("HEAD"), cancellable = true, remap = false)
    private void getSky(CallbackInfoReturnable<OptionalBoolean> cir) {
        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState != null) {
            ReplayVisuals visuals = editorState.replayVisuals;
            if(!visuals.renderSky && IrisApi.getInstance().isShaderPackInUse()){
                cir.setReturnValue(OptionalBoolean.FALSE);
            }
        }
    }
    @Inject(method = "getCloudSetting", at = @At("HEAD"), cancellable = true, remap = false)
    private void getCloudSetting(CallbackInfoReturnable<OptionalBoolean> cir) {
        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState != null) {
            ReplayVisuals visuals = editorState.replayVisuals;
            if(!visuals.renderSky && IrisApi.getInstance().isShaderPackInUse()){
                cir.setReturnValue(OptionalBoolean.FALSE);
            }
        }
    }
    @Inject(method = "getDHCloudSetting", at = @At("HEAD"), cancellable = true, remap = false)
    private void getDHCloudSetting(CallbackInfoReturnable<OptionalBoolean> cir) {
        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState != null) {
            ReplayVisuals visuals = editorState.replayVisuals;
            if(!visuals.renderSky && IrisApi.getInstance().isShaderPackInUse()){
                cir.setReturnValue(OptionalBoolean.FALSE);
            }
        }
    }
}
