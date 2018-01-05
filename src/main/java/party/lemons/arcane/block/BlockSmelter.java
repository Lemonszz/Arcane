package party.lemons.arcane.block;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import party.lemons.arcane.block.tileentity.TileEntitySmelter;
import party.lemons.arcane.config.ArcaneConstants;
import party.lemons.arcane.crafting.CreativeTabMagic;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sam on 5/01/2018.
 */
public class BlockSmelter extends BlockContainer
{
	protected static final AxisAlignedBB[] AABB_WALLS = {
					new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.43D, 0.1D),
					new AxisAlignedBB(0.0D, 0.0D, 0.9D, 1.0D, 0.43D, 1.0D),
					new AxisAlignedBB(0.9D, 0.0D, 0.0D, 1.0D, 0.43D, 1.0D),
					new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.10D, 0.43D, 1.0D)
	};
	protected static final AxisAlignedBB AABB_SELECT = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1, 0.43D, 1.0D);

	public BlockSmelter()
	{
		super(Material.ROCK);

		this.setUnlocalizedName(ArcaneConstants.MODID + ".smelter");
		this.setRegistryName("smelter");
		this.setCreativeTab(CreativeTabMagic.tab);

		ArcaneBlocks.blocks.add(this);
	}

	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		final int smeltCost = 25;
		float f = (float)pos.getY() + (6.0F / 16.0F);

		if (!worldIn.isRemote && entityIn.getEntityBoundingBox().minY <= (double)f && entityIn.ticksExisted > 5)
		{
			entityIn.setFire(5);
			if(entityIn instanceof EntityItem)
			{
				EntityItem entityItem = (EntityItem) entityIn;
				TileEntitySmelter smelter = (TileEntitySmelter) worldIn.getTileEntity(pos);
				ItemStack entityStack = entityItem.getItem();

				double xP = entityItem.posX;
				double yP = entityItem.posY;
				double zP = entityItem.posZ;

				if(!entityStack.isEmpty())
				{
					ItemStack result = FurnaceRecipes.instance().getSmeltingResult(entityItem.getItem());
					while(canSmelt(entityStack, smelter, smeltCost) && !result.isEmpty())
					{
						EntityItem it = new EntityItem(worldIn, xP, yP, zP,  result.copy());
						it.motionY = 0;
						entityStack.shrink(1);

						smelter.getMana().extractEnergy(smeltCost, false);

						worldIn.spawnEntity(it);
					}
				}
			}
		}
	}

	private boolean canSmelt(ItemStack stack, TileEntitySmelter smelter, int smeltCost)
	{
		if(!stack.isEmpty())
		{
			if(smelter.getMana().extractEnergy(smeltCost, true) == smeltCost)
			{
				return true;
			}
		}
		return false;
	}

	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
	{
		Arrays.stream(AABB_WALLS).forEach((b) -> addCollisionBoxToList(pos, entityBox, collidingBoxes, b));
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB_SELECT;
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntitySmelter();
	}

	@Nullable
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end)
	{
		List<RayTraceResult> list = Lists.<RayTraceResult>newArrayList();
		Arrays.stream(AABB_WALLS).forEach((b) -> list.add(this.rayTrace(pos, start, end, b)));

		RayTraceResult raytraceresult1 = null;
		double d1 = 0.0D;

		for (RayTraceResult raytraceresult : list)
		{
			if (raytraceresult != null)
			{
				double d0 = raytraceresult.hitVec.squareDistanceTo(end);

				if (d0 > d1)
				{
					raytraceresult1 = raytraceresult;
					d1 = d0;
				}
			}
		}

		return raytraceresult1;
	}
}
