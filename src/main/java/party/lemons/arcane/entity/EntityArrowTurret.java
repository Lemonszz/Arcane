package party.lemons.arcane.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Created by Sam on 20/12/2017.
 */
public class EntityArrowTurret extends EntityTurret
{
	public static final DataParameter<Boolean> HAS_TARGET = EntityDataManager.createKey(EntityArrowTurret.class, DataSerializers.BOOLEAN);

	private final ItemStack stack = new ItemStack(Items.BOW);
	public int age = 0;

	public EntityArrowTurret(World worldIn)
	{
		super(worldIn);
		this.maxShootTime = 60;
		this.setHeldItem(EnumHand.MAIN_HAND, stack);
	}

	public EntityArrowTurret(World worldIn, double x, double y, double z)
	{
		this(worldIn);
		this.setPosition(x, y, z);
	}

	protected void entityInit()
	{
		this.dataManager.register(HAS_TARGET, false);
		super.entityInit();
	}


	protected void initEntityAI()
	{
		super.initEntityAI();
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityLiving.class, 10, true, false, IMob.MOB_SELECTOR));
	}

	@Override
	public float getEyeHeight()
	{
		float y = 1.2F;
		double offset = -0.10 + ((y + Math.sin(this.age * 0.05)) / 4);
		return (float)(y + offset);
	}

	@Override
	public void onUpdate()
	{
		age++;
		if(!world.isRemote)
		{
			if(this.getAttackTarget() != null)
			{
				this.getLookHelper().setLookPositionWithEntity(getAttackTarget(), 30, 30);
				if(!dataManager.get(HAS_TARGET))
					dataManager.set(HAS_TARGET, true);

			}else
			{
				if(dataManager.get(HAS_TARGET))
					dataManager.set(HAS_TARGET, false);
			}
		}

		if (this.world.isRemote)
		{
			for (int i = 0; i < 2; ++i)
			{
				this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
			}
		}
		super.onUpdate();
	}

	@Override
	void onShoot()
	{
		if(!world.isRemote && getAttackTarget() != null)
		{
			attackEntityWithRangedAttack(getAttackTarget(), 25);
		}
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{
		EntityArrow entityarrow = this.getArrow(distanceFactor);
		double d0 = target.posX - this.posX;
		double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entityarrow.posY;
		double d2 = target.posZ - this.posZ;
		double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
		entityarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F,0);
		this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(entityarrow);
	}

	protected EntityArrow getArrow(float p_190726_1_)
	{
		EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);
		entitytippedarrow.setPotionEffect(new ItemStack(Items.ARROW));
		return entitytippedarrow;
	}


	@Override
	public void setSwingingArms(boolean swingingArms)
	{

	}
}
