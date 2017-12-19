package party.lemons.magicmod.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Sam on 18/12/2017.
 */
public class EntityFirestormProjectile extends EntityFireball
{
	public EntityFirestormProjectile(World worldIn)
	{
		super(worldIn);
		this.setSize(1.0F, 1.0F);
	}

	@SideOnly(Side.CLIENT)
	public EntityFirestormProjectile(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
	{
		super(worldIn, x, y, z, accelX, accelY, accelZ);
		this.setSize(1.0F, 1.0F);
	}

	public EntityFirestormProjectile(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		super(worldIn, shooter, accelX, accelY, accelZ);
		this.setSize(1.0F, 1.0F);
	}

	protected void onImpact(RayTraceResult result)
	{
		if(!world.isRemote)
		{
			if(result.typeOfHit == RayTraceResult.Type.BLOCK || !result.entityHit.isEntityEqual(this.shootingEntity))
			{
				Explosion ex = world.newExplosion(shootingEntity, posX, posY, posZ, 7, true, true);
				BlockPos bottom = getPosition();
				for(BlockPos pos : ex.getAffectedBlockPositions())
				{
					if(pos.getY() < bottom.getY())
					{
						bottom = pos;
					}
				}

				bottom = bottom.up(2);
				if(!this.world.isRemote)
				{
					EntityAreaEffectCloud entityareaeffectcloud = new EntityFirestorm(this.world, posX, bottom.getY() - 0.5F, posZ);
					entityareaeffectcloud.setOwner(this.shootingEntity);
					entityareaeffectcloud.setParticle(EnumParticleTypes.FLAME);
					entityareaeffectcloud.setRadius(10.0F);
					entityareaeffectcloud.setDuration(600);
					entityareaeffectcloud.setRadiusPerTick((7.0F - entityareaeffectcloud.getRadius()) / (float) entityareaeffectcloud.getDuration());
					entityareaeffectcloud.setColor(0xFF0000);
					this.world.spawnEntity(entityareaeffectcloud);
					this.setDead();
				}
			}
		}
		else
		{
			for(int i2 = 0; i2 < 200; ++i2)
			{
				float f2 = rand.nextFloat() * 4.0F;
				float f3 = rand.nextFloat() * ((float) Math.PI * 2F);
				double d6 = (double) (MathHelper.cos(f3) * f2);
				double d7 = 0.01D + rand.nextDouble() * 0.5D;
				double d8 = (double) (MathHelper.sin(f3) * f2);
				BlockPos pos = getPosition();

				Particle particle = this.spawnParticle(EnumParticleTypes.FLAME.getParticleID(), pos.getX() + d6 * 0.1D, pos.getY() + 0.3D, pos.getZ() + d8 * 0.1D, d6, d7, d8);

				if(particle != null)
				{
					particle.multiplyVelocity(f2);
				}
			}
		}
	}


	@Nullable
	@SideOnly(Side.CLIENT)
	private Particle spawnParticle(int particleID, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters)
	{
		Minecraft mc = Minecraft.getMinecraft();
		Entity entity = mc.getRenderViewEntity();

		if (mc != null && entity != null && mc.effectRenderer != null)
		{
			int k1 = this.calculateParticleLevel(mc, false);
			double d3 = entity.posX - xCoord;
			double d4 = entity.posY - yCoord;
			double d5 = entity.posZ - zCoord;

			mc.effectRenderer.spawnEffectParticle(particleID, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
		}
		else
		{
			return null;
		}

		return null;
	}

	private int calculateParticleLevel(Minecraft mc, boolean minParticles)
	{
		int k1 = mc.gameSettings.particleSetting;

		if (minParticles && k1 == 2 && this.world.rand.nextInt(10) == 0)
		{
			k1 = 1;
		}

		if (k1 == 1 && this.world.rand.nextInt(3) == 0)
		{
			k1 = 2;
		}

		return k1;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	public boolean canBeCollidedWith()
	{
		return false;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}

	protected EnumParticleTypes getParticleType()
	{
		return EnumParticleTypes.FLAME;
	}

	protected boolean isFireballFiery()
	{
		return true;
	}
}