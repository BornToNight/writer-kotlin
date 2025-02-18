package ru.pachan.writer_kotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.pachan.writer_kotlin.model.Notification

@Repository
interface NotificationRepository : JpaRepository<Notification, Long> {

    @Query(
        "SELECT n" +
        " FROM Notification n" +
            " WHERE" +
            " (n.personId IN (:personIds))"
    )
    fun findAll(@Param("personIds") personIds: Set<Long>): List<Notification>

}