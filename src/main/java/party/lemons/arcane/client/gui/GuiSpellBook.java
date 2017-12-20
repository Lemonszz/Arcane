package party.lemons.arcane.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.opengl.GL11;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellPage;
import party.lemons.arcane.api.spell.SpellPages;
import party.lemons.arcane.api.spell.SpellRegistry;
import party.lemons.arcane.client.SpellUtilClient;
import party.lemons.arcane.config.ArcaneConstants;
import party.lemons.arcane.network.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sam on 10/12/2017.
 */
public class GuiSpellBook extends GuiScreen
{
	private final ResourceLocation _guiTexture = new ResourceLocation(ArcaneConstants.MODID ,"textures/gui/spellbook.png");
	private static SpellPage lastPage = SpellPages.ELEMENTAL;

	private final int _texWidth = 256;
	private final int _texHeight = 195;
	private final float _scale = 1.0F;

	private SpellPage selectedPage;
	private ButtonPageTab selectedButton;
	private ButtonStoreLevel storeLevelButton, increaseManaButton;
	private PlayerData data;

	@Nullable
	private Spell heldSpell = null;
	private Spell[] selectedSpells;

	private static HashMap<SpellPage, ArrayList<Spell>> spellCache = new HashMap<>();

	public GuiSpellBook()
	{
		this.width = 300;
		this.height = 200;

		if(spellCache.isEmpty())
		{
			buildSpellCache();
		}
	}

	public static void buildSpellCache()
	{
		for(SpellPage pa : SpellPages.pages)
		{
			ArrayList<Spell> li = new ArrayList<>();
			spellCache.put(pa, li);
		}

		for(Spell sp : SpellRegistry.SPELL_REGISTRY.getValues())
		{
			ArrayList<Spell> li = spellCache.get(sp.getSpellPage());
			li.add(sp);
		}

		for(SpellPage pa : SpellPages.pages)
		{
			spellCache.get(pa).sort(Spell::compareTo);
		}
	}

