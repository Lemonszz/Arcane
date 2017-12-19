package party.lemons.arcane.spell;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenIceSpike;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellPage;
import party.lemons.arcane.api.spell.SpellUtil;
import party.lemons.arcane.entity.EntityPhysicsBlock;

/**
 * Created by Sam on 16/12/2017.
 */
public class SpellRockThrow extends Spell
{
	double vel;

	public SpellRockThrow(SpellPage elemental, int vel)
	{
		super(elemental);
		this.vel = vel;
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
			Vec3d vec3b = vec3.addVector(look.x * 4, look.y * 4, look.z * 4);

			RayTraceResult ray = player.world.rayTraceBlocks(vec3, vec3b, false, true, false);
			if(ray != null)
			{
				IBlockState state = player.world.getBlockState(ray.getBlockPos());
				EntityPhysicsBlock block = new EntityPhysicsBlock(player.world, ray.getBlockPos().getX() + 0.5F, ray.getBlockPos().getY(), ray.getBlockPos().getZ()+ 0.5F, player);
				block.setState(state);
				player.world.setBlockToAir(ray.getBlockPos());
				player.world.spawnEntity(block);
			}
		}
		useMana(player);
	}

	@Override
	public void castSpellHold(Spell spell, EntityPlayer player, long holdTime)
	{

	}
}