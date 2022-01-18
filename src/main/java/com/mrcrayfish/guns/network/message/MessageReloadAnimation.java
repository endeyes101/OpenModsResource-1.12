package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.MrCrayfishMod;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Author: MrCrayfish
 */
public class MessageReloadAnimation implements IMessage, IMessageHandler<MessageReloadAnimation, IMessage>
{
    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(MessageReloadAnimation message, MessageContext ctx)
    {
        MrCrayfishMod.proxy.startReloadAnimation();
        return null;
    }
}
