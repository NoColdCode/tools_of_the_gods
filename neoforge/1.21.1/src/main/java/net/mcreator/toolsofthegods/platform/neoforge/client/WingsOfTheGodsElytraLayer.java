package net.mcreator.toolsofthegods.platform.neoforge.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import net.mcreator.toolsofthegods.client.WingsTextures;
import net.mcreator.toolsofthegods.logic.WingsFlightLogic;

/**
 * Always draws TOG wings on the back. While climbing (look-up / positive Y),
 * applies a wing-beat so they flap instead of staying rigid like vanilla elytra.
 */
public final class WingsOfTheGodsElytraLayer
	extends ElytraLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

	private final ElytraModel<AbstractClientPlayer> wingsModel;
	private final ModelPart leftWing;
	private final ModelPart rightWing;

	public WingsOfTheGodsElytraLayer(
		RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
		EntityModelSet models
	) {
		super(parent, models);
		ModelPart root = models.bakeLayer(ModelLayers.ELYTRA);
		this.wingsModel = new ElytraModel<>(root);
		this.leftWing = root.getChild("left_wing");
		this.rightWing = root.getChild("right_wing");
	}

	@Override
	public boolean shouldRender(ItemStack stack, AbstractClientPlayer entity) {
		ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
		return WingsFlightLogic.isWings(chest) || WingsFlightLogic.isWings(stack);
	}

	@Override
	public ResourceLocation getElytraTexture(ItemStack stack, AbstractClientPlayer entity) {
		ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
		if (WingsFlightLogic.isWings(chest)) {
			return WingsTextures.forStack(chest);
		}
		return WingsTextures.forStack(stack);
	}

	@Override
	public void render(
		PoseStack poseStack,
		MultiBufferSource buffer,
		int packedLight,
		AbstractClientPlayer player,
		float limbSwing,
		float limbSwingAmount,
		float partialTick,
		float ageInTicks,
		float netHeadYaw,
		float headPitch
	) {
		ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
		if (!shouldRender(chest, player)) {
			return;
		}

		poseStack.pushPose();
		poseStack.translate(0.0F, 0.0F, 0.125F);
		this.getParentModel().copyPropertiesTo(this.wingsModel);
		this.wingsModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		applyClimbFlap(player, chest, ageInTicks);

		ResourceLocation texture = getElytraTexture(chest, player);
		VertexConsumer consumer = ItemRenderer.getArmorFoilBuffer(
			buffer, RenderType.armorCutoutNoCull(texture), chest.hasFoil()
		);
		this.wingsModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
		poseStack.popPose();
	}

	/**
	 * Vanilla elytra stays rigid when {@code motion.y >= 0}. Beat the wings while climbing
	 * so look-up flight reads as flapping instead of a frozen glide pose.
	 */
	private void applyClimbFlap(AbstractClientPlayer player, ItemStack wings, float ageInTicks) {
		if (!WingsFlightLogic.isWings(wings)) {
			return;
		}
		WingsFlightLogic.Mode mode = WingsFlightLogic.getMode(wings);
		if (mode == WingsFlightLogic.Mode.CAPE) {
			return;
		}

		Vec3 motion = player.getDeltaMovement();
		boolean climbing = motion.y > 0.02d || (player.isFallFlying() && player.getXRot() < -12.0f);
		if (!climbing) {
			return;
		}

		// Open / dive-ready pose if somehow still folded while ascending.
		if (!player.isFallFlying()) {
			float openX = (float) (Math.PI / 9);
			float openZ = (float) (-Math.PI / 2);
			player.elytraRotX += (openX - player.elytraRotX) * 0.25f;
			player.elytraRotZ += (openZ - player.elytraRotZ) * 0.25f;
			this.leftWing.xRot = player.elytraRotX;
			this.leftWing.yRot = player.elytraRotY;
			this.leftWing.zRot = player.elytraRotZ;
			this.rightWing.yRot = -this.leftWing.yRot;
			this.rightWing.xRot = this.leftWing.xRot;
			this.rightWing.zRot = -this.leftWing.zRot;
		}

		float intensity = mode == WingsFlightLogic.Mode.ICARUS ? 0.55f : 0.35f;
		if (motion.y > 0.15d) {
			intensity += 0.15f;
		}
		float flap = Mth.sin(ageInTicks * 1.35f) * intensity;
		this.leftWing.zRot += flap;
		this.rightWing.zRot = -this.leftWing.zRot;
		this.leftWing.xRot += flap * 0.12f;
		this.rightWing.xRot = this.leftWing.xRot;
	}
}
