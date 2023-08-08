package kpentchev.bug.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType

@Entity(name = "SuperClass")
@Inheritance(strategy = InheritanceType.JOINED)
abstract class SuperClassEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id: Long? = null,

    @Column(unique = true)
    var inheritedProperty: String,
)