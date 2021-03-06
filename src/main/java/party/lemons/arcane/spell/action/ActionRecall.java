package party.lemons.arcane.spell.action;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import party.lemons.arcane.api.action.ActionState;
import party.lemons.arcane.api.action.PlayerAction;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.SpellUtil;

import java.util.Random;

/**
 * Created by Sam on 24/12/2017.
 */
public abstract class ActionRecall extends PlayerAction
{
	protected final float speed = 0.15F;			//Speed increase per tick
	protected final float maxY = 600;				//The max y position when moving up
	protected final float maxSpeedForReset = 20;	//If the speed reaches this value, they will be automatically sent to their home
	protected int direction = 0;
	protected final int particleAmount = 10;

	public ActionRecall(int direction)
	{
		this.direction = direction;
	}

	@Override
	public boolean onTakeDamage(EntityPlayer player, DamageSource source, ActionState state)
	{
		return false;
	}

	@Override
	public void onActionUpdate(EntityPlayer player, ActionState state)
	{
		PlayerData data = player.getCapability(PlayerData.CAPABILITY, null);
		recallGetHomePosition(player, data, player.world);

		recallUpdate(player, data,  direction * speed);


		NBTTagCompound stateTags = state.getTagCompound();
		BlockPos pos = NBTUtil.getPosFromTag(stateTags.getCompoundTag("spawnpos"));

		if(shouldSwitch(player, state, pos))
			recallResetAtPosition(player, data, getResetPosition(player, pos), getNextState(state));
	}

	public abstract boolean shouldSwitch(EntityPlayer player, ActionState state, BlockPos spawnPos);
	public abstract BlockPos getResetPosition(EntityPlayer player, BlockPos spawnPos);
	public abstract ActionState getNextState(ActionState currentState);

	private BlockPos recallGetHomePosition(EntityPlayer player, PlayerData data, World world)
	{
		ActionState state = data.getActionState();
		NBTTagCompound stateTags = state.getTagCompound();

		BlockPos spawnPos;
		if(stateTags.hasKey("recallPos"))
		{
			NBTTagCompound posTag = stateTags.getCompoundTag("spawnpos");
			spawnPos = NBTUtil.getPosFromTag(posTag);
		}
		else
		{

			//If there is a bed position
			boolean hasBed = true;

			//Get player's stored bed location
			BlockPos bedLocation = player.getBedLocation();

			//If the bedLocation exists and there isn't a bed @ the bed location, player can't go to bed position.
			if(bedLocation == null || player.getBedSpawnLocation(world, bedLocation, false) == null)
				hasBed = false;

			//If there isn't a bed to go to, get the world's spawn position
			if(!hasBed)
			{
				BlockPos blockpos = world.provider.getRandomizedSpawnPoint();

				//Set the location to the world spawn
				spawnPos = world.getTopSolidOrLiquidBlock(blockpos);
			}
			else
			{
				//Set the location to the bed location
				spawnPos = player.getBedSpawnLocation(world, bedLocation, false);
			}

			//Save the new position
			NBTTagCompound posTag = NBTUtil.createPosTag(spawnPos);
			stateTags.setTag("spawnpos", posTag);

			state.setTagCompound(stateTags);
		}

		return spawnPos;
	}

	private void recallUpdate(EntityPlayer player, PlayerData data, float speed)
	{
		ActionState state = data.getActionState();
		NBTTagCompound stateTags = state.getTagCompound();
		float spd = Math.abs(speed);
		if(stateTags.hasKey("speed"))
		{
			spd = stateTags.getFloat("speed") + Math.abs(speed);
		}

		//Set the player position
		player.posY += Math.signum(speed) * spd;
		player.rotationPitch  = 90;
		if(!player.world.isRemote)
		{
			((EntityPlayerMP)player).connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, 90);
		}
		else
		{
			World world = player.world;
			Random rand = world.rand;

			for(int i = 0; i < particleAmount; i++)
			{
				float posX = (float) (player.posX - 0.5F + rand.nextFloat());
				float posZ = (float) (player.posZ - 0.5F + rand.nextFloat());
				float posY = (float) (player.posY - rand.nextFloat() * 2);

				float vX = rand.nextFloat() / 5;
				float vZ = rand.nextFloat() / 5;
				float vY = -0.25F;

				world.spawnParticle(EnumParticleTypes.CLOUD, posX, posY, posZ, vX, vY, vZ);
			}
		}

		stateTags.setFloat("speed", spd);
		state.setTagCompound(stateTags);

		player.capabilities.isFlying = true;
		player.capabilities.allowFlying = true;
	}

	private void recallResetAtPosition(EntityPlayer player, PlayerData data, BlockPos position, ActionState newState)
	{
		//Make sure the player is moved to the correct position
		player.rotationPitch  = 90;
		if(!player.world.isRemote)
			((EntityPlayerMP)player).connection.setPlayerLocation(position.getX() + 0.5, position.up().getY(), position.getZ() + 0.5, player.rotationYaw, 90);

		//Move into the next state
		data.setActionState(player, newState);

		//Sync all the data to the client to make sure there's no desync
		SpellUtil.syncData(player);

		//Make sure the player no longer has flying abilities
		player.capabilities.isFlying = false;
		if(!player.isCreative())
			player.capabilities.allowFlying = false;
	}

	public static class RecallUp extends ActionRecall
	{
		public RecallUp()
		{
			super(1);
		}

		@Override
		public boolean shouldSwitch(EntityPlayer player, ActionState state, BlockPos spawnPos)
		{
			return player.posY >= maxY;
		}

		@Override
		public BlockPos getResetPosition(EntityPlayer player, BlockPos spawnPos)
		{
			return new BlockPos(spawnPos.getX(), player.posY, spawnPos.getZ());
		}

		@Override
		public ActionState getNextState(ActionState currentState)
		{
			NBTTagCompound currentTags = currentState.getTagCompound();
			currentTags.setFloat("speed", 0);

			ActionState nextState = new ActionState(ArcaneActions.RECALL_DOWN, currentTags);
			return nextState;
		}
	}

	public static class RecallDown extends ActionRecall
	{
		public RecallDown()
		{
			super(-1);
		}

		@Override
		public boolean shouldSwitch(EntityPlayer player, ActionState state, BlockPos spawnPos)
		{
			return player.posY <= spawnPos.down().getY();
		}

		@Override
		public BlockPos getResetPosition(EntityPlayer player, BlockPos spawnPos)
		{
			return spawnPos;
		}

		@Override
		public ActionState getNextState(ActionState currentState)
		{
			return new ActionState(PlayerAction.NONE);
		}
	}
}
