package party.lemons.arcane.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellRegistry;
import party.lemons.arcane.crafting.CreativeTabMagic;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Sam on 12/12/2017.
 */
public class ItemSpellTome extends Item
{
	public ItemSpellTome()
	{
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabMagic.tab);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if(stack.getTagCompound() != null)
		{
			String sp = stack.getTagCompound().getString("spell");
			Spell spell = SpellRegistry.SPELL_REGISTRY.getValue(new ResourceLocation(sp));

			tooltip.add(TextFormatting.LIGHT_PURPLE + spell.getLocalizedName());
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		if(stack.getTagCompound() != null)
		{
			NBTTagCompound tags = stack.getTagCompound();
			String sp = tags.getString("spell");
			Spell spell = SpellRegistry.SPELL_REGISTRY.getValue(new ResourceLocation(sp));
			PlayerData data = playerIn.getCapability(PlayerData.CAPABILITY, null);
			if(!data.hasSpellUnlocked(spell) && data.canUnlockSpell(spell))
			{
				data.unlockSpell(spell);
				stack.shrink(1);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (this.isInCreativeTab(tab))
		{
			for(Spell sp : SpellRegistry.SPELL_REGISTRY.getValues())
			{
				NBTTagCompound tags = new NBTTagCompound();
				tags.setString("spell", sp.getRegistryName().toString());

				ItemStack stack = new ItemStack(this);
				stack.setTagCompound(tags);

				items.add(stack);
			}
		}
	}
	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		if(stack.getTagCompound() != null)
		{
			String sp = stack.getTagCompound().getString("spell");
			Spell spell = SpellRegistry.SPELL_REGISTRY.getValue(new ResourceLocation(sp));

			if(Minecraft.getMinecraft().player.hasCapability(PlayerData.CAPABILITY, null))
			{
				PlayerData data = Minecraft.getMinecraft().player.getCapability(PlayerData.CAPABILITY, null);
				return !data.hasSpellUnlocked(spell);
			}
		}
		return stack.isItemEnchanted();
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		if(stack.getTagCompound() != null)
		{
			String sp = stack.getTagCompound().getString("spell");
			Spell spell = SpellRegistry.SPELL_REGISTRY.getValue(new ResourceLocation(sp));

			return spell.getLocalizedName() + " " + I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
		}

		return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
	}
}
