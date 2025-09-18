package ru.pachan.writer_kotlin.config

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor
import ru.pachan.writer_kotlin.dto.WriterDto
import ru.pachan.writer_kotlin.service.WriterDtoConsumer

@Configuration
class KafkaConfig {

    @Value("\${application.kafka.topic}")
    lateinit var topicName: String

    @Bean
    fun consumerFactory(kafkaProperties: KafkaProperties): ConsumerFactory<String, WriterDto> {
        val props = kafkaProperties.buildConsumerProperties()
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer::class.java)
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 3)
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 3000)

        val kafkaConsumerFactory = DefaultKafkaConsumerFactory<String, WriterDto>(props)
        kafkaConsumerFactory.setValueDeserializer(JsonDeserializer(WriterDto::class.java, false))
        return kafkaConsumerFactory
    }

    @Bean("listenerContainerFactory")
    fun listenerContainerFactory(
        consumerFactory: ConsumerFactory<String, WriterDto>,
    ): ConcurrentKafkaListenerContainerFactory<String, WriterDto> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, WriterDto>()
        factory.consumerFactory = consumerFactory
        factory.isBatchListener = true
        factory.setConcurrency(1)
        factory.containerProperties.idleBetweenPolls = 1000
        factory.containerProperties.pollTimeout = 1000
        val executor = SimpleAsyncTaskExecutor("k-consumer-")
        executor.concurrencyLimit = 10
        val listenerTaskExecutor = ConcurrentTaskExecutor(executor)
        factory.containerProperties.listenerTaskExecutor = listenerTaskExecutor
        return factory
    }

    @Bean
    fun topic(): NewTopic {
        return TopicBuilder.name(topicName).partitions(1).replicas(1).build()
    }

    @Bean
    fun kafkaClient(writerDtoConsumer: WriterDtoConsumer): KafkaClient {
        return KafkaClient(writerDtoConsumer)
    }

    class KafkaClient(private val writerDtoConsumer: WriterDtoConsumer) {

        @KafkaListener(topics = ["\${application.kafka.topic}"], containerFactory = "listenerContainerFactory")
        fun listen(@Payload writerDtoList: List<WriterDto>) {
            writerDtoConsumer.accept(writerDtoList)
        }
    }

}