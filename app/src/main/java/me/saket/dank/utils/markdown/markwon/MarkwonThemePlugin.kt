package me.saket.dank.utils.markdown.markwon

import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.core.MarkwonTheme
import me.saket.dank.markdownhints.MarkdownHintOptions
import javax.inject.Inject
import javax.inject.Named

class MarkwonThemePlugin @Inject constructor(@Named("dawn_markwon_theme") private val options: MarkdownHintOptions) : AbstractMarkwonPlugin() {
  override fun configureTheme(builder: MarkwonTheme.Builder) {
    builder
        .headingBreakHeight(0)
        .blockQuoteColor(options.blockQuoteIndentationRuleColor())
        .blockQuoteWidth(options.blockQuoteVerticalRuleStrokeWidth())
        .blockMargin(options.listBlockIndentationMargin())
        .codeBackgroundColor(options.inlineCodeBackgroundColor())
  }
}
