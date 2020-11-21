package pers.lcy.toutiao.util;

public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKEKEY = "LIKE";
    private static String BIZ_DISLIKEKEY = "DISLIKE";
    private static String BIZ_EVENT="EVENTQUEUE";
    private static String BIZ_NEWSSCORE ="NEWSSCORE";
    private static String BIZ_NEWS="NEWS";
    public static String FIELD_LIKECOUNT="likeCount";
    public static String FIELD_COMMENTCOUNT="commentCount";

    public static String getBizLikekey(int entityId, int entityType) {
        return BIZ_LIKEKEY + SPLIT + String.valueOf(entityType) +SPLIT+ String.valueOf(entityId);
    }

    public static String getBizDislikekey(int entityId, int entityType) {
        return BIZ_DISLIKEKEY + SPLIT + String.valueOf(entityType) +SPLIT+ String.valueOf(entityId);
    }
    public static String getBizEvent(){
        return BIZ_EVENT;
    }
    public static String getBizNewskey(int newsid){return BIZ_NEWS+":"+newsid;}
    public static String getBIZNewsScoreKey(){
        return BIZ_NEWSSCORE;
    }
}
