package net.mcreator.toolsofthegods.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;

import net.mcreator.toolsofthegods.init.ToolsOfTheGodsOrbItems;
import net.mcreator.toolsofthegods.TogModConstants;
import net.mcreator.toolsofthegods.util.GuideBookOpener;

import java.util.ArrayList;
import java.util.List;

public class TogGuideBookItem extends Item {
	public TogGuideBookItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide() && player instanceof ServerPlayer sp) {
			GuideBookOpener.open(sp);
			player.awardStat(Stats.ITEM_USED.get(this));
		}
		return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player == null) {
			return InteractionResult.PASS;
		}
		InteractionResultHolder<ItemStack> result = use(context.getLevel(), player, context.getHand());
		return result.getResult();
	}

	/** Returns the guide pages as plain Components for the custom screen. */
	public static List<Component> getGuidePages() {
		List<Filterable<Component>> raw = buildPages();
		List<Component> result = new ArrayList<>(raw.size());
		for (Filterable<Component> f : raw) result.add(f.raw());
		return result;
	}

	public static ItemStack createPopulatedStack() {
		return new ItemStack(ToolsOfTheGodsOrbItems.TOG_GUIDE_BOOK.get());
	}

	private static List<Filterable<Component>> buildPages() {
		List<Filterable<Component>> pages = new ArrayList<>();

		// ── Page 1: Welcome ──────────────────────────────────────────────────
		pages.add(page(
			h("Tools of the Gods") + "\n\n" +
			"§7v" + TogModConstants.getDisplayVersion() + "§r\n\n" +
			"Welcome, adventurer!\n\n" +
			"This guide covers everything you need to master the Tools of the Gods:\n\n" +
			" • How to craft tools, armor & shield\n" +
			" • Tier progression\n" +
			" • Trait binding system\n" +
			" • Every trait explained\n\n" +
			"§7Given once on your first join to this world.§r\n\n" +
			"Turn the page for the index."
		));

		// ── Page 2: Index ────────────────────────────────────────────────────
		pages.add(Filterable.passThrough(buildIndex()));

		// ── Page 3: First Steps ──────────────────────────────────────────────
		pages.add(page(
			h("First Steps") + "\n\n" +
			"Your journey starts by crafting the §6Primal§r tool set from the crafting table.\n\n" +
			"Each tool levels up through use. Reach level 10, 20... to unlock a Tier upgrade.\n\n" +
			"Reach §aTier 9§r to craft the §dUltimate Tool of the Gods§r."
		));

		// ── Page 4–10: Tools ──────────────────────────────────────────────────
		pages.add(toolPage("Pickaxe",
			"§aBreaks stone and ores.§r\nGains XP from mining. Rare ores give much more XP.\n\n" +
			"§cCannot one-shot until Tier 5+.§r\nAt high tiers, mines layers instantly.\n\n" +
			"§7Shift+RClick: Tier upgrade§r",
			"Craft: Plank+WoodPickaxe+Stick",
			"aba / _c_ / _c_",
			"a=Plank  b=WoodPickaxe  c=Stick"
		));
		pages.add(toolPage("Hammer",
			"§aA heavy mining tool.\nBeats Pickaxe on stone damage.\nSupports 3×3 mine (Broad Touch).§r\n\n" +
			"§cSlower attack than Pickaxe.§r\n\n" +
			"§7Shift+RClick: Tier upgrade§r",
			"Craft: Planks + Sticks",
			"aba / _b_ / _b_",
			"a=Plank  b=Stick"
		));
		pages.add(toolPage("Axe",
			"§aChops logs and combat.\nApplies Poison/Freeze on hit (with traits).\nSupports Broad Touch for tree felling.§r\n\n" +
			"§cPenalty XP when mining soft blocks.§r\n\n" +
			"§7Shift+RClick: Tier upgrade§r",
			"Craft: Plank+WoodAxe+Stick",
			"ab / ac / _c",
			"a=Plank  b=WoodAxe  c=Stick"
		));
		pages.add(toolPage("Shovel",
			"§aDigs dirt, sand, gravel.\nSupports Broad Touch for area digging.§r\n\n" +
			"§cNo melee bonus.§r\nGains XP on each block broken.\n\n" +
			"§7Shift+RClick: Tier upgrade§r",
			"Craft: WoodShovel + Sticks",
			"a / b / b",
			"a=WoodShovel  b=Stick"
		));
		pages.add(toolPage("Hoe",
			"§aTills farmland and strips paths.\n§aGains XP on soil use.§r\n\n" +
			"§cLeast combat utility.§r\nPrimarily a farming tool.\n\n" +
			"§7Shift+RClick: Tier upgrade§r",
			"Craft: Plank+WoodHoe+Stick",
			"ab / _c / _c",
			"a=Plank  b=WoodHoe  c=Stick"
		));
		pages.add(toolPage("Sword",
			"§aCombat weapon.\nApplies Poison/Freeze on hit.\nGains XP per enemy hit (scales with mob type).§r\n\n" +
			"§7Shift+RClick: Tier upgrade§r",
			"Craft: WoodSword+Plank+Stick",
			"a / b / c",
			"a=WoodSword  b=Plank  c=Stick"
		));
		pages.add(toolPage("Bow",
			"§aRanged weapon.\nApplies Poison/Freeze on arrow hit.\nGains 2 XP per arrow fired.§r\n\n" +
			"§cMax Tier 4 (levels 0-50).§r\n§cLimited tier-trait access.§r\n\n" +
			"§7Shift+RClick: Tier upgrade§r",
			"Craft: Stick+String+Bow+Plank",
			"ab_ / acd / ab_",
			"a=Stick  b=String  c=Bow  d=Plank"
		));

		// ── Armor & Shield ───────────────────────────────────────────────────
		pages.add(page(
			h("Armor of the Gods") + "\n\n" +
			"§aFull iron-tier set that levels like tools.§r\nEach piece upgrades independently.\n\n" +
			"§6XP sources:§r\n" +
			"§7• Wearing§r — slow passive XP\n" +
			"§7• Taking hits§r — faster XP per piece\n\n" +
			"§6Defense§r scales with tier & level.\n" +
			"§7Shift+RClick§r any piece to tier-upgrade.\n\n" +
			"§7Craft each piece: Leather surrounding the matching leather armor piece§r"
		));
		pages.add(page(
			h("Shield of the Gods") + "\n\n" +
			"§aBlocks melee and arrows while raised.§r\n\n" +
			"§6Stats scale linearly with level:§r\n" +
			"§7Block§r 50% → 99%\n" +
			"§7Power§r 60% (Lv1) → 100%\n" +
			"§7Strain cap§r 4 → 50\n" +
			"§7Recovery§r 6s → 3s\n" +
			"§7Return§r 0% → 1% (Lv50) → 15%\n\n" +
			"§6XP:§r successful blocks.\n\n" +
			"§7Craft: Shield + Plank + Stick + White Gem§r\n\n" +
			"  [S][P]\n  [SC]\n   [C]\n\n" +
			"§8S=Shield P=Plank C=Stick§r"
		));
		pages.add(page(
			h("Armor & Shield Traits") + "\n\n" +
			"§6Works while worn / in off-hand:§r\n" +
			"Bulwark, Swiftstep, Purifying,\nSustaining, Scholar, Soulbound,\nSpeedy, Frenzy, Modifiable\n\n" +
			"§6Armor-only:§r\n" +
			"§bThorns§r (Cactus) — reflect damage\n" +
			"§bFireward§r (Magma) — fire resist\n\n" +
			"§6Shield-only:§r\n" +
			"§bGuardian§r (Iron Block) — block chance & power\n" +
			"§bRepulse§r (Piston) — knockback wave\n\n" +
			"§7Mining traits cannot bind to armor/shield.§r"
		));
		pages.add(page(
			h("Armor & Shield Binding") + "\n\n" +
			"§bCactus§r → Thorns (armor)\n" +
			"§bMagma Cream§r → Fireward I\n" +
			"§bBlaze Rod§r → Fireward II\n" +
			"§bIron Block§r → Guardian I\n" +
			"§bObsidian§r → Guardian II\n" +
			"§bPiston§r → Repulse (shield)\n\n" +
			"All §bgeneral traits§r from earlier pages\n" +
			"(Brick, Feather, Bread, etc.) also work\n" +
			"when bound at the Trait Smithing Table."
		));

		// ── Trait Smithing Table craft ──────────────────────────────────────
		pages.add(page(
			h("Trait Smithing Table") + "\n\n" +
			"The Trait Smithing Table lets you bind traits to your tools.\n\n" +
			"§6Craft:§r\n" +
			"III\nIPI\nIII\n\n" +
			"I = Iron Ingot\nP = Any Plank\n\n" +
			"§7Right-click the block to open the interface. Place your tool in slot 1 and the ingredient in slot 2, then press Apply.§r"
		));

		// ── Page 12: Binding ingredients ─────────────────────────────────────
		pages.add(page(
			h("Binding Ingredients") + "\n\n" +
			"Place a TOG tool + one ingredient in the Trait Smithing Table.\n\n" +
			"§bRedstone Block§r → Speedy\n" +
			"§bSpider Eye§r → Poison\n" +
			"§bBread§r → Sustaining\n" +
			"§bMagma Block§r → Autosmelt\n" +
			"§bNether Star§r → Soulbound\n" +
			"§bIron Ingot§r → Sharpy\n" +
			"§bDiamond§r → Modifiable\n" +
			"§bCopperIngot§r → Magnetic\n" +
			"§bString§r → Silky\n" +
			"§bIce§r → Freezy"
		));

		// ── Page 13: More binding ingredients ────────────────────────────────
		pages.add(page(
			h("Binding Ingredients II") + "\n\n" +
			"§bSugar§r → Momentum\n" +
			"§bSlime Ball§r → Broad Touch\n" +
			"§bBlaze Powder§r → Frenzy\n" +
			"§bAnvil§r → Titan\n" +
			"§bLapis Lazuli§r → Scholar\n" +
			"§bPhantom Mem.§r → Moonlit\n" +
			"§bFlint§r → Ranger (Bow)\n" +
			"§bEmerald§r → Bountiful\n" +
			"§bHopper§r → Scavenger\n" +
			"§bGlowstone§r → Purifying\n" +
			"§bFeather§r → Swiftstep\n" +
			"§bBrick§r → Bulwark"
		));

		// ── Extended-tool binding ingredients ─────────────────────────────────
		pages.add(page(
			h("Binding Ingredients III") + "\n\n" +
			"§6Fishing Rod§r\n" +
			"§bNautilus Shell§r → Angler I\n" +
			"§bHeart of the Sea§r → Angler II\n" +
			"§bCod§r → Reel I\n\n" +
			"§6Crossbow§r\n" +
			"§bSpectral Arrow§r → Marksman I\n" +
			"§bArrow§r → Marksman II\n" +
			"§bFirework Rocket§r → Quick Load I\n\n" +
			"§6Trident§r\n" +
			"§bEnder Pearl§r → Returning I\n" +
			"§bPrismarine Shard§r → Riptide I\n" +
			"§bPrismarine Crystals§r → Impaler I/II"
		));
		pages.add(page(
			h("Binding Ingredients IV") + "\n\n" +
			"§6Staff§r\n" +
			"§bAmethyst Shard§r → Arcane I/II\n" +
			"§bLightning Rod§r → Channeling I\n\n" +
			"§6Wings§r\n" +
			"§bGhast Tear§r → Aerodynamic I/II\n" +
			"§bTurtle Scute§r → Featherfall I\n\n" +
			"§6Ultimate Tool§r\n" +
			"§bCompass§r → Adaptive I (2 slots)\n\n" +
			"§7Requires extendedToolsEnabled in config.§r"
		));
		pages.add(page(
			h("Binding Ingredients V") + "\n\n" +
			"§bCobweb§r → Hemorrhage (sword/axe/spear…)\n" +
			"§bPaper§r → Volley (bow/crossbow)\n" +
			"§bWheat Seeds§r → Harvest I (hoe)\n" +
			"§bWheat§r → Harvest II\n" +
			"§bChain§r → Steadfast I (shield)\n" +
			"§bIron Nugget§r → Steadfast II\n" +
			"§bFire Charge§r → Searing (sword/bow)\n" +
			"§bGolden Apple§r → Vitality I (sword/spear…)\n" +
			"§bEnchanted Golden Apple§r → Vitality II\n\n" +
			"§bPacked Ice§r → Rime I (sword/bow)\n" +
			"§bBlue Ice§r → Rime II\n" +
			"§bBone§r → Executioner I (sword/axe/spear)\n" +
			"§bWither Skull§r → Executioner II\n" +
			"§bXP Bottle§r → Reaving I (sword/axe/bow)\n" +
			"§bEcho Shard§r → Reaving II\n" +
			"§bRabbit Foot§r → Stalker I (bow/crossbow)\n" +
			"§bSpider Eye§r → Stalker II\n" +
			"§bGold Nugget§r → Riposte I (shield)\n" +
			"§bGold Ingot§r → Riposte II\n" +
			"§bCobblestone§r → Crushing I (pick/hammer/shovel)\n" +
			"§bDeepslate§r → Crushing II\n" +
			"§bLapis§r → Ancient I\n" +
			"§bLapis Block§r → Ancient II"
		));

		// ── Wings progression ───────────────────────────────────────────────
		pages.add(page(
			h("Wings of the Gods") + "\n\n" +
			"Chest flight item. Same gem upgrades as tools;\n" +
			"looks and names change per tier.\n\n" +
			"§dTiers 1–2 Cape§r  Crow → Magpie\n" +
			"§7Slow fall, less fall damage§r\n\n" +
			"§dTiers 3–6 Elytra§r  Phantom → Scarlet Macaw\n" +
			"§7Glide (faster as you level)§r\n\n" +
			"§dTiers 7–10 Wings§r  Dark Spix → Allay → Gods\n" +
			"§7Look up to climb (Angel = Wings of the Gods)§r\n\n" +
			"§8Names: Crow Cape, Phantom Elytra, Allay Wings…§r\n" +
			"§8Textures by OhDeerDreamy (PMC)§r"
		));

		// ── Page 14: Gem progression overview ────────────────────────────────
		pages.add(page(
			h("Gem Progression") + "\n\n" +
			"Gems are used to upgrade your tool's Tier past level 10 boundaries.\n\n" +
			"§7T1§r 32 Cobblestone\n" +
			"§7T2§r White Gem\n" +
			"§7T3§r Yellow Gem\n" +
			"§7T4§r Purple Gem\n" +
			"§7T5§r Red Gem\n" +
			"§7T6§r Black Gem\n" +
			"§7T7§r Green Gem\n" +
			"§7T8§r Blue Gem\n" +
			"§7T9§r Universe Gem\n\n" +
			"§bShift+RClick§r with tool to upgrade."
		));

		// ── Pages 15–22: Gem crafts ────────────────────────────────────────
		pages.add(gemPage("White Gem", "T2 upgrade",
			"III\nIGI\nIII",
			"I=Iron Ingot\nG=Glass Pane"));
		pages.add(gemPage("Yellow Gem", "T3 upgrade",
			"GNG\nNCN\nGNG",
			"G=Gold Ingot\nN=Gold Nugget\nC=Clock"));
		pages.add(gemPage("Purple Gem", "T4 upgrade",
			"AGA\nGLG\nAGA",
			"A=Amethyst Shard\nG=Glass Block\nL=Lapis Lazuli"));
		pages.add(gemPage("Red Gem", "T5 upgrade",
			"aba\nbcb\naba",
			"a=Glowstone Dust\nb=Redstone Block\nc=Fire Charge"));
		pages.add(gemPage("Black Gem", "T6 upgrade",
			"aba\nbcb\naba",
			"a=Obsidian\nb=Lava Bucket\nc=Diamond"));
		pages.add(gemPage("Green Gem", "T7 upgrade",
			"EQE\nQEQ\nEQE",
			"E=Emerald\nQ=Quartz Block"));
		pages.add(gemPage("Blue Gem", "T8 upgrade",
			"DDD\nDED\nDDD",
			"D=Diamond\nE=Eye of Ender"));
		pages.add(gemPage("Universe Gem", "T9 upgrade",
			"WYP\nREB\nG_L",
			"W=White  Y=Yellow\nP=Purple  R=Red\nE=End Crystal  B=Black\nG=Green  L=Blue"));

		// ── Page 23: Ultimate Tool craft ──────────────────────────────────────
		pages.add(page(
			h("Ultimate Tool") + "\n\n" +
			"Combines all 7 tools + Universe Gem:\n\n" +
			"pas\nhuc\nbq_\n\n" +
			"p=Pickaxe  a=Axe\n" +
			"s=Sword  h=Hammer\n" +
			"u=UniverseGem  c=Shovel\n" +
			"b=Bow  q=Hoe\n\n" +
			"§aAll tool functions in one.\n§aUp to 8 trait slots at T9.§r"
		));

		// ── Page 24: Tier table ───────────────────────────────────────────────
		pages.add(page(
			h("Tiers & Speed") + "\n\n" +
			"§6Tier§r  §bName§r\n" +
			"T0  Primal Wooden\n" +
			"T1  Crude Stone\n" +
			"T2  Hewn Iron\n" +
			"T3  Gilded Gold\n" +
			"T4  Lapis-Touched\n" +
			"T5  Redstone-Forged\n" +
			"T6  Obsidian Runed\n" +
			"T7  Arcane Emerald\n" +
			"T8  Ethereal Diamond\n" +
			"T9  [Tool] of the Gods\n\n" +
			"§7Each tier unlocks more trait slots and mining power.§r"
		));

		// ── Page 25: Trait slots & harvest table ──────────────────────────────
		pages.add(page(
			h("Trait Slots by Tier") + "\n\n" +
			"Tier  Slots\n" +
			"  0     0\n" +
			"  1     1\n" +
			"  2     1\n" +
			"  3     2\n" +
			"  4     2\n" +
			"  5     3\n" +
			"  6     4\n" +
			"  7     4\n" +
			"  8     5\n" +
			"  9     6\n\n" +
			"§b+1 slot§r with Modifiable trait.\n" +
			"§7Ultimate Tool: 8 base slots at T9.§r"
		));

		// ── Page 26: Harvest tiers ────────────────────────────────────────────
		pages.add(page(
			h("Harvest Capability") + "\n\n" +
			"§6Tier 0§r  Wood-level\n" +
			"§6Tier 1§r  Stone-level\n" +
			"§6Tier 2+§r Iron-level\n" +
			"§6Tier 8+§r Diamond-level\n\n" +
			"§aOne-Shot blocks:§r\n" +
			"T5 Coal ores\n" +
			"T6 Stone, Copper\n" +
			"T7 Deepslate, Gold\n" +
			"T8 Iron, Redstone\n" +
			"T9 Diamond ores\n\n" +
			"§7XP scales with ore rarity.§r"
		));

		// ── Trait pages (grouped by family) ───────────────────────────────────
		// Speedy
		pages.add(traitGroupPage("Speed", "Redstone Block",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aAtk speed +20%§r\n§aMine speed +15%§r\n" +
				"§cAtk dmg -10%§r\n§cXP -10%§r",
				"§aRank II§r  2 slots\n" +
				"§aAtk speed +40%§r\n§aMine speed +30%§r\n" +
				"§cAtk dmg -15%§r\n§cXP -15%§r",
				"§aRank III§r  3 slots\n" +
				"§aAtk speed +65%§r\n§aMine speed +50%§r\n" +
				"§cAtk dmg -20%§r\n§cXP -20%§r"
			}
		));
		// Poison
		pages.add(traitGroupPage("Poison", "Fermented Spider Eye",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aPoison I on hit for 5s§r\n" +
				"§cAtk speed -20%§r\n\n" +
				"§7Tools: Axe, Sword, Bow§r",
				"§aRank II§r  2 slots\n" +
				"§aPoison I on hit for 10s§r\n" +
				"§cAtk speed -30%§r\n\n" +
				"§7Tools: Axe, Sword, Bow§r"
			}
		));
		// Sustaining
		pages.add(traitGroupPage("Sustaining", "Bread",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aFeeds +1 food/30s§r\n§a+1 saturation/20s§r\n" +
				"§cLoot -20%§r",
				"§aRank II§r  2 slots\n" +
				"§a+1 food/20s§r\n§a+2 saturation/15s§r\n" +
				"§cLoot -35%§r",
				"§aRank III§r  3 slots\n" +
				"§a+2 food/30s§r\n§a+2 saturation/10s§r\n" +
				"§cLoot -50%§r"
			}
		));
		// Autosmelt
		pages.add(traitGroupPage("Autosmelt", "Magma Block",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aSmelts drops from broken blocks automatically§r",
				"§aRank II§r  1 slot (replaces I)\n" +
				"§aAutosmelt, toggleable§r\nSneak+RClick to toggle on/off"
			}
		));
		// Silky
		pages.add(traitGroupPage("Silky", "String",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aAlways mines with Silk Touch§r\n\n" +
				"§7All tools except Bow§r",
				"§aRank II§r  1 slot (replaces I)\n" +
				"§aSilk Touch, toggleable§r\nSneak+RClick to switch mode"
			}
		));
		// Sharpy
		pages.add(traitGroupPage("Sharpy", "Iron Ingot",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a+2 flat damage§r\n" +
				"§cAtk speed -30%§r\n§cMine speed -30%§r\n\n" +
				"§7Axe, Sword only§r",
				"§aRank II§r  2 slots\n" +
				"§a+4 flat damage§r\n" +
				"§cAtk speed -50%§r\n§cMine speed -50%§r",
				"§aRank III§r  3 slots\n" +
				"§a+6 flat damage§r\n" +
				"§cAtk speed -80%§r\n§cMine speed -80%§r"
			}
		));
		// Freezy
		pages.add(traitGroupPage("Freezy", "Ice",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aSlowness I on hit for 10s§r\n" +
				"§c-0.5 flat damage§r\n\n" +
				"§7Axe, Sword, Bow§r",
				"§aRank II§r  2 slots\n" +
				"§aSlowness II on hit for 15s§r\n" +
				"§c-1 flat damage§r"
			}
		));
		// Momentum
		pages.add(traitGroupPage("Momentum", "Sugar",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aMine 50 blocks to reach§r\n§a+25% mine speed§r\n" +
				"§cResets on idle§r\n\n" +
				"§7Pickaxe, Hammer, Shovel, Axe§r",
				"§aRank II§r  2 slots\n" +
				"§aMine 40 blocks to reach§r\n§a+40% mine speed§r\n" +
				"§cResets on idle§r"
			}
		));
		// Broad Touch
		pages.add(traitGroupPage("Broad Touch", "Slime Ball",
			new String[]{
				"§aRank I§r  2 slots\n" +
				"§aPickaxe/Shovel: 3×3§r\n§aHammer: 5×5§r\n§aAxe: fell tree 12§r\n\n" +
				"§7Pickaxe, Hammer, Shovel, Axe§r",
				"§aRank II§r  4 slots\n" +
				"§aLarger area, mode select§r\n§aAxe: fell tree 64§r"
			}
		));
		// Frenzy
		pages.add(traitGroupPage("Frenzy", "Blaze Powder",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aAtk speed +10%§r\n§aMine speed +6%§r\n" +
				"§cAtk dmg -4%§r\n\n" +
				"§7Not for Bow§r",
				"§aRank II§r  2 slots\n" +
				"§aAtk speed +18%§r\n§aMine speed +12%§r\n" +
				"§cAtk dmg -8%§r",
				"§aRank III§r  3 slots\n" +
				"§aAtk speed +28%§r\n§aMine speed +18%§r\n" +
				"§cAtk dmg -12%§r"
			}
		));
		// Titan
		pages.add(traitGroupPage("Titan", "Anvil",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a+2 flat damage§r\n" +
				"§cAtk speed -15%§r\n§cMine speed -10%§r\n\n" +
				"§7Axe, Sword§r",
				"§aRank II§r  2 slots\n" +
				"§a+4 flat damage§r\n" +
				"§cAtk speed -30%§r\n§cMine speed -20%§r"
			}
		));
		// Scholar
		pages.add(traitGroupPage("Scholar", "Lapis Lazuli",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a+10% XP gain§r\n\n" +
				"§7All tools§r",
				"§aRank II§r  2 slots\n" +
				"§a+20% XP gain§r"
			}
		));
		// Moonlit
		pages.add(traitGroupPage("Moonlit", "Phantom Membrane",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aAt night: +12% mine speed§r\n\n" +
				"§7Pickaxe, Hammer, Shovel, Axe§r",
				"§aRank II§r  2 slots\n" +
				"§aAt night: +20% mine speed§r"
			}
		));
		// Ranger
		pages.add(traitGroupPage("Ranger", "Flint",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aBow handling bonus§r\n§a+5% XP gain§r\n\n" +
				"§7Bow, Ultimate only§r",
				"§aRank II§r  2 slots\n" +
				"§aBow handling bonus§r\n§a+10% XP gain§r"
			}
		));
		// Single-rank traits
		pages.add(page(
			h("Utility Traits") + "\n\n" +
			"§bSoulbound§r (Nether Star) 1 slot\n" +
			"§aKept on death§r\n\n" +
			"§bModifiable§r (Diamond) 0 slots\n" +
			"§a+1 trait slot§r\n\n" +
			"§bMagnetic§r (Copper) 1 slot\n" +
			"§aAttracts drops within 5 blocks§r\n\n" +
			"§bBountiful§r (Emerald) 1 slot\n" +
			"§aSmall chance to duplicate drops§r\n\n" +
			"§bScavenger§r (Hopper) 1 slot\n" +
			"§aAuto-collects drops to inventory§r"
		));
		pages.add(page(
			h("Passive Traits") + "\n\n" +
			"§bPurifying§r (Glowstone) 1 slot\n" +
			"§aPeriodically clears a negative effect§r\n\n" +
			"§bSwiftstep§r (Feather) 1 slot\n" +
			"§aGrants Speed I while held/worn§r\n\n" +
			"§bBulwark§r (Brick) 1 slot\n" +
			"§aGrants Resistance I while held/worn§r\n\n" +
			"§7Also active on armor (worn) and\nshield (off-hand).§r"
		));
		// Thorns
		pages.add(traitGroupPage("Thorns", "Cactus (armor only)",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aReflects 2 damage to melee attackers§r\nwhen you take a hit",
				"§aRank II§r  2 slots\n" +
				"§aReflects 5 damage§r"
			}
		));
		// Fireward
		pages.add(traitGroupPage("Fireward", "Magma Cream (armor only)",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aFire Resistance I while worn§r",
				"§aRank II§r  2 slots\n" +
				"§aFire Resistance II while worn§r\n\n" +
				"§7Upgrade with Blaze Rod§r"
			}
		));
		// Guardian
		pages.add(traitGroupPage("Guardian", "Iron Block (shield only)",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a+12% block chance§r\n§a+10% block power§r",
				"§aRank II§r  2 slots\n" +
				"§a+22% block chance§r\n§a+18% block power§r\n\n" +
				"§7Upgrade with Obsidian§r"
			}
		));

		// Repulse
		pages.add(traitGroupPage("Repulse", "Piston (shield only)",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aRight-click knockback wave§r\n" +
				"§7Disabled while sneaking§r"
			}
		));
		// Angler
		pages.add(traitGroupPage("Angler", "Nautilus Shell (fishing rod)",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aBetter fishing loot§r\n§a+10% fishing XP§r",
				"§aRank II§r  2 slots\n" +
				"§aMuch better loot§r\n§a+20% fishing XP§r\n\n" +
				"§7Upgrade with Heart of the Sea§r"
			}
		));
		// Reel
		pages.add(traitGroupPage("Reel", "Cod (fishing rod)",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aFish bite ~40% faster§r"
			}
		));
		// Marksman
		pages.add(traitGroupPage("Marksman", "Spectral Arrow (crossbow)",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a+1 bolt damage§r\n§a+5% crossbow XP§r",
				"§aRank II§r  2 slots\n" +
				"§a+2 bolt damage§r\n§a+10% crossbow XP§r\n\n" +
				"§7Upgrade with Arrow§r"
			}
		));
		// Quick Load
		pages.add(traitGroupPage("Quick Load", "Firework Rocket (crossbow)",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a25% faster crossbow reload§r"
			}
		));
		// Impaler / Returning / Riptide
		pages.add(traitGroupPage("Trident Traits", "Prismarine (trident)",
			new String[]{
				"§bReturning I§r (Ender Pearl) 1 slot\n" +
				"§aThrown trident returns faster§r",
				"§bRiptide I§r (Prismarine Shard) 1 slot\n" +
				"§a+50% thrown damage in rain/water§r",
				"§bImpaler I/II§r (Prismarine Crystals)\n" +
				"§a+2 / +4 trident damage§r"
			}
		));
		// Arcane / Channeling
		pages.add(traitGroupPage("Staff Traits", "Amethyst / Lightning Rod",
			new String[]{
				"§bArcane I§r  1 slot\n" +
				"§aStronger bolt, 20% shorter cooldown§r",
				"§bArcane II§r  2 slots\n" +
				"§aMuch stronger bolt, 35% shorter cooldown§r",
				"§bChanneling I§r (Lightning Rod) 1 slot\n" +
				"§aStaff bolts call lightning in storms§r"
			}
		));
		// Wings
		pages.add(traitGroupPage("Wings Traits", "Ghast Tear / Turtle Scute",
			new String[]{
				"§bAerodynamic I§r  1 slot\n" +
				"§aFaster glide, +50% glide XP§r",
				"§bAerodynamic II§r  2 slots\n" +
				"§aMuch faster glide, +100% glide XP§r",
				"§bFeatherfall I§r (Turtle Scute) 1 slot\n" +
				"§a50% less fall damage while worn§r"
			}
		));
		// Adaptive
		pages.add(traitGroupPage("Adaptive", "Compass (Ultimate Tool)",
			new String[]{
				"§aRank I§r  2 slots\n" +
				"§aAuto-selects sword, pickaxe, shovel, rod…§r\n" +
				"§7for the block or entity you target§r"
			}
		));
		// Hemorrhage
		pages.add(traitGroupPage("Hemorrhage", "Cobweb",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aWither I for 3s on hit§r\n\n" +
				"§7Sword, axe, spear, flail, trident§r",
				"§aRank II§r  2 slots\n" +
				"§aWither I for 6s on hit§r"
			}
		));
		// Volley
		pages.add(traitGroupPage("Volley", "Paper (bow/crossbow)",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a15% chance for an extra projectile§r\n" +
				"§7(75% damage)§r",
				"§aRank II§r  2 slots\n" +
				"§a30% chance§r"
			}
		));
		// Harvest
		pages.add(traitGroupPage("Harvest", "Wheat Seeds (hoe)",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a12% bonus crop drops§r",
				"§aRank II§r  2 slots\n" +
				"§a25% bonus crop drops§r\n\n" +
				"§7Upgrade with Wheat§r"
			}
		));
		// Steadfast
		pages.add(traitGroupPage("Steadfast", "Chain (shield)",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a25% less strain per block§r",
				"§aRank II§r  2 slots\n" +
				"§a50% less strain§r\n\n" +
				"§7Upgrade with Iron Nugget§r"
			}
		));
		// Searing
		pages.add(traitGroupPage("Searing", "Fire Charge",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aSets targets on fire 3s§r\n\n" +
				"§7Sword, bow, crossbow§r",
				"§aRank II§r  2 slots\n" +
				"§aBurns for 6s§r"
			}
		));
		// Vitality
		pages.add(traitGroupPage("Vitality", "Golden Apple",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aHeal 1 heart on kill§r\n\n" +
				"§7Sword, spear, flail, trident§r",
				"§aRank II§r  2 slots\n" +
				"§aHeal 2 hearts on kill§r\n\n" +
				"§7Upgrade with Enchanted Golden Apple§r"
			}
		));
		pages.add(traitGroupPage("Rime", "Packed Ice",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aSlowness I on hit§r\n" +
				"§a+15% damage vs burning foes§r\n\n" +
				"§7Sword, bow§r",
				"§aRank II§r  2 slots\n" +
				"§aSlowness II on hit§r\n" +
				"§a+30% damage vs burning foes§r\n\n" +
				"§7Upgrade with Blue Ice§r"
			}
		));
		pages.add(traitGroupPage("Executioner", "Bone",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a+25% damage below 35% HP§r\n\n" +
				"§7Sword, axe, spear§r",
				"§aRank II§r  2 slots\n" +
				"§a+50% damage below 50% HP§r\n\n" +
				"§7Upgrade with Wither Skull§r"
			}
		));
		pages.add(traitGroupPage("Reaving", "XP Bottle",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a+20% XP from kills§r\n\n" +
				"§7Sword, axe, bow§r",
				"§aRank II§r  2 slots\n" +
				"§a+40% XP from kills§r\n\n" +
				"§7Upgrade with Echo Shard§r"
			}
		));
		pages.add(traitGroupPage("Stalker", "Rabbit Foot",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a+20% projectile damage while sneaking§r\n\n" +
				"§7Bow, crossbow§r",
				"§aRank II§r  2 slots\n" +
				"§a+40% projectile damage while sneaking§r\n\n" +
				"§7Upgrade with Spider Eye§r"
			}
		));
		pages.add(traitGroupPage("Riposte", "Gold Nugget",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§aReflects 3 damage when blocking§r\n\n" +
				"§7Shield only§r",
				"§aRank II§r  2 slots\n" +
				"§aReflects 6 damage when blocking§r\n\n" +
				"§7Upgrade with Gold Ingot§r"
			}
		));
		pages.add(traitGroupPage("Crushing", "Cobblestone",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a+12% mining speed on stone§r\n\n" +
				"§7Pickaxe, hammer, shovel§r",
				"§aRank II§r  2 slots\n" +
				"§a+25% mining speed on stone§r\n\n" +
				"§7Upgrade with Deepslate§r"
			}
		));
		pages.add(traitGroupPage("Ancient", "Lapis Lazuli",
			new String[]{
				"§aRank I§r  1 slot\n" +
				"§a+15% XP from broken blocks§r\n\n" +
				"§7Pickaxe, hammer, shovel§r",
				"§aRank II§r  2 slots\n" +
				"§a+30% XP from broken blocks§r\n\n" +
				"§7Upgrade with Lapis Block§r"
			}
		));

		// ── Trait synergies ────────────────────────────────────────────────────
		pages.add(page(
			h("Trait Synergies") + "\n\n" +
			"§6Bonus effects§r when specific trait combos\n" +
			"are bound on the §bsame tool§r.\n\n" +
			"Shown in the item tooltip under Synergies.\n\n" +
			"§bVenom Rush§r — Poison + Speedy\n" +
			"§a+6% attack speed§r\n\n" +
			"§bCold Steel§r — Freezy + Sharpy\n" +
			"§a+0.5 flat damage§r\n\n" +
			"§bField Cook§r — Sustaining + Autosmelt\n" +
			"§a+4% mining speed§r\n\n" +
			"§bKeeper's Reach§r — Soulbound + Magnetic\n" +
			"§a+4% mining speed§r\n\n" +
			"§bDeep Flow§r — Broad Touch + Momentum\n" +
			"§a+5% mining speed§r\n\n" +
			"§bSilk & Soul§r — Silky + Soulbound\n" +
			"§a+8% XP gain§r\n\n" +
			"§bFrozen Tempo§r — Freezy + Momentum\n" +
			"§a+4% mining speed§r"
		));
		pages.add(page(
			h("Trait Synergies II") + "\n\n" +
			"§bBerserker Script§r — Frenzy + Scholar\n" +
			"§a+4% attack speed§r\n\n" +
			"§bIron Pledge§r — Titan + Soulbound\n" +
			"§a+0.5 flat damage§r\n\n" +
			"§bRanger Tempo§r — Ranger + Frenzy\n" +
			"§a+3% attack speed§r\n\n" +
			"§bMooncraft§r — Moonlit + Scholar\n" +
			"§a+3% mining speed§r\n\n" +
			"§bTriune Focus§r — Frenzy + Titan + Scholar\n" +
			"§a+2% attack speed, +0.5 flat damage§r\n\n" +
			"§bNightfall Engine§r — Moonlit + Momentum\n+ Broad Touch\n" +
			"§a+3% mining speed§r\n\n" +
			"§bAlchemist Loop§r — Bountiful + Scavenger\n+ Autosmelt\n" +
			"§a+5% XP gain§r"
		));

		// ── Page: Guide craft reminder ─────────────────────────────────────────
		pages.add(page(
			h("This Book") + "\n\n" +
			"Lost your guide? Craft a new one:\n\n" +
			"  [ Book ]  +  [ Paper ]\n\n" +
			"§7Shapeless recipe — any slot in the crafting grid.§r\n\n" +
			"§7You receive one copy automatically the§r\n" +
			"§7first time you join this world.§r\n\n" +
			"Good luck, adventurer!"
		));

		return pages;
	}

	// ── Content helpers ────────────────────────────────────────────────────────

	/** Bold gold header */
	private static String h(String text) {
		return "§6§l" + text + "§r";
	}

	/** Wrap plain string in a Filterable page */
	private static Filterable<Component> page(String text) {
		return Filterable.passThrough(Component.literal(text));
	}

	/** One page per tool with description + ASCII craft grid */
	private static Filterable<Component> toolPage(String name, String desc, String craftLabel, String grid, String key) {
		String text = h(name) + "\n" +
			desc + "\n\n" +
			"§7--- " + craftLabel + " ---§r\n" +
			grid + "\n" +
			"§8" + key + "§r";
		return page(text);
	}

	/** Gem page with ASCII craft grid */
	private static Filterable<Component> gemPage(String name, String label, String grid, String key) {
		String text = h(name) + "\n§7" + label + "§r\n\n" +
			"§7--- Craft ---§r\n" +
			grid + "\n\n" +
			"§8" + key + "§r";
		return page(text);
	}

	/** Trait group page: ingredient + 2-3 rank descriptions */
	private static Filterable<Component> traitGroupPage(String name, String ingredient, String[] ranks) {
		StringBuilder sb = new StringBuilder();
		sb.append(h(name)).append('\n');
		sb.append("§7Ingredient: §b").append(ingredient).append("§r\n\n");
		for (int i = 0; i < ranks.length; i++) {
			if (i > 0) sb.append("\n\n");
			sb.append(ranks[i]);
		}
		return page(sb.toString());
	}

	private static MutableComponent buildIndex() {
		MutableComponent c = Component.literal("§6§lIndex§r\n\n");
		c.append(link("First Steps", 3));
		c.append(link("Pickaxe", 4));
		c.append(link("Hammer", 5));
		c.append(link("Axe", 6));
		c.append(link("Shovel", 7));
		c.append(link("Hoe", 8));
		c.append(link("Sword", 9));
		c.append(link("Bow", 10));
		c.append(link("Armor of the Gods", 11));
		c.append(link("Shield of the Gods", 12));
		c.append(link("Armor & Shield Traits", 13));
		c.append(link("Smithing Table", 15));
		c.append(link("Binding Ingredients", 16));
		c.append(link("Gem Progression", 18));
		c.append(link("Tiers & Speed", 28));
		c.append(link("All Traits", 31));
		c.append(link("Trait Synergies", 75));
		return c;
	}

	private static Component link(String label, int page) {
		return Component.literal("» " + label + "\n")
			.withStyle(style -> style
				.withColor(ChatFormatting.AQUA)
				.withUnderlined(true)
				.withClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, Integer.toString(page))));
	}
}
