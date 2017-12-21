package party.lemons.arcane.entity;

import com.google.common.base.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.arcane.api.capability.PlayerData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sam on 16/12/2017.
 */
public class EntityPhysicsBlock extends EntityFallingBlock
{
	private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityPhysicsBlock.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Optional<IBlockState>> STATE = EntityDataManager.createKey(EntityPhysicsBlock.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	public static final DataParameter<Boolean> FIRED = EntityDataManager.createKey(EntityPhysicsBlock.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityTameable.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	private final int PARTICLE_AMOUNT  = 5;
	private final int MIN_AGE_FOR_HOLD = 2;
	private final int HOLDING_DAMAGE = 2;
	private final int FIRED_DAMAGE = 5;

	private int age = 0;
	private int fallTime = 0;
	private boolean noClipSetting;

	public EntityPhysicsBlock(World worldIn)
	{
		super(worldIn);
		this.setSize(0.98F, 0.98F);
	}

	public EntityPhysicsBlock(World worldIn, double x, double y, double z, EntityPlayer owner)
	{
		super(worldIn);
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.setPosition(x, y + (double)((1.0F - this.height) / 2.0F), z);
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.setOrigin(new BlockPos(this));
		this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(owner.getUniqueID()));

	}

	public void setState(IBlockState state)
	{
		this.dataManager.set(STATE, Optional.fromNullable(state));
	}
	public IBlockState getState()
	{
		return (IBlockState) ((Optional) this.dataManager.get(STATE)).orNull();
	}
	/**
	 * Returns true if it's possible to attack this entity with an item.
	 */
	public boolean canBeAttackedWithItem()
	{
		return false;
	}

	public void setOrigin(BlockPos p_184530_1_)
	{
		this.dataManager.set(ORIGIN, p_184530_1_);
	}

	@SideOnly(Side.CLIENT)
	public BlockPos getOrigin()
	{
		return this.dataManager.get(ORIGIN);
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	protected boolean canTriggerWalking()
	{
		return false;
	}

	protected void entityInit()
	{
		this.dataManager.register(ORIGIN, BlockPos.ORIGIN);
		this.dataManager.register(STATE, Optional.absent());
		this.dataManager.register(FIRED, false);
		this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}

	@Nullable
	public EntityLivingBase getOwner()
	{
		try
		{
			UUID uuid = dataManager.get(OWNER_UNIQUE_ID).get();
			return uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
		}
		catch (IllegalArgumentException var2)
		{
			return null;
		}
	}

	public void onUpdate()
	{
		EntityPlayer player = (EntityPlayer) getOwner();
		age++;
		boolean hasBeenFired = dataManager.get(FIRED);

		createBlockParticles();

		if(!hasBeenFired && age > MIN_AGE_FOR_HOLD)
		{
			if(player != null)
				updateHolding(player);
			else
				dataManager.set(FIRED, true);
		}
		else if(hasBeenFired)
		{
			updateFired();
		}
	}

	private void updateHolding(EntityPlayer player)
	{
		PlayerData data = player.getCapability(PlayerData.CAPABILITY, null);
		if(data.isHoldingCast() && !dataManager.get(FIRED))
		{
			this.noClip = true;

			Vec3d look = player.getLookVec();
			Vec3d eye = player.getPositionEyes(1.0F);
			Vec3d pos = eye.addVector(look.x * 3, look.y * 3, look.z * 3);

			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;

			float a = ((float)age) / 10;
			this.posX = pos.x;
			this.posY = pos.y  - 0.25F + (-0.1 + (Math.sin(a) / 8)) ;
			this.posZ = pos.z;

			this.setPosition(posX, posY, posZ);
			damageEntities(HOLDING_DAMAGE);
		}
		else
		{
			if(!dataManager.get(FIRED))
			{
				Vec3d look = player.getLookVec();
				this.noClip = false;
				this.motionX = look.x / 1.25;
				this.motionY = look.y;
				this.motionZ = look.z / 1.25;
				dataManager.set(FIRED, true);
			}
		}
	}

	private void updateFired()
	{
		Block block = getState().getBlock();
		if(noClipSetting)
		{
			noClip = motionY > -0.3;
		}

		if (getState().getMaterial() == Material.AIR)
		{
			this.setDead();
		}
		else
		{
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			fallTime++;

			if (!this.hasNoGravity())
			{
				this.motionY -= 0.03999999910593033D;
			}

			this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

			if (!this.world.isRemote)
			{
				BlockPos entityPos = new BlockPos(this);
				boolean isConcrete = getState().getBlock() == Blocks.CONCRETE_POWDER;
				boolean isConcreteInWater = isConcrete && this.world.getBlockState(entityPos).getMaterial() == Material.WATER;
				double motionSq = this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ;

				if (isConcrete && motionSq > 1.0D)
				{
					RayTraceResult raytraceresult = this.world.rayTraceBlocks(new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ), new Vec3d(this.posX, this.posY, this.posZ), true);

					if (raytraceresult != null && this.world.getBlockState(raytraceresult.getBlockPos()).getMaterial() == Material.WATER)
					{
						entityPos = raytraceresult.getBlockPos();
						isConcreteInWater = true;
					}
				}

				if (!this.onGround && !isConcreteInWater)
				{
					if (this.fallTime > 100 && !this.world.isRemote && (entityPos.getY() < 1 || entityPos.getY() > 256) || this.fallTime > 600)
					{
						if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops"))
						{
							this.entityDropItem(new ItemStack(block.getItemDropped(getState(), rand, 0), 1, block.damageDropped(this.getState())), 0.0F);
						}
						this.setDead();
					}
				}
				else
				{
					IBlockState iblockstate = this.world.getBlockState(entityPos);

					if (this.world.isAirBlock(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ)))
						if (!isConcreteInWater && BlockFalling.canFallThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))))
						{
							this.onGround = false;
							return;
						}

					this.motionX *= 0.699999988079071D;
					this.motionZ *= 0.699999988079071D;
					this.motionY *= -0.5D;

					if (iblockstate.getBlock() != Blocks.PISTON_EXTENSION)
					{
						this.setDead();
						if (this.world.mayPlace(block, entityPos, true, EnumFacing.UP, null) && (isConcreteInWater || !BlockFalling.canFallThrough(this.world.getBlockState(entityPos.down()))) && this.world.setBlockState(entityPos, this.getState(), 3))
						{
							if (block instanceof BlockFalling)
							{
								((BlockFalling)block).onEndFalling(this.world, entityPos, this.getState(), iblockstate);
							}
						}
						else if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops"))
						{
							this.entityDropItem(new ItemStack(block.getItemDropped(getState(), rand, 0), 1, block.damageDropped(this.getState())), 0.0F);
						}
					}
				}
			}

			this.motionX *= 0.9800000190734863D;
			this.motionY *= 0.9800000190734863D;
			this.motionZ *= 0.9800000190734863D;
		}
		damageEntities(FIRED_DAMAGE);
	}

	private void createBlockParticles()
	{
		for(int i = 0; i < PARTICLE_AMOUNT; i++)
		{
			float x1 = 0.5F + (float) (posX - world.rand.nextFloat());
			float y1 = (float) (posY + world.rand.nextFloat());
			float z1 = 0.5F + (float) (posZ - world.rand.nextFloat());
			world.spawnParticle(EnumParticleTypes.BLOCK_DUST, x1, y1, z1, (-0.5 + rand.nextFloat()) / 3, 0, (-0.5 + rand.nextFloat()) / 3, Block.getStateId(getState()));
		}
	}

	private void damageEntities(float amount)
	{
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX -1, posY - 1, posZ -1, posX + 1, posY + 1, posZ + 1));
		if(entities.size() > 0)
		{
			for(EntityLivingBase e : entities)
			{
				e.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) getOwner()), amount);
			}
		}
	}

	public void setNoClipSetting()
	{
		this.noClipSetting = true;
	}

	public void fall(float distance, float damageMultiplier)
	{

	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		setState(Block.getStateById(compound.getInteger("st")));
		age = compound.getInteger("age");

		String pl = "";
		if (compound.hasKey("player", 8))
		{
			pl = compound.getString("player");
		}
		UUID playerID = UUID.fromString(pl);
		dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(playerID));
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		if(compound == null)
			compound = new NBTTagCompound();

		compound.setInteger("st", Block.getStateId(getState()));
		compound.setInteger("age", age);
		compound.setString("player", dataManager.get(OWNER_UNIQUE_ID).get().toString());

		return super.writeToNBT(compound);
	}

	@SideOnly(Side.CLIENT)
	public World getWorldObj()
	{
		return this.world;
	}
}
