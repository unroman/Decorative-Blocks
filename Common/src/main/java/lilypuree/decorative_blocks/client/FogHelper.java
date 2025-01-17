package lilypuree.decorative_blocks.client;

import com.mojang.blaze3d.systems.RenderSystem;
import lilypuree.decorative_blocks.core.DBTags;
import lilypuree.decorative_blocks.fluid.ThatchFluid;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class FogHelper {

    public static Info onColorFog(Camera camera) {
        Entity entity = camera.getEntity();
        if (entity.isEyeInFluid(DBTags.Fluids.THATCH)) {

            Level world = entity.getCommandSenderWorld();
            Vec3 pos = entity.getEyePosition();
            FluidState state = world.getFluidState(new BlockPos(new Vec3i((int) pos.x,(int) pos.y, (int) pos.z)));
            if (state.getType() instanceof ThatchFluid thatchFluid) {
                int color = thatchFluid.getReferenceHolder().color();
                return decodeColor(color);
            }
        }
        return null;
    }

    public static Info decodeColor(int color) {
        float fogRed = ((float) (color >> 16 & 0xFF) / 0xFF);
        float fogGreen = ((float) ((color >> 8) & 0xFF) / 0xFF);
        float fogBlue = ((float) (color & 0xFF) / 0xFF);
        long biomeChangedTime = -1L;

        return new Info(fogRed, fogGreen, fogBlue, biomeChangedTime);
    }

    public static record Info(float fogRed, float fogGreen, float fogBlue, long time) {

    }

    public static void onFogSetup(Entity entity, float farPlaneDist) {
        float start;
        float end;

        if (entity.isSpectator()) {
            start = -8.0F;
            end = farPlaneDist * 0.5F;
        } else {
            start = 0.25F;
            end = 1.0F;
        }
        RenderSystem.setShaderFogStart(start);
        RenderSystem.setShaderFogEnd(end);
    }
}
