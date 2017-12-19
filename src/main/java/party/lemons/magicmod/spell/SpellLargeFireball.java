package party.lemons.magicmod.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.math.Vec3d;
import party.lemons.magicmod.api.spell.Spell;
import party.lemons.magicmod.api.spell.SpellPage;

/**
 * Created by Sam on 15/12/2017.
 */
public class SpellLargeFireball extends SpellFireball
{
	public SpellLargeFireball(SpellPage elemental)
	{
		super(elemental);
	}

	@Override
	EntityFireball createFireball(Spell spell, EntityPlayer player)
	{
		Vec3d vec = player.getLookVec();
		EntityFireball ball = new EntityLargeFireball(player.world, player, vec.x, vec.y, vec.z);
		return ball;
	}

}
