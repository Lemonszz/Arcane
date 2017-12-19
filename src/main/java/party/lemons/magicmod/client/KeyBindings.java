package party.lemons.magicmod.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;
import party.lemons.magicmod.config.ModConstants;

/**
 * Created by Sam on 10/12/2017.
 */
public class KeyBindings
{
	public static KeyBinding keySpellbook, keyRadialMenu, keyCastSpell;

	public static void init()
	{
		keySpellbook = new KeyBinding("Spell Book", Keyboard.KEY_B, ModConstants.MODNAME);
		keyRadialMenu = new KeyBinding("Radial Menu", Keyboard.KEY_GRAVE, ModConstants.MODNAME);
		keyCastSpell = new KeyBinding("Cast Spell", Keyboard.KEY_R, ModConstants.MODNAME);

		ClientRegistry.registerKeyBinding(keySpellbook);
		ClientRegistry.registerKeyBinding(keyRadialMenu);
		ClientRegistry.registerKeyBinding(keyCastSpell);
	}
}
