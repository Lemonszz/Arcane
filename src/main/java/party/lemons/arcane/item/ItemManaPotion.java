package party.lemons.arcane.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.client.gui.GuiManaBar;
import party.lemons.arcane.crafting.CreativeTabMagic;

/**
 * Created by Sam on 15/12/2017.
 */
public class ItemManaPotion extends ItemFood
{
	public ItemManaPotion()
	{
		super(0 ,0, false);
		this.setAlwaysEdible();
		this.setCreativeTab(CreativeTabMagic.tab);
	}

	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		ItemStack st = super.onItemUseFinish(stack, worldIn, entityLiving);
		if(entityLiving instanceof EntityPlayer)
			((EntityPlayer)entityLiving).inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));

		return st;
	}

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
	{
		PlayerData da = player.getCapability(PlayerData.CAPABILITY, null);
		float newMana = Math.min(da.getMaxMana(), da.getMana() + (da.getMaxMana() / 3));

		if(worldIn.isRemote)
			GuiManaBar.drawTime = 4000;

		da.setMana(newMana);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 25;
	}
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.DRINK;
	}

}