	@Override
	public void initGui()
	{
		this.buttonList.clear();
		int i = (int)(((this.width - _texWidth) / 2) / _scale);
		int j = 2;
		int _xx = i - 15;
		int _yy = j + 9;
		int ind = 0;
		selectedPage = GuiSpellBook.lastPage;
		for(SpellPage p : SpellPages.pages)
		{
			ButtonPageTab t = new ButtonPageTab(ind, _xx, _yy, p);
			if(p == selectedPage)
			{
				t = t.setSelected(true);
				selectedButton = t;
			}
			buttonList.add(t);
			ind++;
			_yy += 30;
		}
		storeLevelButton = new ButtonStoreLevel(ind++, i + 103, 27);
		buttonList.add(storeLevelButton);
		increaseManaButton = new ButtonStoreLevel(ind++, i + 103, 52);
		buttonList.add(increaseManaButton);

		data = Minecraft.getMinecraft().player.getCapability(PlayerData.CAPABILITY, null);
		selectedSpells = data.getSelectedSpells();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		Tooltip tooltip = new Tooltip();

		float sZ = itemRender.zLevel;
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPushMatrix();
		GL11.glScalef(_scale, _scale, _scale);
		this.mc.getTextureManager().bindTexture(_guiTexture);
		int i = (int)(((this.width - _texWidth) / 2) / _scale);
		int j = 2;

		this.mc.getTextureManager().bindTexture(_guiTexture);
		GuiUtils.drawTexturedModalRect(i, j, 0, 0, _texWidth, _texHeight, 0);
		GuiUtils.drawTexturedModalRect(i + 45, 22, 127, 196, 78, 24, 0);
		GuiUtils.drawTexturedModalRect(i + 45, 47, 127, 196, 78, 24, 0);

		for(int a = 0; a < 6; a++)
		{
			int xx = i + 13;
			int yy = (j + 20) + (25 * a);
			if(data.getSelectedIndex() == a)
				GuiUtils.drawTexturedModalRect(xx, yy, 99, 195, 24, 24, 0);
			else
				GuiUtils.drawTexturedModalRect(xx, yy, 0, 224, 24, 24, 0);

			if(selectedSpells[a] != null)
			{
				SpellUtilClient.drawSpell(xx + 4, yy + 4, selectedSpells[a]);
				this.mc.getTextureManager().bindTexture(_guiTexture);

				if(mouseX >= xx && mouseX <= xx + 24 && mouseY >= yy && mouseY <= yy + 24)
				{
					Spell sp = selectedSpells[a];
					tooltip = SpellUtilClient.createSpellTooltip(sp, data);
				}
			}
			this.mc.getTextureManager().bindTexture(_guiTexture);

		}
		fontRenderer.drawString(String.valueOf(data.getStoredLevels()), i + 53, 31, 0x66a0ff);
		fontRenderer.drawString(String.valueOf((int)data.getMana() + "/" + (int)data.getMaxMana()), i + 53, 55, 0x66a0ff);
		drawString(fontRenderer, String.valueOf(I18n.format("magic.storedlevels")), i + 50, 20, 8453920);
		drawString(fontRenderer, String.valueOf(I18n.format("magic.maxmana")), i + 50, 45, 8453920);
		storeLevelButton.enabled = true;
		if(mc.player.experienceLevel < 10)
		{
			storeLevelButton.enabled = false;
			increaseManaButton.enabled = false;
		}

		this.mc.getTextureManager().bindTexture(_guiTexture);
		GlStateManager.color(1F, 1F, 1F);
		int _initX = i + 137;
		int xp = _initX + 1;
		int yp = 22;
		int ind = 0;
		for(int yy = 0; yy < 5; yy++)
		{
			for(int xx = 0; xx < 4; xx++)
			{
				if(ind > spellCache.get(selectedPage).size() - 1)
					break;

				if(data.canUnlockSpell(spellCache.get(selectedPage).get(ind)) || data.hasSpellUnlocked(spellCache.get(selectedPage).get(ind)))
				{
					int uv = 56;
					if(data.hasSpellUnlocked(spellCache.get(selectedPage).get(ind)))
						uv -= 25;
					GuiUtils.drawTexturedModalRect(xp, yp, uv, 195, 25, 25, 0);

					if(mouseX >= xp && mouseX <= xp + 26 && mouseY >= yp && mouseY <= yp + 26)
					{
						Spell sp = spellCache.get(selectedPage).get(ind);
						tooltip = SpellUtilClient.createSpellTooltip(sp, data);


						GL11.glColor3f(1,1,1);
						this.mc.getTextureManager().bindTexture(_guiTexture);
					}
					xp += 26;
				}
				else
				{
					xx -= 1;
				}
				ind++;
			}
			xp =  _initX + 1;
			yp += 26;
		}

		GuiUtils.drawTexturedModalRect(_initX + 5, yp + 6, 96, 239, 11, 17, 0);
		GuiUtils.drawTexturedModalRect(_initX + 89, yp + 6, 82, 239, 11, 17, 0);
		this.fontRenderer.drawString("1/1", _initX + 42, yp + 12, 4210752);

		String s = selectedPage.getLocalizedName();
		this.fontRenderer.drawString(s, (_initX), j + 10, 4210752);
		GL11.glScalef(1F, 1F, 1F);
		GL11.glPopMatrix();

		int _xx = _initX  + 5;
		int _yy = 22 + 4;
		int in = 0;
		for(Spell sp : spellCache.get(selectedPage))
		{
			if(data.hasSpellUnlocked(sp) || data.canUnlockSpell(sp))
			{
				SpellUtilClient.drawSpell(_xx, _yy, sp);
				in++;
				_xx += 26;
				if(in >= 4)
				{
					in = 0;
					_xx = _initX + 5;
					_yy += 26;
				}
			}
		}

		if(heldSpell != null)
		{
			SpellUtilClient.drawSpell(mouseX, mouseY, heldSpell);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
		for(GuiButton button : buttonList)
			if(button instanceof ButtonPageTab)
				((ButtonPageTab)button).drawHover(mc, mouseX, mouseY);

		itemRender.zLevel = sZ;

		tooltip.draw(mouseX, mouseY, fontRenderer, itemRender);

		ArrayList<String> str = new ArrayList<>();
		str.add(I18n.format("magic.requiredxp") + ": " + Minecraft.getMinecraft().player.experienceLevel  +"/10");
		if(increaseManaButton.isMouseOver() || storeLevelButton.isMouseOver())
				GuiUtils.drawHoveringText(str, mouseX, mouseY, mc.displayWidth, mc.displayHeight, 250, mc.fontRenderer);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(button.id < SpellPages.pages.size())
		{
			selectedButton.setSelected(false);			//turn off old

			selectedButton = (ButtonPageTab) button;	//set new button as selected
			selectedButton.setSelected(true);			//set it to selected state
			selectedPage = selectedButton.getPage();	//Set selected page
			GuiSpellBook.lastPage = selectedPage;
		}

		if(button.id == storeLevelButton.id)
		{
			if(mc.player.experienceLevel >= 10)
			{
				mc.player.addExperienceLevel(-10);
				data.setStoredLevels(data.getStoredLevels() + 1);
				ArcaneNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendAddPoint());
			}
		}

		if(button.id == increaseManaButton.id)
		{
			if(mc.player.experienceLevel >= 10)
			{
				mc.player.addExperienceLevel(-10);
				data.setMaxMana(data.getMaxMana() + 5);
				ArcaneNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendAddMaxMana());
			}
		}

