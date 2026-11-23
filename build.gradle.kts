plugins {
    // השאר את השורה הקיימת של com.android.application כפי שהיא
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.6.0" apply false
}