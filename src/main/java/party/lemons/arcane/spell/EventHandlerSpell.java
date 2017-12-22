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
					data.setRecallSpeed(data.getRecallSpeed() + speed);
					player.capabilities.isFlying = true;
					player.posY += data.getRecallSpeed();
					playerMP.connection.setPlayerLocation(player.posX, player.posY, player.posZ, playerMP.rotationYaw, 90);
					if(player.posY >= maxY)
					{
						player.capabilities.isFlying = false;
						data.setRecallState(SpellRecall.RecallState.DOWN);
						SpellUtil.syncData(player);
						playerMP.connection.setPlayerLocation(spawnPos.getX() + 0.5, ((EntityPlayerMP) player).posY, spawnPos.getZ() + 0.5,  playerMP.rotationYaw, 90);
						data.setRecallSpeed(0);
					}

					break;
				case DOWN:
					data.setRecallSpeed(data.getRecallSpeed() + speed);
					player.posY -= data.getRecallSpeed();
					playerMP.connection.setPlayerLocation(playerMP.posX, player.posY, player.posZ, playerMP.rotationYaw, 90);
					if(playerMP.posY <= spawnPos.getY())
					{
						playerMP.connection.setPlayerLocation(spawnPos.getX() + 0.5, spawnPos.up().getY(), spawnPos.getZ() + 0.5, playerMP.rotationYaw, 90);
						data.setRecallState(SpellRecall.RecallState.NONE);
						SpellUtil.syncData(player);
						player.capabilities.isFlying = false;
						data.setRecallPosition(null);
						data.setRecallSpeed(0);
					}
					break;
			}
		}
	}
}
