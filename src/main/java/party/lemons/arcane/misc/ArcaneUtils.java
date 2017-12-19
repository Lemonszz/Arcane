package party.lemons.arcane.misc;

import java.util.Random;

/**
 * Created by Sam on 19/12/2017.
 */
public class ArcaneUtils
{
	public static <T> T choose(Random random, T... items)
	{
		T chosen = items[random.nextInt(items.length)];
		return chosen;
	}
}
