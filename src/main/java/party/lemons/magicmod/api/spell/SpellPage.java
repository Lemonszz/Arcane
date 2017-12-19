package party.lemons.magicmod.api.spell;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Sam on 10/12/2017.
 */
public class SpellPage
{
	private ItemStack displayStack;
	private String name;

	public SpellPage(ItemStack stack, String name)
	{
		this.displayStack = stack;
		this.name = name;

		SpellPages.pages.add(this);
	}

	public ItemStack getDisplayStack()
	{
		return displayStack;
	}

	public String getUnlocalisedName()
	{
		return "page." + name + ".name";
	}

	@SideOnly(Side.CLIENT)
	public String getLocalizedName()
	{
		return I18n.format(getUnlocalisedName());
	}
}
