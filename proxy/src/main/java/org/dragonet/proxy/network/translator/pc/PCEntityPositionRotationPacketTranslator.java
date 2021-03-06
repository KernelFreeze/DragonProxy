/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMovementPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import sul.protocol.pocket113.play.MoveEntity;
import sul.utils.Packet;
import sul.utils.Tuples;

public class PCEntityPositionRotationPacketTranslator implements PCPacketTranslator<ServerEntityPositionRotationPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerEntityPositionRotationPacket packet) {
        CachedEntity e = session.getEntityCache().get(packet.getEntityId());
        if (e == null) {
            return null;
        }

        e.relativeMove(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ(), packet.getYaw(), packet.getPitch());

        MoveEntity pk = new MoveEntity();
        pk.entityId = e.eid;
        pk.yaw = (byte) (e.yaw / (360d / 256d));
        pk.headYaw = (byte) (e.yaw / (360d / 256d));
        pk.pitch = (byte) (e.pitch / (360d / 256d));
        pk.position = new Tuples.FloatXYZ((float) e.getX(), (float) e.getY(), (float) e.getZ());
        if(e.player){
            pk.position.y += 1.62f;
        }
        return new Packet[]{pk};
    }

}
