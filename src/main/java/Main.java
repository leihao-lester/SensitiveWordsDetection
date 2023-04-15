import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {
    public static String urlString;
    public static String urlHtml;
    public static String urlData;
    public static String logData;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        primaryStage.setResizable(false);
        primaryStage.setTitle("java爬取敏感词");

        Label AllUrlsLabel = new Label("网址库");
        AllUrlsLabel.setLayoutX(660);
        AllUrlsLabel.setLayoutY(30);
        AllUrlsLabel.setFont(Font.font(14));
        root.getChildren().add(AllUrlsLabel);

        TextArea urlArea = new TextArea();
        urlArea.setLayoutX(660);
        urlArea.setLayoutY(60);
        urlArea.setPrefWidth(500);
        urlArea.setPrefHeight(150);
        urlArea.setFocusTraversable(false);
        urlArea.setText(getTextFromFile("Urls.txt"));
        root.getChildren().add(urlArea);

        Label AllWordsLabel = new Label("敏感词库");
        AllWordsLabel.setLayoutX(660);
        AllWordsLabel.setLayoutY(220);
        AllWordsLabel.setFont(Font.font(14));
        root.getChildren().add(AllWordsLabel);

        TextArea wordArea = new TextArea();
        wordArea.setLayoutX(660);
        wordArea.setLayoutY(250);
        wordArea.setPrefWidth(500);
        wordArea.setPrefHeight(150);
        wordArea.setFocusTraversable(false);
        wordArea.setText(getTextFromFile("Words.txt"));
        root.getChildren().add(wordArea);

        Label logLabel = new Label("日志");
        logLabel.setLayoutX(660);
        logLabel.setLayoutY(410);
        logLabel.setFont(Font.font(14));
        root.getChildren().add(logLabel);

        TextArea logArea = new TextArea();
        logArea.setLayoutX(660);
        logArea.setLayoutY(440);
        logArea.setPrefWidth(500);
        logArea.setPrefHeight(295);
        logArea.setFocusTraversable(false);
        root.getChildren().add(logArea);

        Label urlLabel = new Label("请输入网址: ");
        urlLabel.setLayoutX(30);
        urlLabel.setLayoutY(30);
        urlLabel.setFont(Font.font(14));
        root.getChildren().add(urlLabel);

        TextField urlLine = new TextField();
        urlLine.setLayoutX(130);
        urlLine.setLayoutY(30);
        urlLine.setPrefWidth(370);
        urlLine.setFocusTraversable(false);
        root.getChildren().add(urlLine);

        Button urlButton = new Button("导入网址");
        urlButton.setLayoutX(520);
        urlButton.setLayoutY(30);
        urlButton.setPrefWidth(100);
        urlButton.setFocusTraversable(false);
        urlButton.setOnAction(actionEvent -> {
            urlString = urlLine.getText();
            String pattern = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
            boolean isMatch = Pattern.matches(pattern, urlString);
            if (!isMatch) {
                Alert error = new Alert(Alert.AlertType.ERROR, "请输入合理的网址");
                Button err = new Button();
                err.setOnAction((ActionEvent e) -> {
                    error.showAndWait();
                });
                error.showAndWait();
            } else {
                try {
                    URL url = new URL(urlString);
                    URLConnection urlConnection = url.openConnection();
                    if (!(urlConnection instanceof HttpURLConnection)) {
                        Alert error = new Alert(Alert.AlertType.ERROR, "请输入合理的网址");
                        Button err = new Button();
                        err.setOnAction((ActionEvent e) -> {
                            error.showAndWait();
                        });
                        error.showAndWait();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                TextAddFile("Urls.txt", (urlLine.getText()) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String data = getTextFromFile("Urls.txt");
            urlArea.setText(data);
        });
        root.getChildren().add(urlButton);

        Label wordLabel = new Label("请输入敏感词: ");
        wordLabel.setLayoutX(30);
        wordLabel.setLayoutY(70);
        wordLabel.setFont(Font.font(14));
        wordLabel.setFocusTraversable(false);
        root.getChildren().add(wordLabel);

        TextField wordLine = new TextField();
        wordLine.setLayoutX(130);
        wordLine.setLayoutY(70);
        wordLine.setPrefWidth(370);
        wordLine.setFocusTraversable(false);
        root.getChildren().add(wordLine);

        Button wordButton = new Button("导入敏感词");
        wordButton.setLayoutX(520);
        wordButton.setLayoutY(70);
        wordButton.setPrefWidth(100);
        wordButton.setFocusTraversable(false);
        wordButton.setOnAction(actionEvent -> {
            try {
                TextAddFile("Words.txt", (wordLine.getText()) + " ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String data = getTextFromFile("Words.txt");
            wordArea.setText(data);
        });
        root.getChildren().add(wordButton);

        WebView web = new WebView();
        WebEngine webEngine = web.getEngine();
        web.setLayoutX(5);//参考系为anchorPane
        web.setLayoutY(5);
        web.setPrefWidth(590);
        web.setPrefHeight(580);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setLayoutX(25);
        anchorPane.setLayoutY(145);
        anchorPane.setStyle("-fx-border-color: grey;");//设置边框样式
        anchorPane.getChildren().add(web);
        web.setFocusTraversable(false);
        root.getChildren().add(anchorPane);

        Button htmlButton = new Button("HTML源码");
        htmlButton.setLayoutX(30);
        htmlButton.setLayoutY(110);
        htmlButton.setPrefWidth(110);
        htmlButton.setFocusTraversable(false);
        htmlButton.setOnAction(actionEvent -> {
            String html = "<xmp>" + urlHtml + "</xmp>";//将html代码以文本的形式显示
            webEngine.loadContent(html);
        });
        root.getChildren().add(htmlButton);

        Button dataButton = new Button("提取后内容");
        dataButton.setLayoutX(150);
        dataButton.setLayoutY(110);
        dataButton.setPrefWidth(110);
        dataButton.setFocusTraversable(false);
        dataButton.setOnAction(actionEvent -> {
            webEngine.loadContent(urlData);
        });
        root.getChildren().add(dataButton);

        Button highlightButton = new Button("高亮显示敏感词");
        highlightButton.setLayoutX(270);
        highlightButton.setLayoutY(110);
        highlightButton.setPrefWidth(110);
        highlightButton.setFocusTraversable(false);
        highlightButton.setOnAction(actionEvent -> {
            String logTemp = urlString + "\n";
            String[] temp;
            String wordStr = getTextFromFile("Words.txt");
            String highLightDataStr = urlData;
            wordStr = wordStr.substring(0, wordStr.length() - 1);//去掉最后一个空格符
            temp = wordStr.split(" ");
            temp[temp.length - 1] = temp[temp.length - 1].replace("\n", "");//去除最后一个换行符
            for (var item : temp) {
//                System.out.println("\"" + item + "\"");
                int num = 0;
                Pattern p = Pattern.compile(item, Pattern.CASE_INSENSITIVE);
                Matcher mc = p.matcher(highLightDataStr);
                StringBuffer sb = new StringBuffer();
                while (mc.find()) {
                    mc.appendReplacement(sb, "<mark>" + mc.group() + "</mark>");
                    num++;
                }
                mc.appendTail(sb);
                highLightDataStr = sb.toString();
                logTemp = logTemp + "\"" + item + "\" 出现了 " + num + " 次\n";
            }
            webEngine.loadContent(highLightDataStr);
            try {
                TextAddFile("Logs.txt", (logTemp + "\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            logArea.setText(getTextFromFile("Logs.txt"));
        });
        root.getChildren().add(highlightButton);

        Button actionButton = new Button("爬取输入栏网页");
        actionButton.setLayoutX(390);
        actionButton.setLayoutY(110);
        actionButton.setPrefWidth(110);
        actionButton.setFocusTraversable(false);
        actionButton.setOnAction(actionEvent -> {
            urlString = urlLine.getText();
            String pattern = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
            boolean isMatch = Pattern.matches(pattern, urlString);
            if (!isMatch) {
                Alert error = new Alert(Alert.AlertType.ERROR, "请输入合理的网址");
                Button err = new Button();
                err.setOnAction((ActionEvent e) -> {
                    error.showAndWait();
                });
                error.showAndWait();
            } else {
                try {
                    URL url = new URL(urlString);
                    URLConnection urlConnection = url.openConnection();
                    HttpURLConnection connection = null;
                    if (urlConnection instanceof HttpURLConnection) {
                        connection = (HttpURLConnection) urlConnection;
                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR, "请输入合理的网址");
                        Button err = new Button();
                        err.setOnAction((ActionEvent e) -> {
                            error.showAndWait();
                        });
                        error.showAndWait();
                    }
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    urlHtml = "";
                    String current;
                    while ((current = in.readLine()) != null) {
                        urlHtml += current;
                        urlHtml += "\n";
//                        System.out.println(current);
                    }
                    urlData = urlHtml.replaceAll("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", "");//过滤script标签
                    urlData = urlData.replaceAll("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", "");//过滤style标签
                    urlData = urlData.replaceAll("</?[^>]+>", "");//过滤html标签
                    urlData = urlData.replaceAll("[ ]+", "");
                    urlData = urlData.replaceAll("\n+", "\n");
                    urlData = urlData.replaceAll("\r\n", "");//html标签后面是\r\n,而不是\n\n,\r为回车,\n为换行
                    if ("\n".equals(urlData.charAt(0) + "")) {
                        urlData = urlData.replaceFirst("\n", "");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        root.getChildren().add(actionButton);

        Button actionMoreButton = new Button("批量爬取");
        actionMoreButton.setLayoutX(510);
        actionMoreButton.setLayoutY(110);
        actionMoreButton.setPrefWidth(110);
        actionMoreButton.setFocusTraversable(false);
        actionMoreButton.setOnAction(actionEvent -> {
            String urlTemp = getTextFromFile("Urls.txt");
            urlTemp = urlTemp.substring(0, urlTemp.length() - 1);//去除最后的换行符
            String[] urlList;
            urlList = urlTemp.split("\n");
            String allHighLightDataStr = "";
            urlHtml="";
            for (var urlItem : urlList) {
                String urlData1="";
                String urlHtml1="";
                try {
                    URL url = new URL(urlItem);
                    URLConnection urlConnection = url.openConnection();
                    HttpURLConnection connection = null;
                    if (urlConnection instanceof HttpURLConnection) {
                        connection = (HttpURLConnection) urlConnection;
                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR, "请输入合理的网址");
                        Button err = new Button();
                        err.setOnAction((ActionEvent e) -> {
                            error.showAndWait();
                        });
                        error.showAndWait();
                    }
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String current;
                    while ((current = in.readLine()) != null) {
                        urlHtml1 += current;
                        urlHtml1 += "\n";
//                        System.out.println(current);
                    }
                    urlData1 = urlHtml1.replaceAll("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", "");//过滤script标签
                    urlData1 = urlData1.replaceAll("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", "");//过滤style标签
                    urlData1 = urlData1.replaceAll("</?[^>]+>", "");//过滤html标签
                    urlData1 = urlData1.replaceAll("[ ]+", "");
                    urlData1 = urlData1.replaceAll("\n+", "\n");
                    urlData1 = urlData1.replaceAll("\r\n", "");//html标签后面是\r\n,而不是\n\n,\r为回车,\n为换行
                    if ("\n".equals(urlData1.charAt(0) + "")) {
                        urlData1 = urlData1.replaceFirst("\n", "");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String logTemp = urlItem + "\n";
                String[] temp;
                String wordStr = getTextFromFile("Words.txt");
                String highLightDataStr = urlData1;
                wordStr = wordStr.substring(0, wordStr.length() - 1);//去掉最后一个空格符
                temp = wordStr.split(" ");
                temp[temp.length - 1] = temp[temp.length - 1].replace("\n", "");//去除最后一个换行符
                for (var item : temp) {
//                    System.out.println("\"" + item + "\"");
                    int num = 0;
                    Pattern p = Pattern.compile(item, Pattern.CASE_INSENSITIVE);
                    Matcher mc = p.matcher(highLightDataStr);
                    StringBuffer sb = new StringBuffer();
                    while (mc.find()) {
                        mc.appendReplacement(sb, "<mark>" + mc.group() + "</mark>");
                        num++;
                    }
                    mc.appendTail(sb);
                    highLightDataStr = sb.toString();
                    logTemp = logTemp + "\"" + item + "\" 出现了 " + num + " 次\n";
                }
                allHighLightDataStr=allHighLightDataStr+"\n"+highLightDataStr;
                try {
                    TextAddFile("Logs.txt", (logTemp + "\n"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                logArea.setText(getTextFromFile("Logs.txt"));
                urlHtml=urlHtml+"\n"+urlHtml1;
                urlData=urlData+"\n"+urlData1;
            }
            webEngine.loadContent(allHighLightDataStr);
        });
        root.getChildren().add(actionMoreButton);

        primaryStage.show();
    }

    public void TextToFile(final String strFilename, final String strBuffer) {
        try {
            // 创建文件对象
            File fileText = new File(strFilename);
            // 向文件写入对象写入信息
            FileWriter fileWriter = new FileWriter(fileText);
            // 写文件
            fileWriter.write(strBuffer);
            // 关闭
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void TextAddFile(final String strFilename, final String strBuffer) throws IOException {
        FileOutputStream fos = new FileOutputStream(strFilename, true);
        fos.write((strBuffer).getBytes());
        fos.close();
    }

    public String getTextFromFile(final String strFilename) {
        //读取文件
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(strFilename))); //这里可以控制编码
//            br = new BufferedReader(new InputStreamReader(new FileInputStream(strFilename),"GBK")); //这里可以控制编码
            sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String data = new String(sb); //StringBuffer ==> String
        return data;
    }

    public static String IgnoreCaseReplace(String source, String patternString) {
        Pattern p = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher mc = p.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (mc.find()) {
            mc.appendReplacement(sb, "<mark>" + mc.group() + "</mark>");
        }
        mc.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
