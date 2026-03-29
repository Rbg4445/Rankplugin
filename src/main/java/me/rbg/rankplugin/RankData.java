package me.rbg.rankplugin;

public class RankData {
    private final String rank;
    private final long expireAt;

    public RankData(String rank, long expireAt) {
        this.rank = rank;
        this.expireAt = expireAt;
    }

    public String getRank() {
        return rank;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public boolean isExpired() {
        return expireAt > 0 && System.currentTimeMillis() > expireAt;
    }
}
