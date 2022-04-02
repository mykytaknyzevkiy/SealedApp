import com.google.gson.*
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class Slack {

    fun sendUpdateNotification() {
        val webhooksUrl = URL("https://hooks.slack.com/services/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")

        val connection = (webhooksUrl.openConnection() as HttpURLConnection).apply {
            requestMethod   = "POST"
            connectTimeout  = 5000
            useCaches       = false
            doInput         = true
            doOutput        = true
        }

        val payload = "payload=${generateMessage()}"

        println(payload)

        val wr = DataOutputStream(connection.outputStream)
        wr.writeBytes(payload)
        wr.flush()
        wr.close()

        val `is` = connection.inputStream
        val rd = BufferedReader(InputStreamReader(`is`))
        var line: String? = null
        val  response = StringBuffer()

        line = rd.readLine()

        while (line != null) {
            response.append(line)
            response.append('\r')
            line = rd.readLine()
        }

        println(response.toString())
        rd.close()

        connection.disconnect()
    }

    private fun generateMessage(): String {
        val blocks = arrayListOf<SlackBlock>()

        blocks.add(
            SlackBlock(
                "section",
                text = SlackTxt(text = "Vitau shanoviy kolegie.\\n Ya opublikuvav onovlennya")
            )
        )

        blocks.add(
            SlackBlock(
                "section"
            ).apply {
                fields = arrayListOf()
                fields?.add(
                    SlackTxt(text = "*Release Notes:*\n ${Config.releaseNote}")
                )
                fields?.add(
                    SlackTxt(text = "*Version:*\n ${Config.versionName}-${Config.versionCode}")
                )
            }
        )

        val msg = SlackMsg(blocks = blocks)

        return Gson().toJson(msg)
    }

    private data class SlackTxt(
        val type : String = "mrkdwn",
        val text : String
    )

    private data class SlackBlock(
        val type   : String,
        val text   : SlackTxt? = null,
        var fields : ArrayList<SlackTxt>? = null
    )

    private data class SlackMsg(
        val channel: String = "#mhp",
        val username: String = "webhookbot",
        val text: String = "Nek",
        val blocks: List<SlackBlock>
    )
}