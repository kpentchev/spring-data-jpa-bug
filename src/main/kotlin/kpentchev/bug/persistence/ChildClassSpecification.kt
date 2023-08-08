package kpentchev.bug.persistence

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

class ChildClassSpecification(
    private val inheritedPropertyFilter: String,
    private val ownPropertyFilter: String,
    private val relatedNameFilter: Set<String>
) : Specification<ChildClassEntity> {

    override fun toPredicate(
        root: Root<ChildClassEntity>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        query.distinct(true)
        val criteria = mutableListOf<Predicate>()

        criteria.add(
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get(ChildClassEntity::inheritedProperty.name)),
                "%${inheritedPropertyFilter.lowercase()}%"
            )
        )

        criteria.add(
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get(ChildClassEntity::ownProperty.name)),
                "%${ownPropertyFilter.lowercase()}%"
            )
        )

        if (relatedNameFilter.isNotEmpty()) {
            val join = root.join<ChildClassEntity, RelationshipEntity>(ChildClassEntity::relationships.name)
                .get<RelatedEntity>(RelationshipEntity::related.name)
            criteria.add(
                join.get<String>(RelatedEntity::name.name).`in`(relatedNameFilter.map { it })
            )
        }

        return criteriaBuilder.and(*criteria.toTypedArray())
    }

}