package party.lemons.magicmod.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.math.Vec3d;
import party.lemons.magicmod.api.spell.Spell;
import party.lemons.magicmod.api.spell.SpellPage;
import party.lemons.magicmod.entity.EntityFirestormProjectile;

/**
 * Created by Sam on 15/12/2017.
 */
public class SpellDragonFireball extends SpellFireball
{
	public SpellDragonFireball(SpellPage elemental)
	{
		super(elemental);
	}

	@Override
	EntityFireball createFireball(Spell spell, EntityPlayer player)
	{
		Vec3d vec = player.getLookVec();
		EntityFireball ball = new EntityFirestormProjectile(player.world, player, vec.x, vec.y, vec.z);
		return ball;
	}

}
