package ru.pachan.writer_kotlin.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.pachan.writer_kotlin.dto.WriterDto
import ru.pachan.writer_kotlin.model.Notification
import ru.pachan.writer_kotlin.repository.NotificationRepository

@Service
class WriterDtoConsumer(
    private val repository: NotificationRepository,
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun accept(writerDtoList: List<WriterDto>) {
        logger.info("start WriterDtoConsumerImpl.accept for {}", writerDtoList)

        val groupedData = writerDtoList.groupBy({ it.personId }, { it.count })
            .mapValues { (_, values) -> values.sum() }

        val notificationMap = repository.findAll(groupedData.keys).associateBy { it.personId }

        val notificationList = groupedData.entries.map {
            val personId = it.key
            val count = it.value

            var notification = notificationMap[personId]
            if (notification != null) {
                notification.count = notification.count + count
            } else {
                notification = Notification(
                    personId = personId,
                    count = count.toLong()
                )
            }
            notification
        }
        repository.saveAll(notificationList)
    }

}