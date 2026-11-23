package com.example.myapplication;

/**
 * ממשק המייצג ישות במערכת הניתנת לשמירה ואחזור מול מסד הנתונים Firebase.
 */
public interface FirebaseEntity {
    String getEntityId();
    String getSummaryInfo();
}
