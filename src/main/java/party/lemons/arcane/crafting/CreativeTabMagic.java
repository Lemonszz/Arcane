package party.lemons.arcane.crafting;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import party.lemons.arcane.item.ModItems;

/**
 * Created by Sam on 13/12/2017.
 */
public class CreativeTabMagic extends CreativeTabs
{
	public static CreativeTabs tab = new CreativeTabMagic();

	public CreativeTabMagic()
	{
		super("arcane");
	}

	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(ModItems.spellTome);
	}
}
