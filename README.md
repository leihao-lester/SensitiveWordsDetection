# SensitiveWordsDetection
这是一个使用JavaFx完成的爬取网页并进行敏感词检测与高亮显示的小程序
### 安装
克隆项目代码，然后设置JDK为18，使用idea打开，选择“Edit Configuration”,添加新的运行配置，选择maven，在Command Line里输入javafx:run，然后apply，就可以正常运行了
### 功能
* 输入网页，爬取html内容，提取文字信息，导入敏感词，高亮显示敏感词，批量爬取
### 用法
* 输入敏感词，并点击导入敏感词库
* 输入网址，点击“爬取输入栏网页”，点击“html源码”显示html源码，点击”爬取后内容“提取文字信息，点击“高亮显示敏感词”会高亮显示敏感词
* 点击“导入网址”会将网址栏网址导入网址库
* 点击”批量爬取“会爬取网址库中的所有网页，并提取文字信息，点击“高亮显示敏感词”则会高亮显示
* 日志栏中显示的是敏感词出现的次数
如果帮到你的话可以点个star，谢谢啦！
