package com.liaoyb.qingqing.uaa.repository;

import com.liaoyb.qingqing.uaa.domain.Permission;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Permission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query(value = "select permission.* from qingqing_permission permission JOIN qingqing_role_permission_relation permission_relation ON permission_relation.permission_id = permission.id where permission_relation.role_id in(:roleIds)", nativeQuery = true)
    List<Permission> findWithRelationships(@Param("roleIds") Iterable<Long> roleIds);
}
