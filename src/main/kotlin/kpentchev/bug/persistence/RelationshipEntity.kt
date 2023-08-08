package kpentchev.bug.persistence

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

@Entity(name = "Relationship")
class RelationshipEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Fetch(FetchMode.JOIN)
    @OneToOne
    val related: RelatedEntity?

) {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", insertable = true, updatable = false)
    lateinit var child: ChildClassEntity

}