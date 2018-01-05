package party.lemons.arcane.spell;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellPages;
import party.lemons.arcane.config.ArcaneConstants;

/**
 * Created by Sam on 11/12/2017.
 */
@Mod.EventBusSubscriber(modid = ArcaneConstants.MODID)
public class Spells
{
	public static Spell fireball_1, fireball_2, fireball_3, fireball_4, fireball_5,
						rock_throw, earth_wall, hole, seismic_slam, earth_5,
						leaping,
						recall,
						fertilizer, photosynthesis, growth_storm,
						summon_arrow_turret,
						mana_charge;

	@SubscribeEvent
	public static void registerSpells(RegistryEvent.Register<Spell> event)
	{
		fireball_1 = new SpellFlame(SpellPages.ELEMENTAL, 5, 0, 3)
				.setCastMana(0.1F)
				.setUnlocalizedName(ArcaneConstants.MODID + ".fireball_1")
				.setRegistryName("fireball_1");
		fireball_2 = new SpellFlame(SpellPages.ELEMENTAL, 8, 1, 8)
				.setUnlockCost(2)
				.setCastMana(0.1F).setUnlocalizedName(ArcaneConstants.MODID + ".fireball_2").setRegistryName("fireball_2").setParents(fireball_1);
		fireball_3 = new SpellSmallFireball(SpellPages.ELEMENTAL)
				.setCastMana(25)
				.setUnlockCost(2)
				.setDrawStack(new ItemStack(Items.FIRE_CHARGE))
				.setUnlocalizedName(ArcaneConstants.MODID + ".fireball_3")
				.setRegistryName("fireball_3")
				.setParents(fireball_2);
		fireball_4 = new SpellLargeFireball(SpellPages.ELEMENTAL)
				.setCastMana(60)
				.setUnlockCost(3)
				.setUnlocalizedName(ArcaneConstants.MODID + ".fireball_4")
				.setRegistryName("fireball_4")
				.setParents(fireball_3);
		fireball_5 = new SpellDragonFireball(SpellPages.ELEMENTAL)
				.setCastMana(150)
				.setUnlockCost(8)
				.setUnlocalizedName(ArcaneConstants.MODID + ".fireball_5")
				.setRegistryName("fireball_5")
				.setParents(fireball_4);
		rock_throw = new SpellRockThrow(SpellPages.ELEMENTAL, 3)
				.setUnlockCost(2)
				.setCastMana(40)
				.setUnlocalizedName(ArcaneConstants.MODID + ".rock_throw")
				.setRegistryName("rock_throw");
		earth_wall = new SpellEarthWall(SpellPages.ELEMENTAL)
				.setUnlockCost(2)
				.setCastMana(35)
				.setUnlocalizedName(ArcaneConstants.MODID + ".earth_wall")
				.setRegistryName("earth_wall").setParents(rock_throw);
		hole = new SpellEmergencyHole(SpellPages.ELEMENTAL)
				.setCastMana(30)
				.setDrawStack(new ItemStack(Items.STONE_SHOVEL))
				.setUnlocalizedName(ArcaneConstants.MODID + ".hole")
				.setRegistryName("hole");
		seismic_slam = new Spell(SpellPages.ELEMENTAL)
				.setUnlockCost(4)
				.setCastMana(50)
				.setUnlocalizedName(ArcaneConstants.MODID + ".seismic_slam")
				.setRegistryName("seismic_slam")
				.setParents(earth_wall);
		earth_5 = new Spell(SpellPages.ELEMENTAL)
				.setUnlocalizedName(ArcaneConstants.MODID + ".earth_5")
				.setRegistryName("earth_5")
				.setParents(seismic_slam);
		leaping = new SpellLeap(SpellPages.ELEMENTAL, 1)
				.setCastMana(20)
				.setUnlocalizedName(ArcaneConstants.MODID + ".leaping")
				.setRegistryName("leaping");
		fertilizer = new SpellBonemeal(SpellPages.FARMING, 1)
				.setDrawStack(new ItemStack(Items.DYE, 1, 15))
				.setCastMana(40)
				.setUnlocalizedName(ArcaneConstants.MODID + ".fertilizer")
				.setRegistryName("fertilizer");
		photosynthesis = new SpellBonemeal(SpellPages.FARMING, 5)
				.setUnlockCost(4)
				.setDrawStack(new ItemStack(Blocks.DOUBLE_PLANT, 1, 0))
				.setCastMana(90)
				.setUnlocalizedName(ArcaneConstants.MODID + ".photosynthesis")
				.setRegistryName("photosynthesis").setParents(fertilizer);
		growth_storm = new SpellBonemeal(SpellPages.FARMING, 25)
				.setUnlockCost(10)
				.setDrawStack(new ItemStack(Blocks.FARMLAND))
				.setCastMana(150)
				.setUnlocalizedName(ArcaneConstants.MODID + ".growth_storm")
				.setRegistryName("growthstorm")
				.setParents(photosynthesis);
		summon_arrow_turret = new SpellSummonArrowTurret(SpellPages.SUMMONING)
				.setUnlockCost(4)
				.setCastMana(90)
				.setUnlocalizedName(ArcaneConstants.MODID + ".arrow_turret")
				.setRegistryName("arrow_turret");
		recall = new SpellRecall(SpellPages.SUMMONING)
				.setUnlockCost(5)
				.setCastMana(120)
				.setUnlocalizedName(ArcaneConstants.MODID + ".recall")
				.setRegistryName("recall");
		mana_charge = new SpellManaCharge(SpellPages.ALCHEMY)
				.setUnlockCost(1)
				.setCastMana(10)
				.setUnlocalizedName(ArcaneConstants.MODID + ".mana_charge")
				.setRegistryName("mana_charge");

		event.getRegistry().registerAll(
				fireball_1,
				fireball_2,
				fireball_3,
				fireball_4,
				fireball_5,
				rock_throw,
				earth_wall,
				hole,
				seismic_slam,
				earth_5,
				leaping,
				fertilizer,
				photosynthesis,
				growth_storm,
				summon_arrow_turret,
				recall,
				mana_charge
		);
	}
}
