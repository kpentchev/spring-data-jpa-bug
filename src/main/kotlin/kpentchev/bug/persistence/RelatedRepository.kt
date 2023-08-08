package kpentchev.bug.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface RelatedRepository : JpaRepository<RelatedEntity, Long> {
}