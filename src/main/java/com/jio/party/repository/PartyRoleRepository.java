package com.jio.party.repository;

import com.jio.party.entity.PartyRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartyRoleRepository extends JpaRepository<PartyRole, String> {

    List<PartyRole> findByRoleType(String roleType);
    List<PartyRole> findByStatus(String status);
    List<PartyRole> findByRoleTypeAndStatus(String roleType, String status);
}
