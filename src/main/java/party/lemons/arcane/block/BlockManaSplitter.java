package party.lemons.arcane.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import party.lemons.arcane.block.tileentity.TileEntityManaNodeSplitter;
import party.lemons.arcane.config.ArcaneConstants;
import party.lemons.arcane.crafting.CreativeTabMagic;

import javax.annotation.Nullable;

/**
 * Created by Sam on 4/01/2018.
 */
public class BlockManaSplitter extends BlockManaNode
{
	public BlockManaSplitter()
	{
		super(Material.ROCK);
		this.setUnlocalizedName(ArcaneConstants.MODID + ".mana_splitter");
		this.setRegistryName("mana_splitter");
		this.setCreativeTab(CreativeTabMagic.tab);

		ArcaneBlocks.blocks.add(this);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityManaNodeSplitter();
	}

}