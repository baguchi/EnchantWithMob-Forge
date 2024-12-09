package baguchi.enchantwithmob.client;

import net.minecraft.client.renderer.ShaderInstance;
import org.jetbrains.annotations.Nullable;

public class ModShaders {
    private static ShaderInstance renderTypeEnchantBeamShader;

    @Nullable
    public static ShaderInstance getRenderTypeEnchantBeamShader() {
        return renderTypeEnchantBeamShader;
    }

    public static void setRenderTypeEnchantBeamShader(ShaderInstance instance) {
        renderTypeEnchantBeamShader = instance;
    }
}
