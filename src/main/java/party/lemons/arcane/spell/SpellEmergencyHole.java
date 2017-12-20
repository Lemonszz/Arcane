package party.lemons.arcane.spell;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellPage;
import party.lemons.arcane.config.ArcaneConfig;

/**
 * Created by Sam on 16/12/2017.
 */
public class SpellEmergencyHole extends Spell
{
	public SpellEmergencyHole(SpellPage elemental)
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
				IBlockState state1 = player.world.getBlockState(ray.getBlockPos());
				IBlockState state2 = player.world.getBlockState(ray.getBlockPos().down());
				World world = player.world;

				BlockPos pos1 = ray.getBlockPos();
				BlockPos pos2 = ray.getBlockPos().down();
				if(ArcaneConfig.isEarthBlock(state1.getBlock()) && ArcaneConfig.isEarthBlock(state2.getBlock()))
				{
					if(canPlace(pos1.up(2), state1, world, player) && canPlace(pos1.up(), state2, world, player))
					{
						world.setBlockState(pos1.up(2), state1);
						world.setBlockState(pos1.up(), state2);
						world.setBlockToAir(pos1);
						world.setBlockToAir(pos2);

						((EntityPlayerMP) player).connection.setPlayerLocation(pos2.getX(), pos2.getY(), pos2.getZ(), ((EntityPlayerMP) player).rotationYaw, ((EntityPlayerMP) player).rotationPitch);
						useMana(player);
						//TODO: Sound/Particles
					}
					else
					{
						player.sendStatusMessage(new TextComponentTranslation("spell.hole.fail"), true);
					}
				}
				else
				{
					player.sendStatusMessage(new TextComponentTranslation("spell.hole.fail"), true);
				}
			}
		}
	}

	private boolean canPlace(BlockPos pos, IBlockState state, World world, EntityPlayer player)
	{
		boolean air = world.isAirBlock(pos);
		boolean canPlace = world.mayPlace(state.getBlock(), pos, true, EnumFacing.UP, player);

		return air || canPlace;
	}

	@Override
	public float getSpellReach()
	{
		return 2;
	}

}
