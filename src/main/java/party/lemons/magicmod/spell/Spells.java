package party.lemons.magicmod.spell;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import party.lemons.magicmod.api.spell.Spell;
import party.lemons.magicmod.api.spell.SpellPage;
import party.lemons.magicmod.api.spell.SpellPages;
import party.lemons.magicmod.config.ModConstants;

/**
 * Created by Sam on 11/12/2017.
 */
@Mod.EventBusSubscriber(modid = ModConstants.MODID)
public class Spells
{
	public static Spell fireball_1, fireball_2, fireball_3, fireball_4, fireball_5,
						rock_throw, earth_2, earth_3, earth_4, earth_5,
						leaping,
						fertilizer, photosynthesis;

	@SubscribeEvent
	public static void registerSpells(RegistryEvent.Register<Spell> event)
	{
		fireball_1 = new SpellFlame(SpellPages.ELEMENTAL, 5, 0, 3).setCastMana(0.1F).setUnlocalizedName("fireball_1").setRegistryName("fireball_1");
		fireball_2 = new SpellFlame(SpellPages.ELEMENTAL, 8, 1, 8).setUnlockCost(2).setCastMana(0.1F).setUnlocalizedName("fireball_2").setRegistryName("fireball_2").setParents(fireball_1);
		fireball_3 = new SpellSmallFireball(SpellPages.ELEMENTAL).setCastMana(25).setUnlockCost(2).setDrawStack(new ItemStack(Items.FIRE_CHARGE)).setUnlocalizedName("fireball_3").setRegistryName("fireball_3").setParents(fireball_2);
		fireball_4 = new SpellLargeFireball(SpellPages.ELEMENTAL).setCastMana(60).setUnlockCost(3).setUnlocalizedName("fireball_4").setRegistryName("fireball_4").setParents(fireball_3);
		fireball_5 = new SpellDragonFireball(SpellPages.ELEMENTAL).setCastMana(150).setUnlockCost(8).setUnlocalizedName("fireball_5").setRegistryName("fireball_5").setParents(fireball_4);

		rock_throw = new SpellRockThrow(SpellPages.ELEMENTAL, 3).setUnlockCost(2).setCastMana(40).setUnlocalizedName("rock_throw").setRegistryName("rock_throw");
		earth_2 = new Spell(SpellPages.ELEMENTAL).setUnlocalizedName("earth_2").setRegistryName("earth_2").setParents(rock_throw);
		earth_3 = new Spell(SpellPages.ELEMENTAL).setUnlocalizedName("earth_3").setRegistryName("earth_3").setParents(earth_2);
		earth_4 = new Spell(SpellPages.ELEMENTAL).setUnlocalizedName("earth_4").setRegistryName("earth_4").setParents(earth_3);
		earth_5 = new Spell(SpellPages.ELEMENTAL).setUnlocalizedName("earth_5").setRegistryName("earth_5").setParents(earth_4);
		leaping = new SpellLeap(SpellPages.ELEMENTAL, 1).setCastMana(20).setUnlocalizedName("leaping").setRegistryName("leaping");
		fertilizer = new SpellBonemeal(SpellPages.FARMING, 1).setDrawStack(new ItemStack(Items.DYE, 1, 15)).setCastMana(40).setUnlocalizedName("fertilizer").setRegistryName("fertilizer");
		photosynthesis = new SpellBonemeal(SpellPages.FARMING, 5).setUnlockCost(4).setDrawStack(new ItemStack(Blocks.DOUBLE_PLANT, 1, 0)).setCastMana(90).setUnlocalizedName("photosynthesis").setRegistryName("photosynthesis").setParents(fertilizer);

		event.getRegistry().registerAll(
				fireball_1,
				fireball_2,
				fireball_3,
				fireball_4,
				fireball_5,
				rock_throw,
				earth_2,
				earth_3,
				earth_4,
				earth_5,
				leaping,
				fertilizer,
				photosynthesis
		);
	}
}
