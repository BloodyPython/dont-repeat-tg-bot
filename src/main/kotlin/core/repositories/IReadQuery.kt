package core.repositories

import org.ktorm.expression.BinaryExpression

interface IReadQuery {
    val query: BinaryExpression<Boolean>
}