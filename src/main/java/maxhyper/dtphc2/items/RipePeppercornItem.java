package maxhyper.dtphc2.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class RipePeppercornItem extends Item {

    public RipePeppercornItem(Properties properties) {
        super(properties);
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack pStack, @Nullable World pLevel, List<ITextComponent> pTooltip, @Nonnull ITooltipFlag pFlag) {
        pTooltip.add(new TranslationTextComponent("tooltip.dtphc2.ripe_peppercorn_item"));
    }

}
