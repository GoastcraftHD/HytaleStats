package net.goastcraft.data;

import java.util.HashMap;
import java.util.Map;

public class StatData {
    private Integer version = 0;
    private Map<String, TypeData> statData = new HashMap<>();

    public StatData() {
    }

    public TypeData getTypeData(String key) {
        return statData.computeIfAbsent(key, k -> new TypeData());
    }

    public static class TypeData {
        private Map<String, EntityData> entityData = new HashMap<>();
        private Map<String, BlockData> blockData = new HashMap<>();

        public Map<String, EntityData> getEntityDataMap() {
            return entityData;
        }

        public Map<String, BlockData> getBlockDataMap() {
            return blockData;
        }
    }

    public static class EntityData {
        private String name = "";
        private int killCount = 0;
        private int deathCount = 0;

        public EntityData(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getKillCount() {
            return killCount;
        }

        public int getDeathCount() {
            return deathCount;
        }

        public void incrementKillCount() {
            this.killCount++;
        }
        public void incrementDeathCount() {
            this.deathCount++;
        }
    }

    public static class BlockData {
        private String name = "";
        private String icon = "";
        private int breakCount = 0;
        private int placeCount = 0;

        public BlockData(String name, String icon) {
            this.name = name;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }
        public String getIcon() {
            return icon;
        }

        public int getBreakCount() {
            return breakCount;
        }
        public int getPlaceCount() {
            return placeCount;
        }

        public void incrementBreakCount() {
            this.breakCount++;
        }
        public void incrementPlaceCount() {
            this.placeCount++;
        }
    }
}