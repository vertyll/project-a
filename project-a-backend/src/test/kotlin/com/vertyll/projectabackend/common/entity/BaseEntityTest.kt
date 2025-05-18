package com.vertyll.projectabackend.common.entity

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class BaseEntityTest {
    private class TestEntity : BaseEntity()

    @Test
    fun auditFields_ShouldBeSettableAndGettable() {
        // given
        val entity = TestEntity()
        val now = LocalDateTime.now()
        val user = "testUser"

        // when
        entity.createdAt = now
        entity.updatedAt = now
        entity.createdBy = user
        entity.updatedBy = user

        // then
        assertEquals(now, entity.createdAt)
        assertEquals(now, entity.updatedAt)
        assertEquals(user, entity.createdBy)
        assertEquals(user, entity.updatedBy)
    }

    @Test
    fun id_ShouldBeSettableAndGettable() {
        // given
        val entity = TestEntity()
        val id = 1L

        // when
        entity.id = id

        // then
        assertEquals(id, entity.id)
    }
}
