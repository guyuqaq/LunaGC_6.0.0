package emu.grasscutter.server.packet.send;

import java.util.List;

import emu.grasscutter.net.packet.*;
import emu.grasscutter.net.proto.AvatarWearFlycloakRspOuterClass.AvatarWearFlycloakRsp;
import emu.grasscutter.net.proto.RetcodeOuterClass;

public class PacketAvatarWearFlycloakRsp extends BasePacket {
    public PacketAvatarWearFlycloakRsp(long avatarGuid, int costumeId) {
        super(PacketOpcodes.AvatarWearFlycloakRsp);

        AvatarWearFlycloakRsp proto =
                AvatarWearFlycloakRsp.newBuilder()
                        .addAvatarGuidList(avatarGuid)
                        .setFlycloakId(costumeId)
                        .build();

        this.setData(proto);
    }

    public PacketAvatarWearFlycloakRsp(List<Long> avatarGuids, int costumeId) {
        super(PacketOpcodes.AvatarWearFlycloakRsp);

        AvatarWearFlycloakRsp proto =
                AvatarWearFlycloakRsp.newBuilder()
                        .addAllAvatarGuidList(avatarGuids)
                        .setFlycloakId(costumeId)
                        .build();

        this.setData(proto);
    }

    public PacketAvatarWearFlycloakRsp() {
        super(PacketOpcodes.AvatarWearFlycloakRsp);

        AvatarWearFlycloakRsp proto =
                AvatarWearFlycloakRsp.newBuilder()
                        .setRetcode(RetcodeOuterClass.Retcode.RET_SVR_ERROR_VALUE)
                        .build();

        this.setData(proto);
    }
}