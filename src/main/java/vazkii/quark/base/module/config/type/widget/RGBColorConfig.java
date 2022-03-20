package vazkii.quark.base.module.config.type.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.base.client.config.screen.CategoryScreen;
import vazkii.quark.base.client.config.screen.WidgetWrapper;
import vazkii.quark.base.client.config.screen.widgets.IWidgetProvider;
import vazkii.quark.base.module.config.Config;
import vazkii.quark.base.module.config.ConfigFlagManager;
import vazkii.quark.base.module.config.type.AbstractConfigType;

import java.util.List;
import java.util.Objects;

public class RGBColorConfig extends AbstractConfigType implements IWidgetProvider {

	@Config public double r;
	@Config public double g;
	@Config public double b;

	private int color;
	
	private RGBColorConfig(double r, double g, double b) {
		this(r, g, b, 1);
	}

	RGBColorConfig(double r, double g, double b, double a) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public static RGBColorConfig forColor(double r, double g, double b) {
		RGBColorConfig config = new RGBColorConfig(r, g, b);
		config.calculateColor();
		return config;
	}

	public int getColor() {
		return color;
	}

	@Override
	public void onReload(ConfigFlagManager flagManager) {
		color = calculateColor();
	}
	
	int calculateColor() {
		int rComponent = clamp(r * 255);
		int gComponent = clamp(g * 255) << 8;
		int bComponent = clamp(b * 255) << 16;
		int aComponent = clamp(getAlphaComponent() * 255) << 24;
		return aComponent | bComponent | gComponent | rComponent;
	}

	public double getAlphaComponent() {
		return 1.0;
	}
	
	public void inherit(RGBColorConfig other) {
		r = other.r;
		g = other.g;
		b = other.b;
		color = other.color;

		if(category != null) {
			category.refresh();
			category.updateDirty();
		}
	}

	public RGBColorConfig copy() {
		RGBColorConfig newMatrix = new RGBColorConfig(r, g, b);
		newMatrix.inherit(this);
		return newMatrix;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RGBColorConfig that = (RGBColorConfig) o;
		return Double.compare(that.r, r) == 0 && Double.compare(that.g, g) == 0 && Double.compare(that.b, b) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(r, g, b);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addWidgets(CategoryScreen parent, List<WidgetWrapper> widgets) {
		// TODO give config screens
		Minecraft minecraft = Minecraft.getInstance();
//		widgets.add(new WidgetWrapper(new PencilButton(230, 3, b -> minecraft.setScreen(new ColorInputScreen(parent, this, category)))));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public String getSubtitle() {
		return String.format("[%.1f, %.1f, %.1f]", r, g, b);
	}
	
	private static int clamp(double val) {
		return clamp((int) val);
	}

	private static int clamp(int val) {
		return Mth.clamp(val, 0, 0xFF);
	}

}