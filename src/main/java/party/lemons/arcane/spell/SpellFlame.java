package party.lemons.arcane.spell;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellPage;
import party.lemons.arcane.api.spell.SpellUtil;
import party.lemons.arcane.client.ClientUtil;

/**
 * Created by Sam on 14/12/2017.
 */
public class SpellFlame extends Spell
{
	private  int damage, fireTime, reach;

	public SpellFlame(SpellPage page, int fireTime, int damage, int reach)
	{
		super(page);

		this.fireTime = fireTime;
		this.damage = damage;
		this.reach = reach;
	}

	@Override
	public void castSpellHold(Spell spell, EntityPlayer player, long holdTime)
	{
		if(!canCast(player))
			return;

		World world = player.getEntityWorld();
		Vec3d vec3 = player.getLookVec();
		double x = player.posX + (vec3.x * 2);
		double y = player.posY + player.getEyeHeight() + vec3.y;
		double z = player.posZ + (vec3.z * 2);

		float xR = world.rand.nextFloat() * (-1 + (2 * world.rand.nextInt(2))) / 2;
		float yR = world.rand.nextFloat() * (-1 + (2 * world.rand.nextInt(2))) / 2;
		float zR = world.rand.nextFloat() * (-1 + (2 * world.rand.nextInt(2))) / 2;

		float xV = world.rand.nextFloat() * (-1 + (2 * world.rand.nextInt(2))) / 20;
		float yV = world.rand.nextFloat() * (-1 + (2 * world.rand.nextInt(2))) / 20;
		float zV = world.rand.nextFloat() * (-1 + (2 * world.rand.nextInt(2))) / 20;

		float r = getSpellReach();
		player.world.spawnParticle(EnumParticleTypes.FLAME, x + xR, y + yR , z + zR, (
				(xV + vec3.x) / 50) * r,
				((yV + vec3.y) / 50) * r,
				((zV + vec3.z) / 50)* r);
		player.playSound(SoundEvents.BLOCK_FIRE_AMBIENT, 1, world.rand.nextFloat());
		PlayerData data = player.getCapability(PlayerData.CAPABILITY, null);
		if(data.getLookEntity() != null && !world.isRemote)
		{
			Entity en = data.getLookEntity();
			for(Entity e : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(en.posX - en.width, en.posY - en.height, en.posZ - en.width, en.posX + en.width, en.posY + en.height, en.posZ + en.width)))
			{
				if(e != player)
					e.setFire(fireTime);
			}

			if(data.getLookEntity() instanceof EntityLivingBase)
			{
				EntityLivingBase livingBase = (EntityLivingBase) data.getLookEntity();
				livingBase.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
			}
		}

		useMana(player);
	}

	@Override
	public float getSpellReach()
	{
		return reach;
	}
}
