package kpentchev.bug.persistence

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

@Entity(name = "ChildClass")
class ChildClassEntity(
    id: Long? = null,
    inheritedProperty: String,
    var ownProperty: String,

    @Fetch(FetchMode.JOIN)
    @OneToMany(
        targetEntity = RelationshipEntity::class,
        cascade = [CascadeType.ALL],
        mappedBy = "child",
        orphanRemoval = true
    )
    var relationships: MutableSet<RelationshipEntity> = mutableSetOf(),

) : SuperClassEntity(id, inheritedProperty) {

    @PrePersist
    @PreUpdate
    fun preparePersist() {
        this.relationships.forEach { it.child = this }
    }

}