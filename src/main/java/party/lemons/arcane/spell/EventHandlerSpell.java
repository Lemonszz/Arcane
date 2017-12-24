package party.lemons.arcane.spell;

import net.minecraftforge.fml.common.Mod;
import party.lemons.arcane.config.ArcaneConstants;

/**
 * Created by Sam on 22/12/2017.
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ArcaneConstants.MODID)
public class EventHandlerSpell
{
/*
	@SubscribeEvent
	public static void handleRecallDamage(LivingDamageEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			PlayerData data = player.getCapability(PlayerData.CAPABILITY, null);

			//If the player is recalling, cancel the event so the player wont take damage when moving though blocks.
			if(data.getRecallState() != SpellRecall.RecallState.NONE)
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void handleRecall(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		PlayerData data = player.getCapability(PlayerData.CAPABILITY, null);
		SpellRecall.RecallState state = data.getRecallState();

		final float speed = 0.15F;			//Speed increase per tick
		final float maxY = 600;				//The max y position when moving up
		final float maxSpeedForReset = 20;	//If the speed reaches this value, they will be automatically sent to their home

		//state will not = NONE if the player is recalling
		if(state != SpellRecall.RecallState.NONE)
		{
			if(!player.world.isRemote)
			{
				EntityPlayerMP playerMP = (EntityPlayerMP) player;
				WorldServer worldIn = playerMP.getServerWorld();

				//Get the player's spawn location
				BlockPos spawnPos = recallGetHomePosition(playerMP, data, worldIn);

				switch(state)
				{
					//If moving up
					case UP:
						//Update
						recallUpdate(playerMP, data, speed, true);

						//If the player has reached the maximum y position, move downwards
						if(player.posY >= maxY)
							recallResetAtPosition(playerMP, data, new BlockPos(spawnPos.getX(), playerMP.posY, spawnPos.getZ()), SpellRecall.RecallState.DOWN, false);
						break;
					//If moving down
					case DOWN:
						//Update
						recallUpdate(playerMP, data, -speed, false);

						//If the player  has reached the spawn position y, reset
						if(playerMP.posY <= spawnPos.down().getY())
							recallResetAtPosition(playerMP, data, spawnPos, SpellRecall.RecallState.NONE, true);
						break;
				}

				//If the player has gotten stuck somehow, eventually just tele them to the end position
				if(data.getRecallSpeed() >= maxSpeedForReset)
				{
					Arcane.logger.error("Detected player stuck in recall state, returning them to spawn.");
					recallResetAtPosition(playerMP, data, spawnPos, SpellRecall.RecallState.NONE, true);
				}
			}
		}
	}

	private static BlockPos recallGetHomePosition(EntityPlayerMP player, PlayerData data, World world)
	{
		//Get the saved recall position
		BlockPos spawnPos = data.getRecallPosition();

		//If there isn't any saved recall position, try and find one
		if(spawnPos == null)
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
			data.setRecallPosition(spawnPos);
		}

		return spawnPos;
	}

	private static void recallUpdate(EntityPlayerMP playerMP, PlayerData data, float speed, boolean setFlying)
	{
		//Increase the recall speed
		data.setRecallSpeed(data.getRecallSpeed() + Math.abs(speed));

		//Set the player position
		playerMP.posY += Math.signum(speed) * data.getRecallSpeed();
		playerMP.connection.setPlayerLocation(playerMP.posX, playerMP.posY, playerMP.posZ, playerMP.rotationYaw, 90);

		//If going up, set the player to flying to avoid console spam
		if(setFlying)
			playerMP.capabilities.isFlying = true;
	}

	private static void recallResetAtPosition(EntityPlayerMP player, PlayerData data, BlockPos position, SpellRecall.RecallState newState, boolean resetRecallPosition)
	{
		//Make sure the player is moved to the correct position
		player.connection.setPlayerLocation(position.getX() + 0.5, position.up().getY(), position.getZ() + 0.5, player.rotationYaw, 90);

		//Move into the next state
		data.setRecallState(newState);

		//Sync all the data to the client to make sure there's no desync
		SpellUtil.syncData(player);

		//Make sure the player no longer has flying abilities
		player.capabilities.isFlying = false;

		//Reset the recall speed
		data.setRecallSpeed(0);

		//If we need to get rid of the stored position, remove it
		if(resetRecallPosition)
			data.setRecallPosition(null);
	}*/
}
