#目的
为了更好的展示论文中相关算法的具体执行效果，开发了一套可交互的软件对论文中重点章节的研究内容进行展示。通过展示，可以对论文中设计的协同观测系统有更清晰的了解，同时也能看到算法执行中的一些细节。

#运行
运行java -jar DCOS.jar即可启动程序，web和bootstrap目录必须与DCOS.jar处于同一目录下。

#操作
1. 在实例运行的命令框中输入init -1，可以随机产生四个子区域的传感器分布图；
2. 输入agent命令可以选举出每个区域的区域代理节点，此时可以在节点列表中查看每个传感器节点的状态信息；
3. 输入tenure 3000，可以指定区域代理节点的任期为3秒，到期后每个区域会自动重选区域代理节点；
4. 输入start命令会开启所有的传感器节点；
5. 输入sleep命令会调用半睡眠机制，让冗余节点处于半睡眠睡眠状态；
6. 输入event 50 50可以在坐标为(50,50)的点产生一个事件，此时可以查看监测数据，该事件的相应监测数据曲线会在这里实时生成；
7. 输入event clear可以结束该事件；
8. 输入clear，清除本次的实验设定，操作流程结束（具体的操作说明可以在说明中看到）。

#运行
首页
![ScreenShot](https://raw.github.com/buaastorm/DCOS/master/example/1.png)
实例运行
![ScreenShot](https://raw.github.com/buaastorm/DCOS/master/example/2.png)
监测数据
![ScreenShot](https://raw.github.com/buaastorm/DCOS/master/example/3.png)
节点列表
![ScreenShot](https://raw.github.com/buaastorm/DCOS/master/example/4.png)
说明
![ScreenShot](https://raw.github.com/buaastorm/DCOS/master/example/5.png)
