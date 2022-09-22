package com.github.neapovil.armordurabilityother.mixin;

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
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin
{
    @Shadow
    @Final
    private TextRenderer textRenderer;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"),
            method = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    protected void renderLabelIfPresent(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci)
    {
        if (!(entity instanceof PlayerEntity))
        {
            return;
        }

        if (entity instanceof ClientPlayerEntity)
        {
            return;
        }

        if (text.getString().contains(" health"))
        {
            return;
        }

        final PlayerEntity player = (PlayerEntity) entity;

        final MinecraftClient client = MinecraftClient.getInstance();

        final ItemStack helmet = player.getInventory().getArmorStack(3);

        final int damage = helmet.getMaxDamage() - helmet.getDamage();

        if (damage == 0)
        {
            return;
        }

        final Text text1 = new LiteralText("" + (helmet.getMaxDamage() - helmet.getDamage())).setStyle(Style.EMPTY.withColor(0xFFFFFF).withBold(true));

        float h = -this.textRenderer.getWidth(text1) / 2;

        final Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        float g = client.options.getTextBackgroundOpacity(0.25f);
        int j = (int) (g * 255.0f) << 24;

        final boolean visible = !player.isSneaky();

        int y = -12;

        this.textRenderer.draw(text1, h, y, 0x20FFFFFF, false, matrix4f, vertexConsumers, visible, j, light);

        if (visible)
        {
            this.textRenderer.draw(text1, h, y, 0xFF0000, false, matrix4f, vertexConsumers, false, 0, light);
        }
    }
}
