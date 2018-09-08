/**
 *@author Nikolaus Knop
 */

package nikok.kprofile.api

abstract class Tag(internal val name: String) {
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tag

        if (name != other.name) return false

        return true
    }

    final override fun hashCode(): Int {
        return name.hashCode()
    }

    final override fun toString(): String {
        return "Tag(name='$name')"
    }

    companion object {
        internal fun create(name: String) = object : Tag(name) {}
    }
}