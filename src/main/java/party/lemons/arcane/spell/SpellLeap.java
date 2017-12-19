package party.lemons.arcane.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellPage;
import party.lemons.arcane.api.spell.SpellUtil;

/**
 * Created by Sam on 16/12/2017.
 */
public class SpellLeap extends Spell
{
	double vel;

	public SpellLeap(SpellPage elemental, int vel)
	{
		super(elemental);
		this.vel = vel;
	}

	@Override
	public void castSpellPress(Spell spell, EntityPlayer player)
	{
		if(!canCast(player) || !player.onGround)
			return;

		player.playSound(SoundEvents.ENTITY_ENDERDRAGON_FLAP, player.world.rand.nextFloat(), player.world.rand.nextFloat());
		Vec3d vec3d = player.getPositionEyes(1);
		Vec3d vec3d1 = player.getLook(1);
		Vec3d vec3d2 = vec3d.addVector(vec3d1.x * 1, vec3d1.y * 1, vec3d1.z * 1);
		RayTraceResult res =  SpellUtil.rayTrace(player.world, 5, vec3d, vec3d2, false, false, true);

		Vec3d vec3 = player.getLookVec();
		double x = player.posX + (vec3.x * 4);
		double y = player.posY + player.getEyeHeight() + vec3.y;
		double z = player.posZ + (vec3.z * 4);
		World world = player.world;
		for(int i = 0; i < vel * 4; i++)
		{
			float xo =  world.rand.nextFloat() * (-1 + (2 * world.rand.nextInt(2))) / 2;
			float yo =  world.rand.nextFloat() * (-1 + (2 * world.rand.nextInt(2))) / 2;
			float zo =  world.rand.nextFloat() * (-1 + (2 * world.rand.nextInt(2))) / 2;

			player.world.spawnParticle(EnumParticleTypes.CLOUD, x + xo, y + yo, z + zo, 0, 0, 0);
		}

		if(player.rotationPitch >= 70 && player.rotationPitch <= 100)
		{
				player.addVelocity(0, vel, 0);
		}
		else
		{
				player.knockBack(player, (float) vel * 2, (double) MathHelper.sin(player.rotationYaw * 0.017453292F), (double) -(MathHelper.cos(player.rotationYaw * 0.017453292F)));
				player.addVelocity(0, 0.25, 0);
		}

		useMana(player);
	}

}
