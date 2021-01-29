package me.saket.dank.markdownhints.spans;

import io.noties.markwon.core.MarkwonTheme;
import io.noties.markwon.core.spans.HeadingSpan;

public class HeadingSpanWithLevel extends HeadingSpan {

  private final int level;

  public HeadingSpanWithLevel(MarkwonTheme theme, int level) {
    super(theme, level);
    this.level = level;
  }

  public int level() {
    return level;
  }
}
