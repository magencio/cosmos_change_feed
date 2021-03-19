package com.microsoft.cosmos.cosmoschangefeed.testutils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestUtils {

  /**
   * Get the items from an interator.
   * @param items the iterator
   * @return the list of items
   */
  public static <T> List<T> toList(Iterator<T> items) {
    List<T> list = new ArrayList<>();
    items.forEachRemaining(list::add);
    return list;
  }

}
