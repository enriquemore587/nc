package com.bbva.intranet.senders.domain.responses;

import java.util.List;

public class TopicResp {
    private List<String> topics;
    private Integer paginationKey;
    private Boolean moreElements;

    public TopicResp() {
    }

    public TopicResp(List<String> topics, Integer paginationKey, Boolean moreElements) {

        this.topics = topics;
        this.paginationKey = paginationKey;
        this.moreElements = moreElements;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public Integer getPaginationKey() {
        return paginationKey;
    }

    public void setPaginationKey(Integer paginationKey) {
        this.paginationKey = paginationKey;
    }

    public Boolean getMoreElements() {
        return moreElements;
    }

    public void setMoreElements(Boolean moreElements) {
        this.moreElements = moreElements;
    }
}
