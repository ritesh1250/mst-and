package com.meest.videomvvmmodule.view.recordvideo.ARGearAsset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ARGearDataModel {

    @SerializedName("api_key")
    @Expose
    private String apiKey;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("categories")
    @Expose
    private List<ARGearCategory> categories = null;
    @SerializedName("last_updated_at")
    @Expose
    private long lastUpdatedAt;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ARGearCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ARGearCategory> categories) {
        this.categories = categories;
    }

    public long getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(long lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public class ARGearCategory {

        @SerializedName("uuid")
        @Expose
        private String uuid;
        @SerializedName("slot_no")
        @Expose
        private Integer slotNo;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("is_bundle")
        @Expose
        private Boolean isBundle;
        @SerializedName("updated_at")
        @Expose
        private long updatedAt;
        @SerializedName("items")
        @Expose
        private List<ARGearItem> ARGearItems = null;
        @SerializedName("division")
        @Expose
        private Integer division;
        @SerializedName("level")
        @Expose
        private Integer level;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("countries")
        @Expose
        private String countries;
        @SerializedName("parentCategoryUuid")
        @Expose
        private String parentCategoryUuid;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public Integer getSlotNo() {
            return slotNo;
        }

        public void setSlotNo(Integer slotNo) {
            this.slotNo = slotNo;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Boolean getIsBundle() {
            return isBundle;
        }

        public void setIsBundle(Boolean isBundle) {
            this.isBundle = isBundle;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<ARGearItem> getARGearItems() {
            return ARGearItems;
        }

        public void setARGearItems(List<ARGearItem> ARGearItems) {
            this.ARGearItems = ARGearItems;
        }

        public Integer getDivision() {
            return division;
        }

        public void setDivision(Integer division) {
            this.division = division;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCountries() {
            return countries;
        }

        public void setCountries(String countries) {
            this.countries = countries;
        }

        public String getParentCategoryUuid() {
            return parentCategoryUuid;
        }

        public void setParentCategoryUuid(String parentCategoryUuid) {
            this.parentCategoryUuid = parentCategoryUuid;
        }

    }

    public class ARGearItem {

        @SerializedName("uuid")
        @Expose
        private String uuid;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("zip_file")
        @Expose
        private String zipFile;
        @SerializedName("num_stickers")
        @Expose
        private Integer numStickers;
        @SerializedName("num_effects")
        @Expose
        private Integer numEffects;
        @SerializedName("num_bgms")
        @Expose
        private Integer numBgms;
        @SerializedName("num_filters")
        @Expose
        private Integer numFilters;
        @SerializedName("num_masks")
        @Expose
        private Integer numMasks;
        @SerializedName("has_trigger")
        @Expose
        private Boolean hasTrigger;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("updated_at")
        @Expose
        private long updatedAt;
        @SerializedName("big_thumbnail")
        @Expose
        private String bigThumbnail;
        @SerializedName("type")
        @Expose
        private String type;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getZipFile() {
            return zipFile;
        }

        public void setZipFile(String zipFile) {
            this.zipFile = zipFile;
        }

        public Integer getNumStickers() {
            return numStickers;
        }

        public void setNumStickers(Integer numStickers) {
            this.numStickers = numStickers;
        }

        public Integer getNumEffects() {
            return numEffects;
        }

        public void setNumEffects(Integer numEffects) {
            this.numEffects = numEffects;
        }

        public Integer getNumBgms() {
            return numBgms;
        }

        public void setNumBgms(Integer numBgms) {
            this.numBgms = numBgms;
        }

        public Integer getNumFilters() {
            return numFilters;
        }

        public void setNumFilters(Integer numFilters) {
            this.numFilters = numFilters;
        }

        public Integer getNumMasks() {
            return numMasks;
        }

        public void setNumMasks(Integer numMasks) {
            this.numMasks = numMasks;
        }

        public Boolean getHasTrigger() {
            return hasTrigger;
        }

        public void setHasTrigger(Boolean hasTrigger) {
            this.hasTrigger = hasTrigger;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getBigThumbnail() {
            return bigThumbnail;
        }

        public void setBigThumbnail(String bigThumbnail) {
            this.bigThumbnail = bigThumbnail;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

}
