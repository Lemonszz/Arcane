package party.lemons.magicmod.crafting;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import party.lemons.magicmod.item.ModItems;

/**
 * Created by Sam on 13/12/2017.
 */
public class CreativeTabMagic extends CreativeTabs
{
	public static CreativeTabs tab = new CreativeTabMagic();

	public CreativeTabMagic()
	{
		super("magicmod");
	}

	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(ModItems.spellTome);
	}
}
