package emu.grasscutter.server.packet.recv;

import emu.grasscutter.net.packet.*;
import emu.grasscutter.net.proto.AvatarWearFlycloakReqOuterClass.AvatarWearFlycloakReq;
import emu.grasscutter.server.game.GameSession;
import emu.grasscutter.server.packet.send.PacketAvatarWearFlycloakRsp;

@Opcodes(PacketOpcodes.AvatarWearFlycloakReq)
public class HandlerAvatarWearFlycloakReq extends PacketHandler {
    @Override
    public void handle(GameSession session, byte[] header, byte[] payload) throws Exception {
        AvatarWearFlycloakReq req = AvatarWearFlycloakReq.parseFrom(payload);

        boolean success = true;
        for (Long guidObj : req.getAvatarGuidListList()) {
            long guid = guidObj.longValue();
            if (!session.getPlayer().getAvatars().wearFlycloak(guid, req.getFlycloakId())) {
                success = false;
                break;
            }
        }

        if (success) {
            session
                .getPlayer()
                .sendPacket(new PacketAvatarWearFlycloakRsp(req.getAvatarGuidListList(), req.getFlycloakId()));
        } else {
            session.getPlayer().sendPacket(new PacketAvatarWearFlycloakRsp());
        }
    }
}
