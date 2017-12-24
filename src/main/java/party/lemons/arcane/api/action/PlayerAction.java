package party.lemons.arcane.api.action;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Created by Sam on 24/12/2017.
 */
public class PlayerAction extends IForgeRegistryEntry.Impl<PlayerAction>
{
	public static PlayerAction NONE;

	public PlayerAction()
	{

	}

	public boolean onTakeDamage(EntityPlayer player, DamageSource source, ActionState state)
	{
		return true;
	}

	public void onActionUpdate(EntityPlayer player, ActionState state)
	{

	}

	public void onActionStart(EntityPlayer player, ActionState state)
	{

	}

	public void onActionEnd(EntityPlayer player, ActionState state)
	{

	}
}
