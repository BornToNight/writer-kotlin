package ru.pachan.writer_kotlin.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*


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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val id: Long = 0,
)