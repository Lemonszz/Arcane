package party.lemons.arcane.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderItem;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sam on 20/12/2017.
 */
public class Tooltip
{
	private List<String> lines;

	public Tooltip()
	{
		lines = new ArrayList<>();
	}

	public Tooltip(String... lines)
	{
		this.lines = Arrays.asList(lines);
	}

	public void add(String... lines)
	{
		this.lines.addAll(Arrays.asList(lines));
	}

	public void add(List<String> lines)
	{
		this.lines.addAll(lines);
	}

	public void draw(int xPos, int yPos, FontRenderer fontRenderer, RenderItem renderItem)
	{
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		if(lines.size() > 0)
			GuiUtils.drawHoveringText(lines, xPos, yPos, res.getScaledWidth(), res.getScaledHeight(), 200, fontRenderer);
	}
}
