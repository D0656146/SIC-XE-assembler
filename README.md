# SIC-XE-assembler

A SIC/XE assembler for assignment.

Please report bugs if you found some^^

## 書面報告

### 作者

姓名：劉景翔

學號：D0656146

座號：50

系級：資訊四丁

班級：二丙那班

### 程式開發過程

100%自行開發

### 開發平台

程式語言：Java

IDE：Apache NetBeans 9.0

### 使用方式

目前只有命令列模式

切換到主類別目錄下執行指令

    $ java Assembler [asmfilename] -xe

可選參數-xe開啟XE模式

目的檔輸出在與組合語言檔同目錄下

名稱相同除了副檔名為.obj

測試檔案test.xe與輸出檔test.obj如附件

### 輸入格式

單字間以TAB分隔否則顯示語法錯誤

沒有SYBTAB的話直接空TAB表示

### 程式功能

尚未支援所有可機器獨立的指令

包括EQU、ORG、USE、CSECT

也不支援Literal、Expression

其餘SIC/XE全指令及所有定址模式全部支援

指令列表詳見resources/instructions

可產生幾乎全部（除了機器獨立指令外）的錯誤訊息

請參考Ilearn上的組譯器錯誤訊息表

### 程式特點

+ 可擴充的指令列表

+ 可擴充的暫存器列表

+ 完整的專案架構

+ 可讀性佳（至少在學生程度中？）

+ 分辨30餘種錯誤訊息、除錯方便

### 辛酸血淚史

一言難盡就不贅述了XD

是說本來的目標有想過寫出可以讓老師下學期方便使用的組譯器

取代現在Ilearn上的版本

但只瞄了一眼他的專案架構就知道我還差太遠了QQ

還有很長一段路要學習