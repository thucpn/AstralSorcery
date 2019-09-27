/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2019
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.perk.node.focus;

import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.perk.PerkConverter;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import hellfirepvp.astralsorcery.common.perk.node.ConstellationPerk;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: KeyAlcara
 * Created by HellFirePvP
 * Date: 25.08.2019 / 19:24
 */
public class KeyAlcara extends ConstellationPerk {

    public KeyAlcara(ResourceLocation name, int x, int y) {
        super(name, ConstellationsAS.alcara, x, y);
        setCategory(CATEGORY_FOCUS);
        this.addConverter(new PerkConverter() {
            @Nonnull
            @Override
            public PerkAttributeModifier convertModifier(PlayerEntity player, PlayerProgress progress, PerkAttributeModifier modifier, @Nullable AbstractPerk owningPerk) {
                if (modifier.getAttributeType().equals(PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP)) {
                    switch (modifier.getMode()) {
                        case ADDITION:
                        case ADDED_MULTIPLY:
                            return modifier.convertModifier(PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, modifier.getMode(), modifier.getValue(player, progress) * 0.5F);
                        case STACKING_MULTIPLY:
                            float val = modifier.getValue(player, progress) - 1;
                            val *= 0.5F;
                            return modifier.convertModifier(PerkAttributeTypesAS. ATTR_TYPE_INC_PERK_EFFECT, modifier.getMode(), val + 1F);
                        default:
                            break;
                    }
                }
                return modifier;
            }
        });
    }

    @Override
    public boolean mayUnlockPerk(PlayerProgress progress, PlayerEntity player) {
        return super.mayUnlockPerk(progress, player) &&
                !MiscUtils.contains(progress.getAppliedPerks(), perk -> perk.getCategory().equals(CATEGORY_FOCUS));
    }
}