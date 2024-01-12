-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
#    public static * bind(...);
#    public static * inflate(...);
#    public static * bind(android.view.View);
    public static * inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
}