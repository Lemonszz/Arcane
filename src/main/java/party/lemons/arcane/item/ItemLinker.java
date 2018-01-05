package party.lemons.arcane.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import party.lemons.arcane.block.tileentity.TileEntityManaHolder;
import party.lemons.arcane.block.tileentity.TileEntityManaNode;
import party.lemons.arcane.crafting.CreativeTabMagic;

/**
 * Created by Sam on 3/01/2018.
 */
public class ItemLinker extends Item
{
	public ItemLinker()
	{
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabMagic.tab);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);

		NBTTagCompound tags = stack.getTagCompound();
		TileEntity tileAtPos = world.getTileEntity(pos);

		if(tags == null || !tags.hasKey("pos"))
		{
			if(tileAtPos != null)
			{
				if(tileAtPos instanceof TileEntityManaNode)
				{
					if(!((TileEntityManaNode)tileAtPos).hasMaxLinks())
					{
						if(player.isSneaking())
						{
							((TileEntityManaNode)tileAtPos).clearChildren();
							String message = "arcane.clearlink";
							player.sendStatusMessage(new TextComponentTranslation(message), true);
							return EnumActionResult.SUCCESS;
						}
						NBTTagCompound posTags = NBTUtil.createPosTag(pos);
						NBTTagCompound compound = new NBTTagCompound();
						compound.setTag("pos", posTags);
						stack.setTagCompound(compound);
						player.sendStatusMessage(new TextComponentTranslation("arcane.startlink"), true);

					}
					else
					{
						String message = "arcane.alreadylink";
						if(player.isSneaking())
						{
							((TileEntityManaNode)tileAtPos).clearChildren();
							message = "arcane.clearlink";
						}
						player.sendStatusMessage(new TextComponentTranslation(message), true);
					}
					return EnumActionResult.SUCCESS;
				}
			}

		}
		else
		{
			if(tileAtPos != null)
			{
				BlockPos linkerPos = NBTUtil.getPosFromTag(tags.getCompoundTag("pos"));
				TileEntity linkerTile = world.getTileEntity(linkerPos);

				if(linkerPos != null && linkerTile instanceof TileEntityManaHolder)
				{
					TileEntityManaNode link = (TileEntityManaNode) linkerTile;
					if(link.isValidLink(pos) && !link.hasLink(pos))
					{
						double distance = pos.getDistance(linkerPos.getX(), linkerPos.getY(), linkerPos.getZ());
						if(distance < 20)
						{
							if(distance >= 1)
							{
								((TileEntityManaNode) linkerTile).addLink(pos);
								player.sendStatusMessage(new TextComponentTranslation("arcane.endlink"), true);
								stack.setTagCompound(null);
								return EnumActionResult.SUCCESS;

							}
							else
							{
								player.sendStatusMessage(new TextComponentTranslation("arcane.shortlink"), true);

								//too short
								return EnumActionResult.SUCCESS;

							}
						}
						else
						{
							player.sendStatusMessage(new TextComponentTranslation("arcane.farlink"), true);

							//to long
							return EnumActionResult.SUCCESS;
						}
					}
					else
					{
						player.sendStatusMessage(new TextComponentTranslation("arcane.invalidlink"), true);

						stack.setTagCompound(null);
						return EnumActionResult.SUCCESS;

						//invalid link
					}

				}
				else
				{
					player.sendStatusMessage(new TextComponentTranslation("arcane.faillink"), true);

					//Fail link
					return EnumActionResult.SUCCESS;

				}
			}
			else
			{
				if(player.isSneaking())
				{
					stack.setTagCompound(null);
					player.sendStatusMessage(new TextComponentTranslation("arcane.clearlinker"), true);
				}
			}
		}
		return EnumActionResult.SUCCESS;
	}
}
