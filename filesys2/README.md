# filesys2文件系统服务端
##filesys2系统支持文件和图片断点续传,不支持秒传(相同md5值的文件只会存在一份,第二份直接引用[秒传]，优势：文件暂用资源少；劣势：大文件时md5计算缓慢。),文件单个下载和打包下载
###支持秒传请使用filesys1系统