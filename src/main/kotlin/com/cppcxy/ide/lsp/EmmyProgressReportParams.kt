package com.cppcxy.ide.lsp

import org.eclipse.lsp4j.jsonrpc.validation.NonNull
import org.eclipse.lsp4j.util.ToStringBuilder

class EmmyProgressReportParams {
    @get:NonNull
    @NonNull
    var text: String? = null

    @get:NonNull
    @NonNull
    var percent: Double = 0.0

    constructor()

    constructor(@NonNull text: String?, @NonNull percent: Double) {
        this.text = text
        this.percent = percent
    }

    override fun toString(): String {
        val b = ToStringBuilder(this)
        b.add("text", this.text)
        b.add("percent", this.percent)
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
            val other = obj as EmmyProgressReportParams
            if (this.text == null) {
                if (other.text != null) {
                    return false
                }
            } else if (this.text != other.text) {
                return false
            }

            return this.percent == other.percent
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}