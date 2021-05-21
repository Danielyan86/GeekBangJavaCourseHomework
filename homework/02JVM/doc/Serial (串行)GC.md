Serial GC 对年轻代使用 mark-copy(标记-复制) 算法, 对老年代使用 mark-sweep-compact(标记-清除-整理)算法. 顾名思义, 两者都是单线程的垃圾收集器,不能进行并行处理。两者都会触发全线暂停(STW),停止所有的应用线程。

因此这种GC算法不能充分利用多核CPU。不管有多少CPU内核, JVM 在垃圾收集时都只能使用单个核心。

要启用此款收集器, 只需要指定一个JVM启动参数即可,同时对年轻代和老年代生效:
```java
java -XX:+UseSerialGC com.mypackages.MyExecutableClass
```
该选项只适合几百MB堆内存的JVM,而且是单核CPU时比较有用。 对于服务器端来说, 因为一般是多个CPU内核, 并不推荐使用, 除非确实需要限制JVM所使用的资源。大多数服务器端应用部署在多核平台上, 选择 Serial GC 就表示人为的限制系统资源的使用。 导致的就是资源闲置, 多的CPU资源也不能用来降低延迟,也不能用来增加吞吐量。
使用Serial GC的垃圾收集日志
 ```
java -XX:+PrintGCDetails  GCLogAnalysis
[0.029s][info   ][gc] Using G1
[0.033s][info   ][gc,init] Version: 16+36-2231 (release)
[0.033s][info   ][gc,init] CPUs: 4 total, 4 available
[0.033s][info   ][gc,init] Memory: 8192M
[0.033s][info   ][gc,init] Large Page Support: Disabled
[0.033s][info   ][gc,init] NUMA Support: Disabled
[0.033s][info   ][gc,init] Compressed Oops: Enabled (Zero based)
[0.033s][info   ][gc,init] Heap Region Size: 1M
[0.033s][info   ][gc,init] Heap Min Capacity: 8M
[0.033s][info   ][gc,init] Heap Initial Capacity: 128M
[0.033s][info   ][gc,init] Heap Max Capacity: 2G
[0.033s][info   ][gc,init] Pre-touch: Disabled
[0.033s][info   ][gc,init] Parallel Workers: 4
[0.033s][info   ][gc,init] Concurrent Workers: 1
[0.033s][info   ][gc,init] Concurrent Refinement Workers: 4
[0.033s][info   ][gc,init] Periodic GC: Disabled
[0.036s][info   ][gc,metaspace] CDS archive(s) mapped at: [0x0000000800000000-0x0000000800bb9000-0x0000000800bb9000), size 12292096, SharedBaseAddress: 0x0000000800000000, ArchiveRelocationMode: 0.
[0.036s][info   ][gc,metaspace] Compressed class space mapped at: 0x0000000800c00000-0x0000000840c00000, reserved size: 1073741824
[0.036s][info   ][gc,metaspace] Narrow klass base: 0x0000000800000000, Narrow klass shift: 3, Narrow klass range: 0x100000000
正在执行...
[0.216s][info   ][gc,start    ] GC(0) Pause Young (Normal) (G1 Evacuation Pause)
[0.216s][info   ][gc,task     ] GC(0) Using 3 workers of 4 for evacuation
[0.223s][info   ][gc,phases   ] GC(0)   Pre Evacuate Collection Set: 0.1ms
[0.223s][info   ][gc,phases   ] GC(0)   Merge Heap Roots: 0.2ms
[0.223s][info   ][gc,phases   ] GC(0)   Evacuate Collection Set: 5.1ms
[0.223s][info   ][gc,phases   ] GC(0)   Post Evacuate Collection Set: 0.5ms
[0.223s][info   ][gc,phases   ] GC(0)   Other: 0.7ms
[0.223s][info   ][gc,heap     ] GC(0) Eden regions: 13->0(12)
[0.223s][info   ][gc,heap     ] GC(0) Survivor regions: 0->2(2)
[0.223s][info   ][gc,heap     ] GC(0) Old regions: 0->4
[0.223s][info   ][gc,heap     ] GC(0) Archive regions: 2->2
[0.223s][info   ][gc,heap     ] GC(0) Humongous regions: 4->3
[0.223s][info   ][gc,metaspace] GC(0) Metaspace: 128K(320K)->128K(320K) NonClass: 122K(192K)->122K(192K) Class: 5K(128K)->5K(128K)
[0.223s][info   ][gc          ] GC(0) Pause Young (Normal) (G1 Evacuation Pause) 17M->9M(130M) 6.980ms
[0.223s][info   ][gc,cpu      ] GC(0) User=0.01s Sys=0.01s Real=0.01s
[0.235s][info   ][gc,start    ] GC(1) Pause Young (Normal) (G1 Evacuation Pause)
[0.235s][info   ][gc,task     ] GC(1) Using 3 workers of 4 for evacuation
[0.239s][info   ][gc,phases   ] GC(1)   Pre Evacuate Collection Set: 0.1ms
[0.240s][info   ][gc,phases   ] GC(1)   Merge Heap Roots: 0.0ms
[0.240s][info   ][gc,phases   ] GC(1)   Evacuate Collection Set: 4.0ms
[0.240s][info   ][gc,phases   ] GC(1)   Post Evacuate Collection Set: 0.4ms
[0.240s][info   ][gc,phases   ] GC(1)   Other: 0.2ms
[0.240s][info   ][gc,heap     ] GC(1) Eden regions: 12->0(23)
[0.240s][info   ][gc,heap     ] GC(1) Survivor regions: 2->2(2)
[0.240s][info   ][gc,heap     ] GC(1) Old regions: 4->7
[0.240s][info   ][gc,heap     ] GC(1) Archive regions: 2->2
[0.240s][info   ][gc,heap     ] GC(1) Humongous regions: 10->4
[0.240s][info   ][gc,metaspace] GC(1) Metaspace: 132K(320K)->132K(320K) NonClass: 127K(192K)->127K(192K) Class: 5K(128K)->5K(128K)
[0.240s][info   ][gc          ] GC(1) Pause Young (Normal) (G1 Evacuation Pause) 28M->13M(130M) 5.168ms
[0.240s][info   ][gc,cpu      ] GC(1) User=0.01s Sys=0.00s Real=0.00s
[0.264s][info   ][gc,start    ] GC(2) Pause Young (Normal) (G1 Evacuation Pause)
[0.264s][info   ][gc,task     ] GC(2) Using 3 workers of 4 for evacuation
[0.273s][info   ][gc,phases   ] GC(2)   Pre Evacuate Collection Set: 0.3ms
[0.273s][info   ][gc,phases   ] GC(2)   Merge Heap Roots: 0.3ms
[0.273s][info   ][gc,phases   ] GC(2)   Evacuate Collection Set: 6.2ms
[0.273s][info   ][gc,phases   ] GC(2)   Post Evacuate Collection Set: 1.2ms
[0.273s][info   ][gc,phases   ] GC(2)   Other: 0.2ms
[0.273s][info   ][gc,heap     ] GC(2) Eden regions: 23->0(27)
[0.273s][info   ][gc,heap     ] GC(2) Survivor regions: 2->4(4)
[0.273s][info   ][gc,heap     ] GC(2) Old regions: 7->15
[0.273s][info   ][gc,heap     ] GC(2) Archive regions: 2->2
[0.273s][info   ][gc,heap     ] GC(2) Humongous regions: 9->6
[0.273s][info   ][gc,metaspace] GC(2) Metaspace: 139K(384K)->139K(384K) NonClass: 133K(256K)->133K(256K) Class: 5K(128K)->5K(128K)
[0.273s][info   ][gc          ] GC(2) Pause Young (Normal) (G1 Evacuation Pause) 41M->25M(130M) 8.415ms
[0.273s][info   ][gc,cpu      ] GC(2) User=0.01s Sys=0.01s Real=0.01s
[0.294s][info   ][gc,start    ] GC(3) Pause Young (Normal) (G1 Evacuation Pause)
[0.295s][info   ][gc,task     ] GC(3) Using 3 workers of 4 for evacuation
[0.307s][info   ][gc,phases   ] GC(3)   Pre Evacuate Collection Set: 0.1ms
[0.307s][info   ][gc,phases   ] GC(3)   Merge Heap Roots: 0.1ms
[0.307s][info   ][gc,phases   ] GC(3)   Evacuate Collection Set: 11.5ms
[0.307s][info   ][gc,phases   ] GC(3)   Post Evacuate Collection Set: 0.2ms
[0.307s][info   ][gc,phases   ] GC(3)   Other: 0.3ms
[0.307s][info   ][gc,heap     ] GC(3) Eden regions: 27->0(28)
[0.307s][info   ][gc,heap     ] GC(3) Survivor regions: 4->4(4)
[0.307s][info   ][gc,heap     ] GC(3) Old regions: 15->29
[0.307s][info   ][gc,heap     ] GC(3) Archive regions: 2->2
[0.307s][info   ][gc,heap     ] GC(3) Humongous regions: 20->15
[0.307s][info   ][gc,metaspace] GC(3) Metaspace: 151K(384K)->151K(384K) NonClass: 145K(256K)->145K(256K) Class: 5K(128K)->5K(128K)
[0.307s][info   ][gc          ] GC(3) Pause Young (Normal) (G1 Evacuation Pause) 65M->48M(130M) 12.297ms
[0.307s][info   ][gc,cpu      ] GC(3) User=0.01s Sys=0.01s Real=0.01s
[0.331s][info   ][gc,start    ] GC(4) Pause Young (Concurrent Start) (G1 Humongous Allocation)
[0.331s][info   ][gc,task     ] GC(4) Using 3 workers of 4 for evacuation
[0.343s][info   ][gc,phases   ] GC(4)   Pre Evacuate Collection Set: 0.1ms
[0.343s][info   ][gc,phases   ] GC(4)   Merge Heap Roots: 0.1ms
[0.343s][info   ][gc,phases   ] GC(4)   Evacuate Collection Set: 8.6ms
[0.343s][info   ][gc,phases   ] GC(4)   Post Evacuate Collection Set: 3.0ms
[0.343s][info   ][gc,phases   ] GC(4)   Other: 0.4ms
[0.343s][info   ][gc,heap     ] GC(4) Eden regions: 27->0(42)
[0.343s][info   ][gc,heap     ] GC(4) Survivor regions: 4->4(4)
[0.343s][info   ][gc,heap     ] GC(4) Old regions: 29->38
[0.343s][info   ][gc,heap     ] GC(4) Archive regions: 2->2
[0.343s][info   ][gc,heap     ] GC(4) Humongous regions: 26->21
[0.343s][info   ][gc,metaspace] GC(4) Metaspace: 151K(384K)->151K(384K) NonClass: 145K(256K)->145K(256K) Class: 5K(128K)->5K(128K)
[0.343s][info   ][gc          ] GC(4) Pause Young (Concurrent Start) (G1 Humongous Allocation) 86M->63M(342M) 12.567ms
[0.343s][info   ][gc,cpu      ] GC(4) User=0.00s Sys=0.01s Real=0.01s
[0.343s][info   ][gc          ] GC(5) Concurrent Undo Cycle
[0.343s][info   ][gc,marking  ] GC(5) Concurrent Cleanup for Next Mark
[0.351s][info   ][gc,marking  ] GC(5) Concurrent Cleanup for Next Mark 7.458ms
[0.351s][info   ][gc          ] GC(5) `G�m� 7.641ms
[0.439s][info   ][gc,start    ] GC(6) Pause Young (Normal) (G1 Evacuation Pause)
[0.439s][info   ][gc,task     ] GC(6) Using 4 workers of 4 for evacuation
[0.447s][info   ][gc,phases   ] GC(6)   Pre Evacuate Collection Set: 0.1ms
[0.447s][info   ][gc,phases   ] GC(6)   Merge Heap Roots: 0.1ms
[0.447s][info   ][gc,phases   ] GC(6)   Evacuate Collection Set: 7.6ms
[0.447s][info   ][gc,phases   ] GC(6)   Post Evacuate Collection Set: 0.2ms
[0.447s][info   ][gc,phases   ] GC(6)   Other: 0.3ms
[0.447s][info   ][gc,heap     ] GC(6) Eden regions: 42->0(97)
[0.447s][info   ][gc,heap     ] GC(6) Survivor regions: 4->6(6)
[0.447s][info   ][gc,heap     ] GC(6) Old regions: 38->51
[0.447s][info   ][gc,heap     ] GC(6) Archive regions: 2->2
[0.447s][info   ][gc,heap     ] GC(6) Humongous regions: 44->29
[0.447s][info   ][gc,metaspace] GC(6) Metaspace: 151K(384K)->151K(384K) NonClass: 145K(256K)->145K(256K) Class: 5K(128K)->5K(128K)
[0.447s][info   ][gc          ] GC(6) Pause Young (Normal) (G1 Evacuation Pause) 128M->86M(342M) 8.390ms
[0.447s][info   ][gc,cpu      ] GC(6) User=0.01s Sys=0.02s Real=0.01s
[0.549s][info   ][gc,start    ] GC(7) Pause Young (Normal) (G1 Evacuation Pause)
[0.549s][info   ][gc,task     ] GC(7) Using 4 workers of 4 for evacuation
[0.570s][info   ][gc,phases   ] GC(7)   Pre Evacuate Collection Set: 0.1ms
[0.570s][info   ][gc,phases   ] GC(7)   Merge Heap Roots: 0.0ms
[0.570s][info   ][gc,phases   ] GC(7)   Evacuate Collection Set: 20.5ms
[0.570s][info   ][gc,phases   ] GC(7)   Post Evacuate Collection Set: 0.4ms
[0.570s][info   ][gc,phases   ] GC(7)   Other: 0.2ms
[0.570s][info   ][gc,heap     ] GC(7) Eden regions: 97->0(74)
[0.570s][info   ][gc,heap     ] GC(7) Survivor regions: 6->13(13)
[0.570s][info   ][gc,heap     ] GC(7) Old regions: 51->84
[0.570s][info   ][gc,heap     ] GC(7) Archive regions: 2->2
[0.570s][info   ][gc,heap     ] GC(7) Humongous regions: 82->44
[0.570s][info   ][gc,metaspace] GC(7) Metaspace: 152K(384K)->152K(384K) NonClass: 146K(256K)->146K(256K) Class: 5K(128K)->5K(128K)
[0.570s][info   ][gc          ] GC(7) Pause Young (Normal) (G1 Evacuation Pause) 236M->141M(342M) 21.508ms
[0.570s][info   ][gc,cpu      ] GC(7) User=0.02s Sys=0.04s Real=0.03s
[0.581s][info   ][gc,start    ] GC(8) Pause Young (Concurrent Start) (G1 Humongous Allocation)
[0.581s][info   ][gc,task     ] GC(8) Using 4 workers of 4 for evacuation
[0.586s][info   ][gc,phases   ] GC(8)   Pre Evacuate Collection Set: 0.1ms
[0.586s][info   ][gc,phases   ] GC(8)   Merge Heap Roots: 0.0ms
[0.586s][info   ][gc,phases   ] GC(8)   Evacuate Collection Set: 4.0ms
[0.586s][info   ][gc,phases   ] GC(8)   Post Evacuate Collection Set: 0.3ms
[0.586s][info   ][gc,phases   ] GC(8)   Other: 0.2ms
[0.586s][info   ][gc,heap     ] GC(8) Eden regions: 31->0(70)
[0.586s][info   ][gc,heap     ] GC(8) Survivor regions: 13->11(11)
[0.586s][info   ][gc,heap     ] GC(8) Old regions: 84->96
[0.586s][info   ][gc,heap     ] GC(8) Archive regions: 2->2
[0.586s][info   ][gc,heap     ] GC(8) Humongous regions: 68->55
[0.586s][info   ][gc,metaspace] GC(8) Metaspace: 152K(384K)->152K(384K) NonClass: 146K(256K)->146K(256K) Class: 5K(128K)->5K(128K)
[0.586s][info   ][gc          ] GC(8) Pause Young (Concurrent Start) (G1 Humongous Allocation) 195M->162M(342M) 5.011ms
[0.586s][info   ][gc,cpu      ] GC(8) User=0.01s Sys=0.00s Real=0.00s
[0.586s][info   ][gc          ] GC(9) Concurrent Undo Cycle
[0.586s][info   ][gc,marking  ] GC(9) Concurrent Cleanup for Next Mark
[0.588s][info   ][gc,marking  ] GC(9) Concurrent Cleanup for Next Mark 1.358ms
[0.588s][info   ][gc          ] GC(9) `G�m� 1.494ms
[0.589s][info   ][gc,start    ] GC(10) Pause Young (Concurrent Start) (G1 Humongous Allocation)
[0.589s][info   ][gc,task     ] GC(10) Using 4 workers of 4 for evacuation
[0.600s][info   ][gc,phases   ] GC(10)   Pre Evacuate Collection Set: 0.1ms
[0.600s][info   ][gc,phases   ] GC(10)   Merge Heap Roots: 0.0ms
[0.600s][info   ][gc,phases   ] GC(10)   Evacuate Collection Set: 2.4ms
[0.600s][info   ][gc,phases   ] GC(10)   Post Evacuate Collection Set: 8.8ms
[0.600s][info   ][gc,phases   ] GC(10)   Other: 0.2ms
[0.600s][info   ][gc,heap     ] GC(10) Eden regions: 7->0(382)
[0.600s][info   ][gc,heap     ] GC(10) Survivor regions: 11->4(11)
[0.600s][info   ][gc,heap     ] GC(10) Old regions: 96->107
[0.600s][info   ][gc,heap     ] GC(10) Archive regions: 2->2
[0.600s][info   ][gc,heap     ] GC(10) Humongous regions: 58->57
[0.600s][info   ][gc,metaspace] GC(10) Metaspace: 152K(384K)->152K(384K) NonClass: 146K(256K)->146K(256K) Class: 5K(128K)->5K(128K)
[0.600s][info   ][gc          ] GC(10) Pause Young (Concurrent Start) (G1 Humongous Allocation) 171M->167M(849M) 11.804ms
[0.600s][info   ][gc,cpu      ] GC(10) User=0.01s Sys=0.00s Real=0.02s
[0.600s][info   ][gc          ] GC(11) Concurrent Undo Cycle
[0.600s][info   ][gc,marking  ] GC(11) Concurrent Cleanup for Next Mark
[0.612s][info   ][gc,marking  ] GC(11) Concurrent Cleanup for Next Mark 11.863ms
[0.612s][info   ][gc          ] GC(11) `G�m� 11.971ms
执行结束!共生成对象次数:3829
[1.220s][info   ][gc,heap,exit] Heap
[1.220s][info   ][gc,heap,exit]  garbage-first heap   total 869376K, used 665291K [0x0000000780000000, 0x0000000800000000)
[1.220s][info   ][gc,heap,exit]   region size 1024K, 306 young (313344K), 4 survivors (4096K)
[1.220s][info   ][gc,heap,exit]  Metaspace       used 247K, committed 448K, reserved 1056768K
[1.220s][info   ][gc,heap,exit]   class space    used 8K, committed 128K, reserved 1048576K
```
```