package com.example.hidrett_app.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class TokenVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {

        val masked = text.text.map { ch ->
            if (ch.isLetterOrDigit()) '•' else ch
        }.joinToString("")

        return TransformedText(
            AnnotatedString(masked),
            OffsetMapping.Identity
        )
    }
}