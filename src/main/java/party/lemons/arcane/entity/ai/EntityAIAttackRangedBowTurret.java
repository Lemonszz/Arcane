package party.lemons.arcane.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;
import party.lemons.arcane.entity.EntityTurret;

public class EntityAIAttackRangedBowTurret<T extends EntityTurret & IRangedAttackMob> extends EntityAIBase
{
	private final T entity;
	private int attackCooldown;
	private final float maxAttackDistance;
	private int attackTime = -1;
	private int seeTime;

	public EntityAIAttackRangedBowTurret(T p_i47515_1_, int p_i47515_4_, float p_i47515_5_)
	{
		this.entity = p_i47515_1_;
		this.attackCooldown = p_i47515_4_;
		this.maxAttackDistance = p_i47515_5_ * p_i47515_5_;
		this.setMutexBits(3);
	}

	public void setAttackCooldown(int p_189428_1_)
	{
		this.attackCooldown = p_189428_1_;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		return this.entity.getAttackTarget() != null && this.isBowInMainhand();
	}

	protected boolean isBowInMainhand()
	{
		return !this.entity.getHeldItemMainhand().isEmpty() && this.entity.getHeldItemMainhand().getItem() == Items.BOW;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting()
	{
		return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.isBowInMainhand();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		super.startExecuting();
		this.entity.setSwingingArms(true);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask()
	{
		super.resetTask();
		this.entity.setSwingingArms(false);
		this.seeTime = 0;
		this.attackTime = -1;
		this.entity.resetActiveHand();
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		EntityLivingBase entitylivingbase = this.entity.getAttackTarget();

		if(entitylivingbase != null)
		{
			double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
			boolean flag = this.entity.getEntitySenses().canSee(entitylivingbase);
			boolean flag1 = this.seeTime > 0;

			if(flag != flag1)
			{
				this.seeTime = 0;
			}

			if(flag)
			{
				++this.seeTime;
			}else
			{
				--this.seeTime;
			}
			this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
			if(this.entity.isHandActive())
			{
				if(!flag && this.seeTime < -60)
				{
					this.entity.resetActiveHand();
				}
				else if(flag)
				{
					int i = this.entity.getItemInUseMaxCount();
					System.out.println("3- " + i);

					if(i >= 20)
					{
						this.entity.resetActiveHand();
						this.entity.attackEntityWithRangedAttack(entitylivingbase, ItemBow.getArrowVelocity(i));
						this.attackTime = this.attackCooldown;
					}
				}
			}else if(--this.attackTime <= 0 && this.seeTime >= -60)
			{
				this.entity.setActiveHand(EnumHand.MAIN_HAND);
			}
		}
	}
}
