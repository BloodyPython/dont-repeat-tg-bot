package core.domain

import java.time.Instant

interface IEntity<TId> {
    val id: TId
    var version: Int
    var whenCreated: Instant
    var whenModified: Instant
}