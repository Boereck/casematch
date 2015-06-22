/**
 * This package contains case matching functionality where the object/value to match on is
 * known on definition time of the cases. To start matching, use on of the static methods
 * defined in class {@link de.boereck.matcher.eager.EagerMatcher EagerMatcher}. The implementation
 * is optimized to create as few objects (and therefore garbage) as possible, while still
 * being immutable. Cases are evaluated directly when they are called, if a match is found
 * further cases will not evaluate their checks (e.g. instanceof checks, predicates, to optional functions).
 *
 * @author Max Bureck
 */
package de.boereck.matcher.eager;