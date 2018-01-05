package party.lemons.arcane.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellPage;
import party.lemons.arcane.energy.EnergyMana;

/**
 * Created by Sam on 16/12/2017.
 */
public class SpellManaCharge extends Spell
{
	public SpellManaCharge(SpellPage elemental)
	{
		super(elemental);
	}

	@Override
	public void castSpellHold(Spell spell, EntityPlayer player, long holdTime)
	{
		if(!canCast(player))
			return;

		if(!player.world.isRemote && holdTime % 5 == 0)
		{
			Vec3d vec3 = player.getPositionEyes(1F);
			Vec3d look = player.getLook(1.0F);
			Vec3d vec3b = vec3.addVector(look.x * getSpellReach(), look.y * getSpellReach(), look.z * getSpellReach());

			RayTraceResult ray = player.world.rayTraceBlocks(vec3, vec3b, false, true, false);
			if(ray != null)
			{
				World world = player.world;
				BlockPos pos = ray.getBlockPos();
				if(world.getTileEntity(pos) != null)
				{
					TileEntity te = world.getTileEntity(pos);
					if(te.hasCapability(EnergyMana.MANA_ENERGY, null))
					{
						EnergyMana.IEnergyManaStorage mana = te.getCapability(EnergyMana.MANA_ENERGY, null);
						if(mana.receiveEnergy((int)getCastMana(), true) > 0)
						{
							mana.receiveEnergy((int)getCastMana(), false);
							useMana(player);
						}
					}
				}
			}
		}
	}

	@Override
	public float getSpellReach()
	{
		return 4;
	}

}
