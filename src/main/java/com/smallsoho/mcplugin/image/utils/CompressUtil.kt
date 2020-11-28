package com.smallsoho.mcplugin.image.utils

import java.io.File

class CompressUtil {

    companion object {
        private const val TAG = "Compress"
        fun compressImg(imgFile: File, outDir: File) {
            if (!ImageUtil.isImage(imgFile)) {
                return
            }
            val oldSize = imgFile.length()
            val newSize: Long
            if (ImageUtil.isJPG(imgFile)) {
                val tempFilePath: String = "${imgFile.path.substring(0, imgFile.path.lastIndexOf("."))}_temp" +
                        imgFile.path.substring(imgFile.path.lastIndexOf("."))
                Tools.cmd("guetzli", "${imgFile.path} $tempFilePath")
                val tempFile = File(tempFilePath)
                newSize = tempFile.length()
                LogUtil.log("newSize = $newSize")
                if (newSize < oldSize) {
                    tempFile.copyTo(File(outDir, imgFile.name))
                    tempFile.delete()
                } else {
                    if (tempFile.exists()) {
                        tempFile.delete()
                    }
                }

            } else {
                val outFile = File(outDir, imgFile.name)
                Tools.cmd("pngquant", "--skip-if-larger --speed 1 --nofs --strip --force --output ${outFile.path} -- ${imgFile.path}")
                newSize = outFile.length()
            }

            LogUtil.log(TAG, imgFile.path, oldSize.toString(), newSize.toString())
        }
    }

}