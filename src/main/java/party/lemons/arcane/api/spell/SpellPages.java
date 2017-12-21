package party.lemons.arcane.api.spell;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import party.lemons.arcane.config.ArcaneConstants;

import java.util.ArrayList;

/**
 * Created by Sam on 10/12/2017.
 */
public class SpellPages
{
	public static ArrayList<SpellPage> pages = new ArrayList<>();

	public static SpellPage ELEMENTAL = new SpellPage(new ItemStack(Items.BLAZE_POWDER), ArcaneConstants.MODID + ".elemental");
	public static SpellPage FARMING = new SpellPage(new ItemStack(Items.IRON_HOE), ArcaneConstants.MODID + ".farming");
	public static SpellPage SUMMONING = new SpellPage(new ItemStack(Items.SPAWN_EGG), ArcaneConstants.MODID + ".summoning");
	public static SpellPage ALCHEMY = new SpellPage(new ItemStack(Items.GOLD_NUGGET), ArcaneConstants.MODID + ".alchemy");
}
