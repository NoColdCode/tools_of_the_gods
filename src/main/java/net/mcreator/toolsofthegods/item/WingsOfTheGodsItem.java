package net.mcreator.toolsofthegods.item;

import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.mcreator.toolsofthegods.init.TogArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsModItems;
import net.mcreator.toolsofthegods.util.TraitSystem;
import net.mcreator.toolsofthegods.util.ToolProgressionHelper;

import java.util.List;

public class WingsOfTheGodsItem extends ArmorItem implements IItemExtension {
	public WingsOfTheGodsItem() {
		super(
			TogArmorMaterials.AERIAL,
			ArmorItem.Type.CHESTPLATE,
			new Item.Properties()
				.fireResistant()
				.rarity(net.minecraft.world.item.Rarity.EPIC)
				.durability(ArmorItem.Type.CHESTPLATE.getDurability(TogArmorMaterials.AERIAL.get().enchantmentValue()))
		);
	}


	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		InteractionResultHolder<ItemStack> upgrade = TogItemUtils.handleShiftUpgrade(world, player, hand);
		if (upgrade != null) {
			return upgrade;
		}
		return super.use(world, player, hand);
	}

	@Override
	public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
		return entity.getItemBySlot(EquipmentSlot.CHEST).is(ToolsOfTheGodsModItems.WINGS_OF_THE_GODS.get());
	}

	@Override
	public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
		if (!(entity instanceof Player player)) {
			return false;
		}
		if (player.level().getGameTime() % 20 == 0) {
			int xp = Math.max(1, Math.round(TraitSystem.getAerodynamicGlideXpMultiplier(stack)));
			ToolProgressionHelper.gainXp(player.level(), player.getX(), player.getY(), player.getZ(), player, stack, xp);
		}
		if (TraitSystem.hasTrait(stack, TraitSystem.Trait.AERODYNAMIC_I)
			|| TraitSystem.hasTrait(stack, TraitSystem.Trait.AERODYNAMIC_II)) {
			Vec3 motion = player.getDeltaMovement();
			if (motion.y < -0.1d) {
				player.setDeltaMovement(motion.x * 1.02d, motion.y * 0.98d, motion.z * 1.02d);
			}
		}
		return true;
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level world, Player player) {
		super.onCraftedBy(stack, world, player);
		TogItemUtils.onTogCrafted(stack, ToolProgressionHelper.ToolType.WINGS);
	}

	@Override
	public Component getName(ItemStack stack) {
		return TogItemUtils.togDisplayName(stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, list, flag);
		TogItemUtils.appendTogTooltip(stack, context, list, flag);
		list.add(Component.literal("§8Wear in chest slot to glide like elytra"));
	}
}
