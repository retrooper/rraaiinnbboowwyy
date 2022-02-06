package org.hinoob.rainbowy.processor.impl;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.Column;
import com.github.retrooper.packetevents.protocol.world.chunk.TileEntity;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_8.Chunk_v1_8;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkDataBulk;
import org.hinoob.rainbowy.processor.Processor;
import org.hinoob.rainbowy.user.User;

import java.util.HashMap;
import java.util.Map;


public class BlockProcessor extends Processor {

    public BlockProcessor(User user){
        super(user);
    }

    private Map<Long, Column> chunkData = new HashMap<>();

    public void handle(PacketSendEvent event){
        if(event.getPacketType() == PacketType.Play.Server.CHUNK_DATA) {
            WrapperPlayServerChunkData wrapper = new WrapperPlayServerChunkData(event);

            user.transactionProcessor.addTask(() -> {
                long id = chunkPositionToLong(wrapper.getColumn().getX(), wrapper.getColumn().getZ());

                chunkData.put(id, wrapper.getColumn());
            });
        }else if(event.getPacketType() == PacketType.Play.Server.MAP_CHUNK_BULK){
            WrapperPlayServerChunkDataBulk wrapper = new WrapperPlayServerChunkDataBulk(event);

//            user.transactionProcessor.addTask(() -> {
//                int[] x = wrapper.getX();
//                int[] z = wrapper.getZ();
//
//                for(int i = 0; i < x.length; i++){
//                    int cX = x[i];
//                    int cZ = z[i];
//
//                    chunkData.put(chunkPositionToLong(cX >> 4, cZ >> 4), new Column(cX, cZ, true, wrapper.getChunks()[i], new TileEntity[0], wrapper.getBiomeData()[i]));
//                }
//            });
        }else if(event.getPacketType() == PacketType.Play.Server.BLOCK_CHANGE){
            WrapperPlayServerBlockChange wrapper = new WrapperPlayServerBlockChange(event);

            user.transactionProcessor.addTask(() -> {
                int x = wrapper.getBlockPosition().getX();
                int z = wrapper.getBlockPosition().getZ();
                int y = wrapper.getBlockPosition().getY();
                Column column = getChunk(x >> 4, z >> 4);
                if(column == null) return;

                BaseChunk chunk = column.getChunks()[y >> 4];

                if(chunk == null){
                    column.getChunks()[y >> 4] = new Chunk_v1_8(false);
                    chunk = column.getChunks()[y >> 4]; // Rewrite
                }

                chunk.set(x & 0xF, y & 0xF, z & 0xF, wrapper.getBlockId());
            });
        }
    }

    private long chunkPositionToLong(int x, int z) {
        return ((x & 0xFFFFFFFFL) << 32L) | (z & 0xFFFFFFFFL);
    }

    private Column getChunk(int x, int z){
        return chunkData.getOrDefault(chunkPositionToLong(x, z), null);
    }

    public WrappedBlockState getBlock(int x, int y, int z){
        Column column = getChunk(x >> 4, z >> 4);
        if(column == null) return WrappedBlockState.getByGlobalId(0);

        BaseChunk chunk = column.getChunks()[y >> 4];
        if(chunk == null) return WrappedBlockState.getByGlobalId(0);

        return chunk.get(x & 0xF, y & 0xF, z & 0xF);
    }


}
