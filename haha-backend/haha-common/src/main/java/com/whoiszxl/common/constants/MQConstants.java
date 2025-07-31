package com.whoiszxl.common.constants;

public class MQConstants {

    public interface Game {
        String REVIEW_VOTE_MESSAGE_TOPIC = "REVIEW_VOTE_MESSAGE_TOPIC";
        String REVIEW_PUBLISH_MESSAGE_TOPIC = "REVIEW_PUBLISH_MESSAGE_TOPIC";
        String REVIEW_COMMENT_PUBLISH_MESSAGE_TOPIC = "REVIEW_COMMENT_PUBLISH_MESSAGE_TOPIC";
    }

    public interface Order {
        String SECKILL_PLACE_ORDER_TOPIC = "SECKILL_PLACE_ORDER_TOPIC";
    }
}
