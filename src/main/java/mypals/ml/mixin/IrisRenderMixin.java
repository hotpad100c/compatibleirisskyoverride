package mypals.ml.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.irisshaders.iris.pipeline.IrisRenderingPipeline;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;

@Debug(export = true)
@Mixin(value = IrisRenderingPipeline.class,remap = false)
public class IrisRenderMixin {
	/*@WrapMethod(method = "shouldRenderSkyDisc")
	private boolean shouldRenderSkyDisc(Operation<Boolean> original) {
		//return shouldKeepRenderSkyDisc() || original.call(); not working :(
	}*/
}