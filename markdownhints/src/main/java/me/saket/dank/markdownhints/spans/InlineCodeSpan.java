package me.saket.dank.markdownhints.spans;

import androidx.annotation.NonNull;

import io.noties.markwon.core.MarkwonTheme;
import io.noties.markwon.core.spans.CodeSpan;

public class InlineCodeSpan extends CodeSpan {

  public InlineCodeSpan(@NonNull MarkwonTheme theme) {
    super(theme);
  }
}
