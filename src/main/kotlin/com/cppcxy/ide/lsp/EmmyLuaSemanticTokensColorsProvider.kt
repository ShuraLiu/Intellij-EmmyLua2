package com.cppcxy.ide.lsp

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiFile
import com.redhat.devtools.lsp4ij.features.semanticTokens.SemanticTokensColorsProvider
import com.redhat.devtools.lsp4ij.features.semanticTokens.SemanticTokensHighlightingColors
import com.tang.intellij.lua.highlighting.LuaHighlightingData

class EmmyLuaSemanticTokensColorsProvider : SemanticTokensColorsProvider {
    override fun getTextAttributesKey(
        tokenType: String,
        tokenModifiers: List<String>,
        file: PsiFile
    ): TextAttributesKey? {

        when (tokenType) {
            "namespace" -> {
                if (this.hasTokenModifiers(tokenModifiers, "declaration", "definition")) {
                    return SemanticTokensHighlightingColors.NAMESPACE_DECLARATION
                }

                return SemanticTokensHighlightingColors.NAMESPACE
            }

            "class" -> {
                if (this.hasTokenModifiers(tokenModifiers, "declaration", "definition")) {
                    return SemanticTokensHighlightingColors.CLASS_DECLARATION
                }

                return SemanticTokensHighlightingColors.CLASS
            }

            "enum" -> return SemanticTokensHighlightingColors.ENUM
            "interface" -> return SemanticTokensHighlightingColors.INTERFACE
            "struct" -> return SemanticTokensHighlightingColors.STRUCT
            "typeParameter" -> return SemanticTokensHighlightingColors.TYPE_PARAMETER
            "type" -> return SemanticTokensHighlightingColors.TYPE
            "parameter" -> if (this.hasTokenModifiers(tokenModifiers, "documentation")) {
                    return LuaHighlightingData.DOC_COMMENT_TAG_VALUE
                } else {
                    return LuaHighlightingData.PARAMETER
                }
            "variable" -> if (this.hasTokenModifiers(tokenModifiers, "static")) {
                if (this.hasTokenModifiers(tokenModifiers, "readonly")) {
                    return SemanticTokensHighlightingColors.STATIC_READONLY_VARIABLE
                }

                return SemanticTokensHighlightingColors.STATIC_VARIABLE
            } else {
                if (this.hasTokenModifiers(tokenModifiers, "readonly")) {
                    return SemanticTokensHighlightingColors.READONLY_VARIABLE
                }

                return SemanticTokensHighlightingColors.VARIABLE
            }

            "property" -> if (this.hasTokenModifiers(tokenModifiers, "static")) {
                if (this.hasTokenModifiers(tokenModifiers, "readonly")) {
                    return SemanticTokensHighlightingColors.STATIC_READONLY_PROPERTY
                }

                return SemanticTokensHighlightingColors.STATIC_PROPERTY
            }
            else if (this.hasTokenModifiers(tokenModifiers, "documentation")) {
                return LuaHighlightingData.DOC_COMMENT_TAG_VALUE
            }
            else {
                if (this.hasTokenModifiers(tokenModifiers, "readonly")) {
                    return SemanticTokensHighlightingColors.READONLY_PROPERTY
                }

                return SemanticTokensHighlightingColors.PROPERTY
            }

            "enumMember" -> return SemanticTokensHighlightingColors.ENUM_MEMBER
            "decorator" -> return SemanticTokensHighlightingColors.DECORATOR
            "event" -> return SemanticTokensHighlightingColors.EVENT
            "function" -> if (this.hasTokenModifiers(tokenModifiers, "defaultLibrary")) {
                return LuaHighlightingData.GLOBAL_FUNCTION
            } else {
                if (this.hasTokenModifiers(tokenModifiers, "declaration", "definition")) {
                    return SemanticTokensHighlightingColors.FUNCTION_DECLARATION
                }

                return SemanticTokensHighlightingColors.FUNCTION
            }

            "method" -> if (this.hasTokenModifiers(tokenModifiers, "declaration", "definition")) {
                return SemanticTokensHighlightingColors.METHOD_DECLARATION
            } else {
                if (this.hasTokenModifiers(tokenModifiers, "static")) {
                    return SemanticTokensHighlightingColors.STATIC_METHOD
                }

                return LuaHighlightingData.INSTANCE_METHOD
            }

            "macro" -> return SemanticTokensHighlightingColors.MACRO
            "label" -> return SemanticTokensHighlightingColors.LABEL
            "comment" -> return SemanticTokensHighlightingColors.COMMENT
            "string" -> return SemanticTokensHighlightingColors.STRING
            "keyword" -> return LuaHighlightingData.KEYWORD
            "number" -> return LuaHighlightingData.NUMBER
            "regexp" -> return SemanticTokensHighlightingColors.REGEXP
            "modifier" -> return SemanticTokensHighlightingColors.MODIFIER
            "operator" -> return LuaHighlightingData.OPERATORS
            "self" -> return LuaHighlightingData.SELF
            "_G" -> return LuaHighlightingData.GLOBAL_VAR
            "upvalue" -> return LuaHighlightingData.UP_VALUE
            else -> return null
        }
    }

    protected fun hasTokenModifiers(tokenModifiers: List<String>, vararg checkedTokenModifiers: String): Boolean {
        if (tokenModifiers.isEmpty()) {
            return false
        } else {
            val var4 = checkedTokenModifiers.size
            for (var5 in 0 until var4) {
                val modifier = checkedTokenModifiers[var5]
                if (tokenModifiers.contains(modifier)) {
                    return true
                }
            }

            return false
        }
    }
}