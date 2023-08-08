package kpentchev.bug.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ChildClassRepository : JpaRepository<ChildClassEntity, Long>, JpaSpecificationExecutor<ChildClassEntity> {
}