		super.actionPerformed(button);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		int i = (int) (((this.width - _texWidth) / 2) / _scale);
		int _initX = i + 138;
		boolean shift = GuiScreen.isShiftKeyDown();
		if(mouseButton == 0)
		{
			int in = 0;
			int _yy = 22;
			int _xx = _initX;

			if(mouseX < i || mouseX > i + 255 || mouseY < 2 || mouseY > 255)
			{
				heldSpell = null;
			}

			for(Spell sp : spellCache.get(selectedPage))
			{
				if(data.hasSpellUnlocked(sp) || data.canUnlockSpell(sp))
				{
					if(mouseX >= _xx && mouseX <= _xx + 26 && mouseY >= _yy && mouseY <= _yy + 26)
					{
						if(shift && data.hasSpellUnlocked(sp))
						{
							for(int v = 0; v < selectedSpells.length; v++)
							{
								if(selectedSpells[v] == null)
								{
									selectedSpells[v] = sp;
									break;
								}
							}
						}
						else
						{
							if(data.hasSpellUnlocked(sp))
								heldSpell = sp;
							else
								if(data.getStoredLevels() >= sp.getUnlockCost())
								{
									data.setStoredLevels(data.getStoredLevels() - sp.getUnlockCost());
									data.unlockSpell(sp);

									ArcaneNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendUnlockSpell(sp.getRegistryName().toString()));
								}
							break;
						}
					}
					in++;
					_xx += 26;
					if(in >= 4)
					{
						in = 0;
						_xx = _initX + 5;
						_yy += 26;
					}
				}
			}
		}


		int startX = i + 13;
		int startY = 22;
		int gapSize = 25;
		int width = 23;
		int height = 23;
		int yy = startY;

		for(int b = 0; b < 6; b++)
		{
			if(mouseX >= startX && mouseX <= startX + width)
			{
				if(mouseY >= yy && mouseY <= yy + height)
				{
					if(mouseButton == 1)
					{
						selectedSpells[b] = null;
						break;
					}else if(heldSpell != null)
					{
						selectedSpells[b] = heldSpell;
						heldSpell = null;
						break;
					}
					else
					{
						data.setSelectedIndex(b);
						ArcaneNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendSelectedSpell(b));
					}
				}
			}
			yy += gapSize;
		}

		if(mouseButton == 1)
		{
			heldSpell = null;
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onGuiClosed()
	{
		for(int i = 0; i < selectedSpells.length; i++)
		{
			if(selectedSpells[i] != null)
			{
				ArcaneNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendChosenSpell(selectedSpells[i].getRegistryName().toString(), i));
			}
			else
			{
				ArcaneNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendChosenNullSpell(i));
			}
		}
		super.onGuiClosed();
	}
}
