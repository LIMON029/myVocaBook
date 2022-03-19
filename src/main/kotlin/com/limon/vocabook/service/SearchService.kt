package com.limon.vocabook.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.limon.vocabook.API.CLIENT_ID
import com.limon.vocabook.API.CLIENT_SECRET
import com.limon.vocabook.API.API_URL
import org.springframework.stereotype.Service
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

@Service
class SearchService {
    fun searchWord(en:String):String {
        val word:String
        try {
            word = URLEncoder.encode(en, ENCODING)
        } catch (e:Exception){
            throw RuntimeException("인코딩 실패", e)
        }

        val reqHeader = hashMapOf<String, String>()
        reqHeader["X-Naver-Client-Id"] = CLIENT_ID
        reqHeader["X-Naver-Client-Secret"] = CLIENT_SECRET

        return post(reqHeader, word)
    }

    private fun post(reqHeader: HashMap<String, String>, en:String): String {
        val con = connect()
        val param = "source=en&target=ko&text=$en"
        try {
            con.requestMethod = "POST"
            for(header in reqHeader.entries) {
                con.setRequestProperty(header.key, header.value)
            }
            con.doOutput = true
            val wr = DataOutputStream(con.outputStream)
            wr.write(param.toByteArray())
            wr.flush()

            val resCode = con.responseCode
            return if(resCode == HttpURLConnection.HTTP_OK) {
                readBody(con.inputStream)
            } else {
                readBody(con.errorStream)
            }
        } catch (e:IOException) {
            throw RuntimeException("API 요청과 응답 실패", e)
        } finally {
            con.disconnect()
        }
    }

    private fun readBody(body: InputStream): String {
        val reader = InputStreamReader(body, "UTF-8")
        val lineReader = BufferedReader(reader)
        val om = ObjectMapper()
        try {
            val resBody = StringBuilder()
            var line = lineReader.readLine()
            while(line != null){
                resBody.append(line)
                line = lineReader.readLine()
            }
            val msg = om.readValue(resBody.toString(), Map::class.java)["message"].toString()
            val startIndex = msg.indexOf('=', msg.indexOf("translatedText"))
            val lastIndex = msg.indexOf(',', startIndex)
            return msg.substring(startIndex+1, lastIndex)
        } catch (e:IOException) {
            throw RuntimeException("API 응답을 읽는데 실패했습니다.", e)
        }
    }

    private fun connect(): HttpURLConnection {
        try {
            return URL(API_URL).openConnection() as HttpURLConnection
        } catch (e: MalformedURLException) {
            throw RuntimeException("API URL이 잘못되었습니다. : $API_URL", e)
        } catch (e: IOException) {
            throw RuntimeException("연결 실패... : $API_URL", e)
        }
    }

    companion object {
        private const val ENCODING = "UTF-8"
    }
}