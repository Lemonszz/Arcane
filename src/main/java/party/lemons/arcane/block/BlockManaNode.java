package party.lemons.arcane.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import party.lemons.arcane.block.tileentity.TileEntityManaNode;
import party.lemons.arcane.item.ItemLinker;

/**
 * Created by Sam on 3/01/2018.
 */
public abstract class BlockManaNode extends BlockContainer
{
	protected BlockManaNode(Material mat)
	{
		super(Material.WOOD);

	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos position, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!playerIn.getHeldItem(hand).isEmpty() && playerIn.getHeldItem(hand).getItem() instanceof ItemLinker)
			return false;

		TileEntityManaNode node = (TileEntityManaNode) world.getTileEntity(position);
		String energy = node.getMana().getEnergyStored() + "/" + node.getMana().getMaxEnergyStored();

		if(!world.isRemote)
			playerIn.sendStatusMessage(new TextComponentString(energy), true);

		return true;
	}
}
