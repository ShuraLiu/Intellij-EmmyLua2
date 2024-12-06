package com.cppcxy.ide.lsp

import com.jetbrains.rd.generator.nova.PredefinedType
import org.eclipse.lsp4j.jsonrpc.validation.NonNull
import org.eclipse.lsp4j.util.ToStringBuilder

class EmmyServerStatusParams {
    @get:NonNull
    @NonNull
    var health: String? = null
    
    @get:NonNull
    @NonNull
    var loading: Boolean = false

    @get:NonNull
    @NonNull
    var message: String? = null

    constructor()

    constructor(@NonNull health: String?, @NonNull loading: Boolean, @NonNull message: String?) {
        this.health = health
        this.loading = loading
        this.message = message
    }

    override fun toString(): String {
        val b = ToStringBuilder(this)
        b.add("health", this.health)
        b.add("loading", this.loading)
        b.add("message", this.message)
        return b.toString()
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        } else if (obj == null) {
            return false
        } else if (this.javaClass != obj.javaClass) {
            return false
        } else {
            val other = obj as EmmyServerStatusParams
            if (this.health == null) {
                if (other.health != null) {
                    return false
                }
            } else if (this.health != other.health) {
                return false
            }
            else if (this.loading != other.loading)
            {
                return false
            }

            return this.message == other.message
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}