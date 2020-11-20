package pers.lcy.toutiao.util;

public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKEKEY = "LIKE";
    private static String BIZ_DISLIKEKEY = "DISLIKE";
    private static String BIZ_EVENT="EVENTQUEUE";
    private static String BIZ_NEWS="NEWS";

    public static String getBizLikekey(int entityId, int entityType) {
        return BIZ_LIKEKEY + SPLIT + String.valueOf(entityType) +SPLIT+ String.valueOf(entityId);
    }

    public static String getBizDislikekey(int entityId, int entityType) {
        return BIZ_DISLIKEKEY + SPLIT + String.valueOf(entityType) +SPLIT+ String.valueOf(entityId);
    }
    public static String getBizEvent(){
        return BIZ_EVENT;
    }
    public static String getBIZNewsKey(){
        return BIZ_NEWS;
    }
}
