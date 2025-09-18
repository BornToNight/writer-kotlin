package ru.pachan.writer_kotlin.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table


@Entity
@Table(name = "notifications")
class Notification(

    @Column(nullable = false, unique = true)
    val personId: Long = 0,

    @Column(nullable = false)
    var count: Long = 0,

    @Id
    @SequenceGenerator(
        name = "notifications_seq",
        sequenceName = "notifications_seq",
        allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifications_seq")
    @Column(name = "notification_id")
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val id: Long = 0,
)