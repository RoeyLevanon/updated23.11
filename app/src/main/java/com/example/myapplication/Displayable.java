package com.example.myapplication;

/**
 * ממשק המייצג פריט הניתן להצגה ברשימות (RecyclerView) בממשק המשתמש.
 */
public interface Displayable {
    String getDisplayTitle();
    String getDisplaySubtitle();
    String getDisplayTag();
}
