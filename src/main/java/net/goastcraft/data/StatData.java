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

        public Map<String, EntityData> getEntityDataMap() {
            return entityData;
        }
    }

    public static class EntityData {
        private String name = "";
        private int count = 0;

        public EntityData(String roleName, int count) {
            this.name = roleName;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public Integer getCount() {
            return count;
        }

        public void incrementCount() {
            this.count++;
        }
    }
}