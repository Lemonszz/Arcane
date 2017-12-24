package party.lemons.arcane.spell;

import net.minecraft.entity.player.EntityPlayer;
import party.lemons.arcane.api.action.ActionState;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellPage;
import party.lemons.arcane.spell.action.ArcaneActions;

/**
 * Created by Sam on 22/12/2017.
 */
public class SpellRecall extends Spell
{
	public SpellRecall(SpellPage page)
	{
		super(page);
	}

	public void castSpellPress(Spell spell, EntityPlayer player)
	{
		PlayerData data = player.getCapability(PlayerData.CAPABILITY, null);
		data.setActionState(player, new ActionState(ArcaneActions.RECALL_UP));
	}

	private void startRecall(EntityPlayer player)
	{
		//data.startRecall(player);
	}

	public enum RecallState
	{
		NONE,
		UP,
		DOWN
	}
}
