package dev.fabien2s.bookly.mixin.level;

import dev.fabien2s.bookly.mixin.entity.LocalPlayerAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "getRenderOffset(Lnet/minecraft/client/player/AbstractClientPlayer;F)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void getRenderOffset_adjustFirstPerson(AbstractClientPlayer player, float f, CallbackInfoReturnable<Vec3> cir) {
        if (shouldRenderNormally(player)) return;

        float yaw = player.getViewYRot(f);
        Vec3 direction = Vec3.directionFromRotation(0, yaw);
        cir.setReturnValue(direction.scale(-.333334f));
    }

    @Inject(method = "setModelProperties", at = @At("RETURN"))
    private void setModelProperties(AbstractClientPlayer player, CallbackInfo ci) {
        if (shouldRenderNormally(player)) return;

        player.yBodyRot = player.yHeadRot;

        PlayerModel<AbstractClientPlayer> model = getModel();
        model.head.visible = false;
    }

    private static boolean shouldRenderNormally(AbstractClientPlayer player) {
        if (!(player instanceof LocalPlayer)) {
            return true;
        }

        Minecraft minecraft = ((LocalPlayerAccessor) player).getMinecraft();
        Camera mainCamera = minecraft.gameRenderer.getMainCamera();
        return mainCamera.isDetached();
    }

}
