package party.lemons.arcane.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import party.lemons.arcane.block.tileentity.TileEntityManaNodeStandard;
import party.lemons.arcane.config.ArcaneConstants;
import party.lemons.arcane.crafting.CreativeTabMagic;

import javax.annotation.Nullable;

/**
 * Created by Sam on 5/01/2018.
 */
public class BlockManaNodeStandard extends BlockManaNode
{
	public BlockManaNodeStandard()
	{
		super(Material.WOOD);
		this.setUnlocalizedName(ArcaneConstants.MODID + ".mana_node");
		this.setRegistryName("mana_node");
		this.setCreativeTab(CreativeTabMagic.tab);

		ArcaneBlocks.blocks.add(this);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityManaNodeStandard();
	}
}
