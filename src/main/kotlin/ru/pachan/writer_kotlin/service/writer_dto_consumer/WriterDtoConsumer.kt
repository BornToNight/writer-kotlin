package ru.pachan.writer_kotlin.service.writer_dto_consumer

import ru.pachan.writer_kotlin.dto.WriterDto

interface WriterDtoConsumer {

    fun accept(writerDtoList: List<WriterDto>)

}