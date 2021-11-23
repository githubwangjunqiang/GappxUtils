package com.gx.app.gappxutils

import android.app.Application
import android.content.Context
import java.io.*

/**
 * Created by Android-小强 on 2021/11/15.
 * mailbox:980766134@qq.com
 * description:
 */
class App : Application() {
    companion object {
        lateinit var mContext: Context

        /**
         * 是否测试版
         */
        var APP_DEBUG = true
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this

    }
}

fun main(args: Array<String>) {
    /**
     * 本次最新的路径
     */
    val newFilePath = "C:\\wangjunqiang\\as\\gappx项目资料\\offer包名\\新建文本文档.txt"

    /**
     * 上次完整版路径
     */
    val oldFilePath = "C:\\wangjunqiang\\as\\gappx项目资料\\offer包名\\1.4.0\\包名文件完整版.txt"

    /**
     * 本次生成的版本号是多少
     */
    var versionCode = "1.4.0"
    var endFileDoc = File("C:\\wangjunqiang\\as\\gappx项目资料\\offer包名\\", versionCode)
    if (!endFileDoc.exists()) {
        endFileDoc.mkdirs()
    }

    var endFile = File(endFileDoc, "包名文件完整版-未排重版本.txt")
//  生成标准文件
    var name = createListFile(newFilePath)

//    整合之前的文件 生成新文件 主要用来排重两个文件
    var zipFile: String = zip(oldFilePath, name, endFile)

//   格式化文件 进行初步排重 并生成最终文件
    var finalFile = File(endFileDoc, "包名文件完整版.txt")
    var nameFile = format(zipFile, finalFile.absolutePath)

//    生成清单文件 和代码文件
    var manifestName = "清单文件元素.txt"
    var listName = "代码集合元素.txt"
    createManifestFile(nameFile, manifestName, listName)
}

/**
 * 合并两个文件 并且进行排重
 */
fun zip(oldFilePath: String, nameFile: String, finalFile: File): String {


    return finalFile.absolutePath
}

private fun createManifestFile(nameFile: String, manifestName: String, listName: String) {
    println("开始")
    var file = File(nameFile)
    var fileNew = File(file.parent, manifestName)
    var fileNewList = File(file.parent, listName)

    val fileReader = FileReader(file)
    val bufferedReader = BufferedReader(fileReader)
    val fileWriter = FileWriter(fileNew)
    val bufferedWriter = BufferedWriter(fileWriter)

    val fileWriterList = FileWriter(fileNewList)
    val bufferedWriterList = BufferedWriter(fileWriterList)
    var line: String? = ""
//     <package android:name="" />
    while (bufferedReader.readLine()?.run {
            line = this
        } != null) {
        if (line?.toLongOrNull() == null && line?.contains("-") != true) {
            bufferedWriter.write("<package android:name=\"${line}\" />")
            bufferedWriterList.write("\"${line}\",")
            bufferedWriter.newLine()
            bufferedWriterList.newLine()
        }
    }
    bufferedReader.close()
    bufferedWriter.close()
    bufferedWriterList.close()
    println("完成")
}

//456
private fun createListFile(fileName: String): String {
    println("开始")
    var file = File(fileName)
    var fileNew = File(file.parent, "${file.name}临时缓存.txt")

    val fileReader = FileReader(file)
    val bufferedReader = BufferedReader(fileReader)
    val fileWriter = FileWriter(fileNew)
    val bufferedWriter = BufferedWriter(fileWriter)
    var line: String? = ""
//     "com.raizen.acelera",
    while (bufferedReader.readLine()?.run {
            line = this
        } != null) {
        if (line?.toLongOrNull() == null && line?.contains("-") != true) {
            bufferedWriter.write("$line")
            bufferedWriter.newLine()
        }
    }
    bufferedReader.close()
    bufferedWriter.close()
    println("完成")
    return fileNew.absolutePath
}

private fun format(fileName: String, endName: String): String {
    println("开始format")
    var file = File(fileName)
    var fileNew = File(endName)

    val fileReader = FileReader(file)
    val bufferedReader = BufferedReader(fileReader)
    val fileWriter = FileWriter(fileNew)
    val bufferedWriter = BufferedWriter(fileWriter)
    var line: String? = ""
//     "com.raizen.acelera",
    val hashSetOf = hashSetOf<String>()
    while (bufferedReader.readLine()?.run {
            line = this
        } != null) {
        val add = hashSetOf.add(line ?: "")
        if (add) {
            bufferedWriter.write("$line")
            bufferedWriter.newLine()
        }
    }
    bufferedReader.close()
    bufferedWriter.close()
    println("完成format")
    return fileNew.absolutePath
}