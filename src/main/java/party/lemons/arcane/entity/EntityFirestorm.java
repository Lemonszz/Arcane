package party.lemons.arcane.entity;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Sam on 18/12/2017.
 */
public class EntityFirestorm extends EntityAreaEffectCloud
{
	public EntityFirestorm(World world)
	{
		super(world);
	}

	public EntityFirestorm(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public void onUpdate()
	{
		super.onUpdate();

		List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox());

		if (!list.isEmpty())
		{
			for(EntityLivingBase entitylivingbase : list)
			{
				if(entitylivingbase != this.getOwner() && !entitylivingbase.isPotionActive(MobEffects.FIRE_RESISTANCE))
				{
					entitylivingbase.setFire(15);
					DamageSource source = DamageSource.IN_FIRE;

					if(getOwner() instanceof EntityPlayer)
						source = DamageSource.causePlayerDamage((EntityPlayer) getOwner());
					else if(getOwner() != null)
						source = DamageSource.causeMobDamage(getOwner());

					entitylivingbase.attackEntityFrom(source, 1);

					for (int i = 0; i < 2; ++i)
					{
						float f1 = this.rand.nextFloat();
						float f2 = rand.nextFloat();
						float f3 = rand.nextFloat();
						float f4 = MathHelper.sin(f1) * f2;

						this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), entitylivingbase.posX + (double)f3, entitylivingbase.posY, entitylivingbase.posZ + (double)f4, rand.nextFloat() / 4, 0.25D, rand.nextFloat() / 4);
						this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.LAVA.getParticleID(), entitylivingbase.posX + (double)f3, entitylivingbase.posY, entitylivingbase.posZ + (double)f4, rand.nextFloat() / 4, 0.25D, rand.nextFloat() / 4);
					}
				}
			}
		}
	}
}