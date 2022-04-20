object Config {

    const val packageName = "com.sealed.app"

    const val minSdkVersion = "27"
    const val targetVersion = "31"
    const val versionCode = 8
    const val versionName = "1.0.0"

    private val releaseList = arrayListOf<String>().apply {}

    val releaseNote: String
        get() {
            val txt = StringBuilder()

            var i = 1
            releaseList.forEach {
                txt.append("$i) ")
                txt.append(it)
                txt.append("\n")
                i++
            }

            return txt.toString()
        }

}