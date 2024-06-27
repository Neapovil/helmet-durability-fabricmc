package com.github.neapovil.helmetdurability.mixin;

import net.minecraft.util.Colors;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin
{
    @Shadow
    @Final
    private TextRenderer textRenderer;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"),
            method = "renderLabelIfPresent")
    protected void renderLabelIfPresent(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo ci)
    {
        if (!(entity instanceof PlayerEntity player))
        {
            return;
        }

        if (entity instanceof ClientPlayerEntity)
        {
            return;
        }

        if (text.getString().toLowerCase().contains(" health"))
        {
            return;
        }

        final ItemStack helmet = player.getInventory().getArmorStack(3);
        final int damage = helmet.getMaxDamage() - helmet.getDamage();

        final Text text1 = Text.literal("" + damage).setStyle(Style.EMPTY.withColor(helmet.getItemBarColor()).withBold(true));

        final MinecraftClient client = MinecraftClient.getInstance();
        float h = (float) -this.textRenderer.getWidth(text1) / 2;
        float g = client.options.getTextBackgroundOpacity(0.25f);
        final int j = (int) (g * 255.0f) << 24;
        final int y = -10;
        final boolean visible = !player.isSneaky();
        final Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        this.textRenderer.draw(text1, h, (float) y, 0x20FFFFFF, false, matrix4f, vertexConsumers, visible ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, j, light);

        if (visible)
        {
            this.textRenderer.draw(text1, h, (float) y, Colors.WHITE, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
        }
    }
}
