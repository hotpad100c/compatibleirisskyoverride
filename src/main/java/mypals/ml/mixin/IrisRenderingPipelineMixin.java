package mypals.ml.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.moulberry.flashback.state.EditorState;
import com.moulberry.flashback.state.EditorStateManager;
import com.moulberry.flashback.visuals.ReplayVisuals;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.pipeline.IrisRenderingPipeline;
import net.irisshaders.iris.pipeline.WorldRenderingPhase;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(IrisRenderingPipeline.class)
public class IrisRenderingPipelineMixin {
    @WrapMethod(
            method = "setPhase",remap = false
    )
    private void modifySetPhase(WorldRenderingPhase phase, Operation<Void> original) {
        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState != null) {
            ReplayVisuals visuals = editorState.replayVisuals;
            if(!(!visuals.renderSky && IrisApi.getInstance().isShaderPackInUse() && isBlackListedPhase(phase))){
                original.call(phase);
            }
            else{
                original.call(WorldRenderingPhase.NONE);
            }
        }else{
            original.call(phase);
        }
    }
    @Unique
    private boolean isBlackListedPhase(WorldRenderingPhase phase){
        return phase == WorldRenderingPhase.SKY
                || phase == WorldRenderingPhase.CUSTOM_SKY
                || phase == WorldRenderingPhase.CLOUDS
                || phase == WorldRenderingPhase.VOID
                || phase == WorldRenderingPhase.SUN
                || phase == WorldRenderingPhase.STARS
                || phase == WorldRenderingPhase.MOON
                || phase == WorldRenderingPhase.SUNSET;
    }
}
