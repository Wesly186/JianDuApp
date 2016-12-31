package com.mialab.jiandu.entity;

/**
 * 贡献榜
 */

public class Rank extends User {

    private int rankNum;
    private int publishNum;

    public int getRankNum() {
        return rankNum;
    }

    public void setRankNum(int rankNum) {
        this.rankNum = rankNum;
    }

    public int getPublishNum() {
        return publishNum;
    }

    public void setPublishNum(int publishNum) {
        this.publishNum = publishNum;
    }
}
