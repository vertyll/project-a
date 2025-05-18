package com.vertyll.projectabackend.role.enums

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RoleTypeTest {
    @Test
    fun `enum should have correct number of values`() {
        // given/when
        val values = RoleType.entries

        // then
        assertEquals(4, values.size)
    }

    @Test
    fun `enum should contain expected values`() {
        // given/when/then
        assertTrue(RoleType.entries.contains(RoleType.ADMIN))
        assertTrue(RoleType.entries.contains(RoleType.USER))
        assertTrue(RoleType.entries.contains(RoleType.MANAGER))
        assertTrue(RoleType.entries.contains(RoleType.EMPLOYEE))
    }

    @Test
    fun `getAuthority should return correct authority string`() {
        // given/when/then
        assertEquals("ROLE_ADMIN", RoleType.ADMIN.getAuthority())
        assertEquals("ROLE_USER", RoleType.USER.getAuthority())
        assertEquals("ROLE_MANAGER", RoleType.MANAGER.getAuthority())
        assertEquals("ROLE_EMPLOYEE", RoleType.EMPLOYEE.getAuthority())
    }

    @Test
    fun `valueOf should return correct enum value`() {
        // given/when/then
        assertSame(RoleType.ADMIN, RoleType.valueOf("ADMIN"))
        assertSame(RoleType.USER, RoleType.valueOf("USER"))
        assertSame(RoleType.MANAGER, RoleType.valueOf("MANAGER"))
        assertSame(RoleType.EMPLOYEE, RoleType.valueOf("EMPLOYEE"))
    }
}
