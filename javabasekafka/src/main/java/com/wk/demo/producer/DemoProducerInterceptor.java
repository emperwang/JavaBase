package com.wk.demo.producer;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 *  发送拦截器实现
 */
public class DemoProducerInterceptor implements ProducerInterceptor<String,String>{

    private Long succCount;
    private Long failCount;

    // 要发送
    // 发送前给消息添加一个前缀
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
        String value = "prefix-" + producerRecord.value();

        return new ProducerRecord<String, String>(producerRecord.topic(),
                producerRecord.partition(),
                producerRecord.timestamp(),
                producerRecord.key(),
                value,
                producerRecord.headers());
    }
    // 消息被应答之前  或  消息发送失败时调用
    // 优先于用户定义的callback 调用
    // 进行一个统计，计算发送成功和失败的次数
    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        if (e == null){
            succCount++;
        }else {
            failCount++;
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
