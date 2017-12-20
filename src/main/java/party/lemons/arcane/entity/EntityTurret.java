package party.lemons.arcane.entity;

import com.google.common.base.Optional;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Sam on 20/12/2017.
 */
public abstract class EntityTurret extends EntityCreature implements IRangedAttackMob
{
	private static final DataParameter<Optional<BlockPos>> TARGET = EntityDataManager.<Optional<BlockPos>>createKey(EntityTurret.class, DataSerializers.OPTIONAL_BLOCK_POS);
	protected int shootTime = -1;
	protected int maxShootTime = 1;

	public EntityTurret(World worldIn)
	{
		super(worldIn);
	}

	public EntityTurret(World worldIn, double x, double y, double z)
	{
		this(worldIn);
		this.setPosition(x, y, z);
		this.prevPosX = posX;
		this.prevPosZ = posZ;
	}

	@Override
	protected void entityInit()
	{
		this.getDataManager().register(TARGET, Optional.absent());

		super.entityInit();
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		shootTime--;
		if(shootTime <= 0)
		{
			onShoot();
			shootTime = maxShootTime;
		}
		posX = prevPosX;
		posZ = prevPosZ;
		posY = prevPosY;
	}

	public void setTarget(@Nullable BlockPos beamTarget)
	{
		this.getDataManager().set(TARGET, Optional.fromNullable(beamTarget));
	}

	@Nullable
	public BlockPos getTarget()
	{
		return (BlockPos)((Optional)this.getDataManager().get(TARGET)).orNull();
	}

	public void readEntityFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("target", 10))
		{
			this.setTarget(NBTUtil.getPosFromTag(compound.getCompoundTag("target")));
		}
		super.readEntityFromNBT(compound);
	}

	public void writeEntityToNBT(NBTTagCompound compound)
	{
		if (this.getTarget() != null)
		{
			compound.setTag("target", NBTUtil.createPosTag(this.getTarget()));
		}
		super.writeEntityToNBT(compound);
	}

	abstract void onShoot();

	protected boolean canTriggerWalking()
	{
		return false;
	}
	public boolean canBeCollidedWith()
	{
		return true;
	}

	public int getShootTime()
	{
		return shootTime;
	}
}
