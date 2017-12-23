package party.lemons.arcane.spell;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellPage;
import party.lemons.arcane.config.ArcaneConfig;
import party.lemons.arcane.entity.EntityPhysicsBlock;
import party.lemons.arcane.misc.ArcaneUtils;

/**
 * Created by Sam on 16/12/2017.
 */
public class SpellEarthWall extends Spell
{
	double vel;

	public SpellEarthWall(SpellPage elemental)
	{
		super(elemental);
	}

	@Override
	public void castSpellPress(Spell spell, EntityPlayer player)
	{
		if(!canCast(player))
			return;

		if(!player.world.isRemote)
		{
			Vec3d vec3 = player.getPositionEyes(1F);
			Vec3d look = player.getLook(1.0F);
			Vec3d vec3b = vec3.addVector(look.x * getSpellReach(), look.y * getSpellReach(), look.z * getSpellReach());

			RayTraceResult ray = player.world.rayTraceBlocks(vec3, vec3b, false, true, false);
			if(ray != null)
			{
				EnumFacing facing = player.getHorizontalFacing();
				World world = player.world;

				int ii = -2;
				BlockPos pos = ray.getBlockPos().offset(facing.rotateAround(EnumFacing.Axis.Y), -2);

				while(ii < 3)
				{
					if(player.world.isAirBlock(pos.offset(EnumFacing.UP)) || world.mayPlace(Blocks.STONE, pos.offset(EnumFacing.UP), true, EnumFacing.UP, player))
					for(int i = 0; i < 3; i++)
					{
						BlockPos enPos = pos.offset(EnumFacing.DOWN, i);
						if(ArcaneConfig.isEarthBlock(world.getBlockState(enPos).getBlock()))
						{
							float yOffset = 0.24161F * (3 - i);
							IBlockState st = world.getBlockState(enPos);
							spawnPhysicsBlock(world, st, player, enPos.getX() + 0.5F, yOffset + (enPos.getY() + 0.5F), enPos.getZ() + 0.5F,  0.55F);
							world.setBlockToAir(enPos);

							if(i == 2)
							{
								IBlockState fillerState = ArcaneUtils.choose(world.rand,
										Blocks.STONE.getDefaultState(),
										Blocks.DIRT.getDefaultState(),
										Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE),
										Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE),
										Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE),
										Blocks.GRAVEL.getDefaultState()
								);
								spawnPhysicsBlock(world, fillerState, player, enPos.getX() + 0.5F, yOffset + (enPos.getY()), enPos.getZ() + 0.5F, 0.5F);
							}
						}
						else
						{
							break;
						}
					}
					pos = pos.offset(facing.rotateAround(EnumFacing.Axis.Y));
					ii++;
				}
			}
		}
		useMana(player);
	}

	public void spawnPhysicsBlock(World world, IBlockState state, EntityPlayer player, float x, float y, float z, float motionY)
	{
		new EntityPhysicsBlock(world, x,y,z, player).setNoClipSetting().setFired().setVelocity(0, motionY, 0).setState(state).spawn();
	}

	@Override
	public float getSpellReach()
	{
		return 6;
	}

}
