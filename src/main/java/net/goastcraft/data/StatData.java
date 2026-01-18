package net.goastcraft.data;

import java.time.Duration;
import java.time.Instant;
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
        private PlayerData playerData;

        public Map<String, EntityData> getEntityDataMap() {
            return entityData;
        }
        public Map<String, BlockData> getBlockDataMap() {
            return blockData;
        }
        public PlayerData getPlayerData() {
            return playerData;
        }

        public void setPlayerData(PlayerData playerData) {
            this.playerData = playerData;
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
        private int dropCount = 0;
        private int useCount = 0;

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
        public int getDropCount() {
            return dropCount;
        }
        public int getUseCount() {
            return useCount;
        }

        public void incrementBreakCount() {
            this.breakCount++;
        }
        public void incrementPlaceCount() {
            this.placeCount++;
        }
        public void incrementDropCount(int amount) {
            this.dropCount += amount;
        }
        public void incrementUseCount() {
            this.useCount++;
        }
    }

    public static class PlayerData {
        String firstJoinDate = "";
        String lastJoinDate = ""; // To calculate Time played
        long timePlayed = 0; // In seconds

        public PlayerData(Instant firstJoin) {
            this.firstJoinDate = firstJoin.toString();
        }

        public void setLastJoinDate(Instant lastJoin) {
            this.lastJoinDate = lastJoin.toString();
        }

        public void updateTimePlayed() {
            Instant now = Instant.now();
            this.timePlayed += Duration.between(getLastJoinDate(), now).toSeconds();
        }

        public Instant getFirstJoinDate() {
            try {
                return Instant.parse(firstJoinDate);
            } catch (Exception e) {
                return null;
            }
        }

        public Instant getLastJoinDate() {
            try {
                return Instant.parse(lastJoinDate);
            } catch (Exception e) {
                return null;
            }
        }

        public long getTimePlayed() {
            return timePlayed;
        }
    }
}