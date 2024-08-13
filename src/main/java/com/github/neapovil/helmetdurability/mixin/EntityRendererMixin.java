package com.github.neapovil.helmetdurability.mixin;

import com.github.neapovil.helmetdurability.HelmetDurability;
import com.llamalad7.mixinextras.sugar.Local;
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
import net.minecraft.util.Colors;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity>
{
    @Shadow
    @Final
    private TextRenderer textRenderer;

    @Inject(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I", ordinal = 0))
    protected void renderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, @Local boolean bl, @Local Matrix4f matrix4f, @Local(ordinal = 1) int j)
    {
        if (!HelmetDurability.INSTANCE.config().enabled)
        {
            return;
        }

        if (!(entity instanceof PlayerEntity playerentity))
        {
            return;
        }

        if (playerentity instanceof ClientPlayerEntity)
        {
            return;
        }

        if (!text.equals(playerentity.getDisplayName()))
        {
            return;
        }

        final ItemStack helmet = playerentity.getInventory().getArmorStack(3);
        final int damage = helmet.getMaxDamage() - helmet.getDamage();

        if (damage < 1)
        {
            return;
        }

        final Text text1 = Text.literal("" + damage).setStyle(Style.EMPTY.withColor(helmet.getItemBarColor()).withBold(true));
        float g = (float) (-this.textRenderer.getWidth(text1) / 2);

        this.textRenderer.draw(text1, g, (float) -10, 0x20FFFFFF, false, matrix4f, vertexConsumers, bl ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, j, light);

        if (bl)
        {
            this.textRenderer.draw(text1, g, (float) -10, Colors.WHITE, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
        }
    }
}
