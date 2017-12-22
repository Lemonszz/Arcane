package party.lemons.arcane.spell;

import net.minecraft.entity.player.EntityPlayer;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellPage;

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
		startRecall(player);
	}

	private void startRecall(EntityPlayer player)
	{
		PlayerData data = player.getCapability(PlayerData.CAPABILITY, null);
		data.startRecall(player);
	}

	public enum RecallState
	{
		NONE,
		UP,
		DOWN
	}
}
