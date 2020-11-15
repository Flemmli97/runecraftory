package com.flemmli97.runecraftory.mobs.events;

import com.flemmli97.runecraftory.mobs.MobModule;
import com.flemmli97.runecraftory.mobs.config.MobConfig;
import com.flemmli97.runecraftory.mobs.entity.GateEntity;
import com.flemmli97.runecraftory.mobs.spawning.GateSpawning;
import com.flemmli97.runecraftory.registry.ModEntities;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MobEvents {

    @SubscribeEvent
    public void registerSpawn(BiomeLoadingEvent event) {
        MobModule.spawnConfig.getEntityFromBiome(event.getName()).forEach(entity-> GateSpawning.addSpawn(event.getName(), entity));
        for(BiomeDictionary.Type type : BiomeDictionary.getTypes(RegistryKey.of(Registry.BIOME_KEY, event.getName())))
            MobModule.spawnConfig.getEntityFromBiomeType(type).forEach(entity-> GateSpawning.addSpawn(event.getName(), entity));
        event.getSpawns().spawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.gate, 100, 1, 1));
    }

    @SubscribeEvent
    public void disableNatural(LivingSpawnEvent.CheckSpawn event){
        if(MobConfig.disableNaturalSpawn){
            if((event.getSpawnReason() == SpawnReason.CHUNK_GENERATION || event.getSpawnReason() == SpawnReason.NATURAL) && !(event.getEntity() instanceof GateEntity))
                event.setResult(Event.Result.DENY);
        }
    }
}