package party.lemons.magicmod.spell;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import party.lemons.magicmod.api.spell.Spell;
import party.lemons.magicmod.api.spell.SpellPage;
import party.lemons.magicmod.config.ModConfig;
import party.lemons.magicmod.entity.EntityPhysicsBlock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
				IBlockState state = player.world.getBlockState(ray.getBlockPos());
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
						if(ModConfig.isEarthBlock(world.getBlockState(enPos).getBlock()))
						{
							float yOffset = (3 - i);
							IBlockState st = world.getBlockState(enPos);
							EntityPhysicsBlock block = new EntityPhysicsBlock(world, enPos.getX() + 0.5, yOffset + (enPos.getY() + 0.5), enPos.getZ() + 0.5, player);
							block.setState(st);
							block.getDataManager().set(EntityPhysicsBlock.FIRED, true);
							block.setVelocity(0, 0.8, 0);

							world.setBlockToAir(enPos);
							world.spawnEntity(block);

							if(i == 2)
							{
								block = new EntityPhysicsBlock(world, enPos.getX() + 0.5, yOffset + (enPos.getY()), enPos.getZ() + 0.5, player);
								block.setState(Blocks.STONE.getDefaultState());
								block.getDataManager().set(EntityPhysicsBlock.FIRED, true);
								block.setVelocity(0, 0.5, 0);

								world.spawnEntity(block);
							}
						}
						else
						{
							break;
						}
					}

					/////
				/*	BlockPos finalPos = pos;
					Runnable underBlock = () -> world.setBlockState(finalPos, Blocks.DIRT.getDefaultState());
					ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
					scheduler.schedule(underBlock, 1, TimeUnit.SECONDS);
					scheduler.shutdown();*/
					/////

					pos = pos.offset(facing.rotateAround(EnumFacing.Axis.Y));
					ii++;
				}
			}
		}
		useMana(player);
	}

	@Override
	public float getSpellReach()
	{
		return 6;
	}

}
