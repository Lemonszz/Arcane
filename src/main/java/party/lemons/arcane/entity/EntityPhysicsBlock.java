package party.lemons.arcane.entity;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.arcane.api.capability.PlayerData;

import java.util.List;

/**
 * Created by Sam on 16/12/2017.
 */
public class EntityPhysicsBlock extends EntityFallingBlock
{
	private EntityPlayer player;
	protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityPhysicsBlock.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Optional<IBlockState>> STATE = EntityDataManager.createKey(EntityPhysicsBlock.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	public static final DataParameter<Boolean> FIRED = EntityDataManager.createKey(EntityPhysicsBlock.class, DataSerializers.BOOLEAN);
	int age = 0;
	int fallTime = 0;

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
		this.player = owner;

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
		return (BlockPos)this.dataManager.get(ORIGIN);
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
		this.dataManager.register(STATE, Optional.<IBlockState>absent());
		this.dataManager.register(FIRED, false);
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}

	public void onUpdate()
	{
		if(world.isRemote)
		{
			player = Minecraft.getMinecraft().player;
		}

		for(int i = 0; i < 2; i++)
		{
			float x1 = 0.5F + (float) (posX - world.rand.nextFloat());
			float y1 = (float) (posY + world.rand.nextFloat());
			float z1 = 0.5F + (float) (posZ - world.rand.nextFloat());

			//world.spawnParticle(EnumParticleTypes.BLOCK_DUST, x1, posY, y1, 0, 1, 0, ));
			world.spawnParticle(EnumParticleTypes.BLOCK_DUST, x1, y1, z1, 0, 0, 0, Block.getStateId(getState()));
		}

		age++;
		if(player != null && age > 2)
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
				this.posX = pos.x;
				float a = ((float)age) / 10;
				this.posY = pos.y  - 0.25F + (-0.1 + (Math.sin(a) / 8)) ;
				this.posZ = pos.z;
				this.setPosition(posX, posY, posZ);
				//this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
				setOrigin(new BlockPos(posX, posY, posZ));

				damageEntities(2);
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
		else
		{
			if(player == null)
			{
				dataManager.set(FIRED, true);
			}
		}
		if(dataManager.get(FIRED))
		{
			Block block = getState().getBlock();

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
					BlockPos blockpos1 = new BlockPos(this);
					boolean flag = getState().getBlock() == Blocks.CONCRETE_POWDER;
					boolean flag1 = flag && this.world.getBlockState(blockpos1).getMaterial() == Material.WATER;
					double d0 = this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ;

					if (flag && d0 > 1.0D)
					{
						RayTraceResult raytraceresult = this.world.rayTraceBlocks(new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ), new Vec3d(this.posX, this.posY, this.posZ), true);

						if (raytraceresult != null && this.world.getBlockState(raytraceresult.getBlockPos()).getMaterial() == Material.WATER)
						{
							blockpos1 = raytraceresult.getBlockPos();
							flag1 = true;
						}
					}

					if (!this.onGround && !flag1)
					{
						if (this.fallTime > 100 && !this.world.isRemote && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600)
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
						IBlockState iblockstate = this.world.getBlockState(blockpos1);

						if (this.world.isAirBlock(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))) //Forge: Don't indent below.
							if (!flag1 && BlockFalling.canFallThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))))
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

							if (true)
							{
								if (this.world.mayPlace(block, blockpos1, true, EnumFacing.UP, (Entity)null) && (flag1 || !BlockFalling.canFallThrough(this.world.getBlockState(blockpos1.down()))) && this.world.setBlockState(blockpos1, this.getState(), 3))
								{
									if (block instanceof BlockFalling)
									{
										((BlockFalling)block).onEndFalling(this.world, blockpos1, this.getState(), iblockstate);
									}

									if (this.tileEntityData != null && block.hasTileEntity(this.getState()))
									{
										TileEntity tileentity = this.world.getTileEntity(blockpos1);

										if (tileentity != null)
										{
											NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());

											for (String s : this.tileEntityData.getKeySet())
											{
												NBTBase nbtbase = this.tileEntityData.getTag(s);

												if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s))
												{
													nbttagcompound.setTag(s, nbtbase.copy());
												}
											}

											tileentity.readFromNBT(nbttagcompound);
											tileentity.markDirty();
										}
									}
								}
								else if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops"))
								{
									this.entityDropItem(new ItemStack(block.getItemDropped(getState(), rand, 0), 1, block.damageDropped(this.getState())), 0.0F);
								}
							}
							else if (block instanceof BlockFalling)
							{
								((BlockFalling)block).onBroken(this.world, blockpos1);
							}
						}
					}
				}

				this.motionX *= 0.9800000190734863D;
				this.motionY *= 0.9800000190734863D;
				this.motionZ *= 0.9800000190734863D;
			}
			damageEntities(5);
		}
	}

	public void damageEntities(float amount)
	{
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX -1, posY - 1, posZ -1, posX + 1, posY + 1, posZ + 1));
		if(entities.size() > 0)
		{
			for(EntityLivingBase e : entities)
			{
				e.attackEntityFrom(DamageSource.causePlayerDamage(player), amount);
			}
		}
	}

	public void fall(float distance, float damageMultiplier)
	{

	}

	public static void registerFixesFallingBlock(DataFixer fixer)
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

		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		if(compound == null)
			compound = new NBTTagCompound();

		compound.setInteger("st", Block.getStateId(getState()));
		compound.setInteger("age", age);

		return super.writeToNBT(compound);
	}

	@SideOnly(Side.CLIENT)
	public World getWorldObj()
	{
		return this.world;
	}
}
