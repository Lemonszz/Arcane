package party.lemons.arcane.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.SpellUtil;
import party.lemons.arcane.config.ArcaneConstants;

import java.util.function.Predicate;

/**
 * Created by Sam on 22/12/2017.
 */
@Mod.EventBusSubscriber(modid = ArcaneConstants.MODID)
public class EventHandlerSpell
{

	@SubscribeEvent
	public static void handleRecallDamage(LivingDamageEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			PlayerData data = player.getCapability(PlayerData.CAPABILITY, null);
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
		final float speed = 0.15F;
		final float maxY = 600;

		if(state != SpellRecall.RecallState.NONE)
		if(!player.world.isRemote)
		{
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			WorldServer worldIn = playerMP.getServerWorld();
			BlockPos spawnPos = data.getRecallPosition();

			if(spawnPos == null)
			{
				BlockPos bedLocation = player.getBedLocation();
				boolean hasBed = true;
				if(bedLocation == null)
					hasBed = false;
				else if(player.getBedSpawnLocation(worldIn, bedLocation, false) == null)
					hasBed = false;

				if(!hasBed)
				{
					BlockPos blockpos = worldIn.provider.getRandomizedSpawnPoint();
					spawnPos = worldIn.getTopSolidOrLiquidBlock(blockpos);
				}
				else
				{
					spawnPos = player.getBedSpawnLocation(worldIn, bedLocation, false);
				}
				data.setRecallPosition(spawnPos);
			}
			switch(state)
			{
				case NONE:
					spawnPos = null;
				case UP:
					recallUpdate(playerMP, data, speed, true);
					if(player.posY >= maxY)
					{
						recallResetAtPosition(playerMP, data, spawnPos, SpellRecall.RecallState.DOWN, false);
					}
					break;
				case DOWN:
					recallUpdate(playerMP, data, -speed, false);
					if(playerMP.posY <= spawnPos.getY())
					{
						recallResetAtPosition(playerMP, data, spawnPos, SpellRecall.RecallState.NONE, true);
					}
					break;
			}
		}
	}

	private static void recallUpdate(EntityPlayerMP playerMP, PlayerData data, float speed, boolean setFlying)
	{
		data.setRecallSpeed(data.getRecallSpeed() + Math.abs(speed));
		playerMP.posY += Math.signum(speed) * data.getRecallSpeed();
		playerMP.connection.setPlayerLocation(playerMP.posX, playerMP.posY, playerMP.posZ, playerMP.rotationYaw, 90);
		if(setFlying)
			playerMP.capabilities.isFlying = true;

		reset.test(playerMP);
	}

	private interface IResetCondition
	{
		void reset(EntityPlayerMP player, PlayerData data, BlockPos position, SpellRecall.RecallState newState, boolean resetRecallPosition);
	}

	private static void recallResetAtPosition(EntityPlayerMP player, PlayerData data, BlockPos position, SpellRecall.RecallState newState, boolean resetRecallPosition)
	{
		player.connection.setPlayerLocation(position.getX() + 0.5, position.up().getY(), position.getZ() + 0.5, player.rotationYaw, 90);
		data.setRecallState(newState);
		SpellUtil.syncData(player);
		player.capabilities.isFlying = false;
		data.setRecallSpeed(0);

		if(resetRecallPosition)
			data.setRecallPosition(null);
	}
}
