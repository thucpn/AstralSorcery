package hellfirepvp.astralsorcery.common.network.packet.server;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.entities.EntityGrindstone;
import hellfirepvp.astralsorcery.common.tile.TileGrindstone;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: PktPlayEntityEffect
 * Created by HellFirePvP
 * Date: 10.11.2016 / 12:47
 */
public class PktPlayEffect implements IMessage, IMessageHandler<PktPlayEffect, IMessage> {

    private byte typeOrdinal;
    public BlockPos pos;

    public PktPlayEffect() {}

    public PktPlayEffect(EffectType type, BlockPos pos) {
        this.typeOrdinal = (byte) type.ordinal();
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.typeOrdinal = buf.readByte();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(typeOrdinal);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public IMessage onMessage(PktPlayEffect message, MessageContext ctx) {
        try {
            EffectType type = EffectType.values()[message.typeOrdinal];
            EventAction trigger = type.getTrigger(ctx.side);
            if(trigger != null) {
                trigger.trigger(message);
            }
        } catch (Exception exc) {
            AstralSorcery.log.warn("Error executing ParticleEventType " + message.typeOrdinal + " for pos " + pos.toString());
        }
        return null;
    }

    public static enum EffectType {

        //DEFINE EVENT TRIGGER IN THE FCKING HUGE SWITCH STATEMENT DOWN TEHRE.
        GRINDSTONE_WHEEL;

        //GOD I HATE THIS PART
        //But i can't do this in the ctor because server-client stuffs.
        @SideOnly(Side.CLIENT)
        private static EventAction getClientTrigger(EffectType type) {
            switch (type) {
                case GRINDSTONE_WHEEL:
                    return TileGrindstone::playWheelAnimation;
            }
            return null;
        }

        public EventAction getTrigger(Side side) {
            if(!side.isClient()) return null;
            return getClientTrigger(this);
        }

    }

    private static interface EventAction {

        public void trigger(PktPlayEffect event);

    }

}