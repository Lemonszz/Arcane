package party.lemons.arcane.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;
import party.lemons.arcane.config.ArcaneConstants;

/**
 * Created by Sam on 10/12/2017.
 */
public class KeyBindings
{
	public static KeyBinding keySpellbook, keyRadialMenu, keyCastSpell;

	public static void init()
	{
		keySpellbook = new KeyBinding("Spell Book", Keyboard.KEY_B, ArcaneConstants.MODNAME);
		keyRadialMenu = new KeyBinding("Radial Menu", Keyboard.KEY_GRAVE, ArcaneConstants.MODNAME);
		keyCastSpell = new KeyBinding("Cast Spell", Keyboard.KEY_R, ArcaneConstants.MODNAME);

		ClientRegistry.registerKeyBinding(keySpellbook);
		ClientRegistry.registerKeyBinding(keyRadialMenu);
		ClientRegistry.registerKeyBinding(keyCastSpell);
	}
}
