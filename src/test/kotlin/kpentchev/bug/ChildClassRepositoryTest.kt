package kpentchev.bug

import kpentchev.bug.persistence.ChildClassEntity
import kpentchev.bug.persistence.ChildClassRepository
import kpentchev.bug.persistence.ChildClassSpecification
import kpentchev.bug.persistence.RelatedEntity
import kpentchev.bug.persistence.RelatedRepository
import kpentchev.bug.persistence.RelationshipEntity
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest(showSql = true)
@Import(EntityTestConfig::class)
@TestPropertySource(properties = ["spring.jpa.hibernate.ddl-auto=create-drop"])
class ChildClassRepositoryTest {

    @Autowired
    lateinit var childClassRepository: ChildClassRepository

    @Autowired
    lateinit var relatedRepository: RelatedRepository

    @Test
    fun test() {
        // GIVEN
        val instanceA = childClassRepository.save(
            ChildClassEntity(
                inheritedProperty = "inheritedA",
                ownProperty = "ownA",
                relationships = mutableSetOf(RelationshipEntity(
                    related = relatedRepository.save(RelatedEntity(
                        name = "nameA"
                    ))
                ))
            )
        )
        val instanceB = childClassRepository.save(
            ChildClassEntity(
                inheritedProperty = "inheritedB",
                ownProperty = "ownB"
            )
        )

        // WHEN
        val specification = ChildClassSpecification(
            ownPropertyFilter = "ownA",
            inheritedPropertyFilter = "inheritedA",
            relatedNameFilter = setOf("nameA")
        )
        val result = childClassRepository.findAll(specification, PageRequest.ofSize(1))

        // THEN
        MatcherAssert.assertThat(result, Matchers.contains(instanceA))
    }
}