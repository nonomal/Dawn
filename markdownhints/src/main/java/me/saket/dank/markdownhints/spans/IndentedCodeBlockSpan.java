package me.saket.dank.markdownhints.spans;

import androidx.annotation.NonNull;

import io.noties.markwon.core.MarkwonTheme;
import io.noties.markwon.core.spans.CodeSpan;

public class IndentedCodeBlockSpan extends CodeSpan {

  public IndentedCodeBlockSpan(@NonNull MarkwonTheme theme) {
    super(theme);
  }
}
