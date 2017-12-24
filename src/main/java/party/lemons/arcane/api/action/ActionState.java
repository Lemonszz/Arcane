package party.lemons.arcane.api.action;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Created by Sam on 24/12/2017.
 */
@Nonnull
public class ActionState
{
	private NBTTagCompound tags;
	private PlayerAction action;

	public ActionState(PlayerAction action)
	{
		this(action, new NBTTagCompound());
	}

	public ActionState(NBTTagCompound tags)
	{
		readFromNBT(tags);
	}

	public ActionState(PlayerAction action, NBTTagCompound tags)
	{
		this.action = action;
		this.tags = tags;
	}

	public PlayerAction getAction()
	{
		return action;
	}

	public void setTagCompound(NBTTagCompound tags)
	{
		this.tags = tags;
	}

	public NBTTagCompound getTagCompound()
	{
		return tags;
	}

	public NBTTagCompound writeToNBT()
	{
		NBTTagCompound holder = new NBTTagCompound();
		holder.setString("action", action.getRegistryName().toString());
		holder.setTag("action_tags", tags);
		return holder;
	}

	public void readFromNBT(NBTTagCompound tags)
	{
		PlayerAction action = ActionRegistry.ACTION_REGISTRY.getValue(new ResourceLocation(tags.getString("action")));
		if(action == null)
		{
			this.action = PlayerAction.NONE;
			this.tags = new NBTTagCompound();
			return;
		}
		this.action = action;
		setTagCompound((NBTTagCompound) tags.getTag("action_tags"));
	}
}