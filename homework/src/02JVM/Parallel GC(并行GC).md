并行垃圾收集器这一类组合, 在年轻代使用 标记-复制(mark-copy)算法, 在老年代使用 标记-清除-整理(mark-sweep-compact)算法。年轻代和老年代的垃圾回收都会触发STW事件,暂停所有的应用线程来执行垃圾收集。两者在执行 标记和 复制/整理阶段时都使用多个线程, 因此得名“(Parallel)”。通过并行执行, 使得GC时间大幅减少。

通过命令行参数 -XX:ParallelGCThreads=NNN 来指定 GC 线程数。 其默认值为CPU内核数
并行垃圾收集器适用于多核服务器,主要目标是增加吞吐量。因为对系统资源的有效使用,能达到更高的吞吐量:

- 在GC期间, 所有 CPU 内核都在并行清理垃圾, 所以暂停时间更短
- 在两次GC周期的间隔期, 没有GC线程在运行,不会消耗任何系统资源

另一方面, 因为此GC的所有阶段都不能中断, 所以并行GC很容易出现长时间的卡顿. 如果延迟是系统的主要目标, 那么就应该选择其他垃圾收集器组合。

```
java -XX:+PrintGCDetails -XX:ParallelGCThreads=6 GCLogAnalysis 
[0.004s][warning][gc] -XX:+PrintGCDetails is deprecated. Will use -Xlog:gc* instead.
[0.021s][info   ][gc] Using G1
[0.023s][info   ][gc,init] Version: 16+36-2231 (release)
[0.023s][info   ][gc,init] CPUs: 4 total, 4 available
[0.023s][info   ][gc,init] Memory: 8192M
[0.023s][info   ][gc,init] Large Page Support: Disabled
[0.023s][info   ][gc,init] NUMA Support: Disabled
[0.023s][info   ][gc,init] Compressed Oops: Enabled (Zero based)
[0.023s][info   ][gc,init] Heap Region Size: 1M
[0.023s][info   ][gc,init] Heap Min Capacity: 8M
[0.023s][info   ][gc,init] Heap Initial Capacity: 128M
[0.023s][info   ][gc,init] Heap Max Capacity: 2G
[0.023s][info   ][gc,init] Pre-touch: Disabled
[0.024s][info   ][gc,init] Parallel Workers: 6
[0.024s][info   ][gc,init] Concurrent Workers: 2
[0.024s][info   ][gc,init] Concurrent Refinement Workers: 6
[0.024s][info   ][gc,init] Periodic GC: Disabled
[0.027s][info   ][gc,metaspace] CDS archive(s) mapped at: [0x0000000800000000-0x0000000800bb9000-0x0000000800bb9000), size 12292096, SharedBaseAddress: 0x0000000800000000, ArchiveRelocationMode: 0.
[0.027s][info   ][gc,metaspace] Compressed class space mapped at: 0x0000000800c00000-0x0000000840c00000, reserved size: 1073741824
[0.027s][info   ][gc,metaspace] Narrow klass base: 0x0000000800000000, Narrow klass shift: 3, Narrow klass range: 0x100000000
正在执行...
[0.189s][info   ][gc,start    ] GC(0) Pause Young (Normal) (G1 Evacuation Pause)
[0.189s][info   ][gc,task     ] GC(0) Using 6 workers of 6 for evacuation
[0.194s][info   ][gc,phases   ] GC(0)   Pre Evacuate Collection Set: 0.1ms
[0.194s][info   ][gc,phases   ] GC(0)   Merge Heap Roots: 0.1ms
[0.194s][info   ][gc,phases   ] GC(0)   Evacuate Collection Set: 3.9ms
[0.194s][info   ][gc,phases   ] GC(0)   Post Evacuate Collection Set: 0.3ms
[0.194s][info   ][gc,phases   ] GC(0)   Other: 0.7ms
[0.194s][info   ][gc,heap     ] GC(0) Eden regions: 21->0(20)
[0.194s][info   ][gc,heap     ] GC(0) Survivor regions: 0->3(3)
[0.194s][info   ][gc,heap     ] GC(0) Old regions: 0->5
[0.194s][info   ][gc,heap     ] GC(0) Archive regions: 2->2
[0.194s][info   ][gc,heap     ] GC(0) Humongous regions: 12->6
[0.194s][info   ][gc,metaspace] GC(0) Metaspace: 134K(320K)->134K(320K) NonClass: 128K(192K)->128K(192K) Class: 5K(128K)->5K(128K)
[0.194s][info   ][gc          ] GC(0) Pause Young (Normal) (G1 Evacuation Pause) 33M->14M(130M) 5.256ms
[0.194s][info   ][gc,cpu      ] GC(0) User=0.01s Sys=0.01s Real=0.00s
[0.227s][info   ][gc,start    ] GC(1) Pause Young (Normal) (G1 Evacuation Pause)
[0.227s][info   ][gc,task     ] GC(1) Using 6 workers of 6 for evacuation
[0.237s][info   ][gc,phases   ] GC(1)   Pre Evacuate Collection Set: 0.3ms
[0.237s][info   ][gc,phases   ] GC(1)   Merge Heap Roots: 0.2ms
[0.237s][info   ][gc,phases   ] GC(1)   Evacuate Collection Set: 8.3ms
[0.237s][info   ][gc,phases   ] GC(1)   Post Evacuate Collection Set: 0.7ms
[0.237s][info   ][gc,phases   ] GC(1)   Other: 0.5ms
[0.237s][info   ][gc,heap     ] GC(1) Eden regions: 20->0(38)
[0.237s][info   ][gc,heap     ] GC(1) Survivor regions: 3->3(3)
[0.237s][info   ][gc,heap     ] GC(1) Old regions: 5->14
[0.237s][info   ][gc,heap     ] GC(1) Archive regions: 2->2
[0.237s][info   ][gc,heap     ] GC(1) Humongous regions: 23->13
[0.237s][info   ][gc,metaspace] GC(1) Metaspace: 139K(384K)->139K(384K) NonClass: 133K(256K)->133K(256K) Class: 5K(128K)->5K(128K)
[0.237s][info   ][gc          ] GC(1) Pause Young (Normal) (G1 Evacuation Pause) 51M->30M(130M) 10.371ms
[0.237s][info   ][gc,cpu      ] GC(1) User=0.01s Sys=0.01s Real=0.01s
[0.302s][info   ][gc,start    ] GC(2) Pause Young (Normal) (G1 Evacuation Pause)
[0.302s][info   ][gc,task     ] GC(2) Using 6 workers of 6 for evacuation
[0.325s][info   ][gc,phases   ] GC(2)   Pre Evacuate Collection Set: 0.1ms
[0.325s][info   ][gc,phases   ] GC(2)   Merge Heap Roots: 0.0ms
[0.325s][info   ][gc,phases   ] GC(2)   Evacuate Collection Set: 21.7ms
[0.325s][info   ][gc,phases   ] GC(2)   Post Evacuate Collection Set: 0.3ms
[0.325s][info   ][gc,phases   ] GC(2)   Other: 0.2ms
[0.325s][info   ][gc,heap     ] GC(2) Eden regions: 38->0(25)
[0.325s][info   ][gc,heap     ] GC(2) Survivor regions: 3->6(6)
[0.325s][info   ][gc,heap     ] GC(2) Old regions: 14->27
[0.325s][info   ][gc,heap     ] GC(2) Archive regions: 2->2
[0.325s][info   ][gc,heap     ] GC(2) Humongous regions: 38->25
[0.325s][info   ][gc,metaspace] GC(2) Metaspace: 151K(384K)->151K(384K) NonClass: 145K(256K)->145K(256K) Class: 5K(128K)->5K(128K)
[0.325s][info   ][gc          ] GC(2) Pause Young (Normal) (G1 Evacuation Pause) 93M->58M(130M) 22.469ms
[0.325s][info   ][gc,cpu      ] GC(2) User=0.02s Sys=0.02s Real=0.03s
[0.334s][info   ][gc,start    ] GC(3) Pause Young (Concurrent Start) (G1 Humongous Allocation)
[0.334s][info   ][gc,task     ] GC(3) Using 6 workers of 6 for evacuation
[0.337s][info   ][gc,phases   ] GC(3)   Pre Evacuate Collection Set: 0.1ms
[0.337s][info   ][gc,phases   ] GC(3)   Merge Heap Roots: 0.0ms
[0.337s][info   ][gc,phases   ] GC(3)   Evacuate Collection Set: 1.5ms
[0.337s][info   ][gc,phases   ] GC(3)   Post Evacuate Collection Set: 0.2ms
[0.337s][info   ][gc,phases   ] GC(3)   Other: 0.3ms
[0.337s][info   ][gc,heap     ] GC(3) Eden regions: 4->0(26)
[0.337s][info   ][gc,heap     ] GC(3) Survivor regions: 6->2(4)
[0.337s][info   ][gc,heap     ] GC(3) Old regions: 27->34
[0.337s][info   ][gc,heap     ] GC(3) Archive regions: 2->2
[0.337s][info   ][gc,heap     ] GC(3) Humongous regions: 28->27
[0.337s][info   ][gc,metaspace] GC(3) Metaspace: 151K(384K)->151K(384K) NonClass: 145K(256K)->145K(256K) Class: 5K(128K)->5K(128K)
[0.337s][info   ][gc          ] GC(3) Pause Young (Concurrent Start) (G1 Humongous Allocation) 65M->62M(130M) 2.195ms
[0.337s][info   ][gc,cpu      ] GC(3) User=0.01s Sys=0.01s Real=0.01s
[0.337s][info   ][gc          ] GC(4) Concurrent Mark Cycle
[0.337s][info   ][gc,marking  ] GC(4) Concurrent Clear Claimed Marks
[0.337s][info   ][gc,marking  ] GC(4) Concurrent Clear Claimed Marks 0.095ms
[0.337s][info   ][gc,marking  ] GC(4) Concurrent Scan Root Regions
[0.337s][info   ][gc,marking  ] GC(4) Concurrent Scan Root Regions 0.431ms
[0.337s][info   ][gc,marking  ] GC(4) Concurrent Mark
[0.337s][info   ][gc,marking  ] GC(4) Concurrent Mark From Roots
[0.337s][info   ][gc,task     ] GC(4) Using 2 workers of 2 for marking
[0.360s][info   ][gc,marking  ] GC(4) Concurrent Mark From Roots 22.911ms
[0.360s][info   ][gc,marking  ] GC(4) Concurrent Preclean
[0.361s][info   ][gc,marking  ] GC(4) Concurrent Preclean 0.103ms
[0.361s][info   ][gc,start    ] GC(4) Pause Remark
[0.362s][info   ][gc          ] GC(4) Pause Remark 77M->77M(135M) 0.655ms
[0.362s][info   ][gc,cpu      ] GC(4) User=0.00s Sys=0.00s Real=0.00s
[0.372s][info   ][gc,marking  ] GC(4) Concurrent Mark 34.996ms
[0.373s][info   ][gc,marking  ] GC(4) Concurrent Rebuild Remembered Sets
[0.375s][info   ][gc,marking  ] GC(4) Concurrent Rebuild Remembered Sets 2.030ms
[0.379s][info   ][gc,start    ] GC(4) Pause Cleanup
[0.379s][info   ][gc          ] GC(4) Pause Cleanup 78M->78M(135M) 0.320ms
[0.379s][info   ][gc,cpu      ] GC(4) User=0.00s Sys=0.00s Real=0.00s
[0.389s][info   ][gc,marking  ] GC(4) Concurrent Cleanup for Next Mark
[0.392s][info   ][gc,marking  ] GC(4) Concurrent Cleanup for Next Mark 2.813ms
[0.392s][info   ][gc          ] GC(4) Concurrent Mark Cycle 55.437ms
[0.393s][info   ][gc,start    ] GC(5) Pause Young (Concurrent Start) (G1 Humongous Allocation)
[0.393s][info   ][gc,task     ] GC(5) Using 6 workers of 6 for evacuation
[0.400s][info   ][gc,phases   ] GC(5)   Pre Evacuate Collection Set: 0.1ms
[0.400s][info   ][gc,phases   ] GC(5)   Merge Heap Roots: 0.0ms
[0.400s][info   ][gc,phases   ] GC(5)   Evacuate Collection Set: 4.2ms
[0.400s][info   ][gc,phases   ] GC(5)   Post Evacuate Collection Set: 2.0ms
[0.400s][info   ][gc,phases   ] GC(5)   Other: 0.2ms
[0.400s][info   ][gc,heap     ] GC(5) Eden regions: 10->0(127)
[0.400s][info   ][gc,heap     ] GC(5) Survivor regions: 2->4(4)
[0.400s][info   ][gc,heap     ] GC(5) Old regions: 34->35
[0.400s][info   ][gc,heap     ] GC(5) Archive regions: 2->2
[0.400s][info   ][gc,heap     ] GC(5) Humongous regions: 36->28
[0.400s][info   ][gc,metaspace] GC(5) Metaspace: 151K(384K)->151K(384K) NonClass: 145K(256K)->145K(256K) Class: 5K(128K)->5K(128K)
[0.400s][info   ][gc          ] GC(5) Pause Young (Concurrent Start) (G1 Humongous Allocation) 81M->67M(273M) 6.564ms
[0.400s][info   ][gc,cpu      ] GC(5) User=0.01s Sys=0.01s Real=0.01s
[0.400s][info   ][gc          ] GC(6) Concurrent Undo Cycle
[0.400s][info   ][gc,marking  ] GC(6) Concurrent Cleanup for Next Mark
[0.416s][info   ][gc,marking  ] GC(6) Concurrent Cleanup for Next Mark 16.185ms
[0.416s][info   ][gc          ] GC(6) PkQ4� 16.388ms
[0.588s][info   ][gc,start    ] GC(7) Pause Young (Concurrent Start) (G1 Humongous Allocation)
[0.588s][info   ][gc,task     ] GC(7) Using 6 workers of 6 for evacuation
[0.602s][info   ][gc,phases   ] GC(7)   Pre Evacuate Collection Set: 0.1ms
[0.602s][info   ][gc,phases   ] GC(7)   Merge Heap Roots: 0.1ms
[0.602s][info   ][gc,phases   ] GC(7)   Evacuate Collection Set: 13.4ms
[0.602s][info   ][gc,phases   ] GC(7)   Post Evacuate Collection Set: 0.4ms
[0.602s][info   ][gc,phases   ] GC(7)   Other: 0.2ms
[0.602s][info   ][gc,heap     ] GC(7) Eden regions: 92->0(56)
[0.602s][info   ][gc,heap     ] GC(7) Survivor regions: 4->17(17)
[0.602s][info   ][gc,heap     ] GC(7) Old regions: 35->57
[0.602s][info   ][gc,heap     ] GC(7) Archive regions: 2->2
[0.602s][info   ][gc,heap     ] GC(7) Humongous regions: 86->53
[0.602s][info   ][gc,metaspace] GC(7) Metaspace: 152K(384K)->152K(384K) NonClass: 146K(256K)->146K(256K) Class: 5K(128K)->5K(128K)
[0.602s][info   ][gc          ] GC(7) Pause Young (Concurrent Start) (G1 Humongous Allocation) 216M->127M(273M) 14.310ms
[0.602s][info   ][gc,cpu      ] GC(7) User=0.02s Sys=0.03s Real=0.01s
[0.602s][info   ][gc          ] GC(8) Concurrent Undo Cycle
[0.602s][info   ][gc,marking  ] GC(8) Concurrent Cleanup for Next Mark
[0.604s][info   ][gc,marking  ] GC(8) Concurrent Cleanup for Next Mark 1.761ms
[0.604s][info   ][gc          ] GC(8) PkQ4� 1.889ms
[0.621s][info   ][gc,start    ] GC(9) Pause Young (Concurrent Start) (G1 Humongous Allocation)
[0.621s][info   ][gc,task     ] GC(9) Using 6 workers of 6 for evacuation
[0.632s][info   ][gc,phases   ] GC(9)   Pre Evacuate Collection Set: 0.1ms
[0.632s][info   ][gc,phases   ] GC(9)   Merge Heap Roots: 0.1ms
[0.632s][info   ][gc,phases   ] GC(9)   Evacuate Collection Set: 10.5ms
[0.632s][info   ][gc,phases   ] GC(9)   Post Evacuate Collection Set: 0.2ms
[0.632s][info   ][gc,phases   ] GC(9)   Other: 0.2ms
[0.632s][info   ][gc,heap     ] GC(9) Eden regions: 23->0(50)
[0.632s][info   ][gc,heap     ] GC(9) Survivor regions: 17->9(10)
[0.632s][info   ][gc,heap     ] GC(9) Old regions: 57->73
[0.632s][info   ][gc,heap     ] GC(9) Archive regions: 2->2
[0.632s][info   ][gc,heap     ] GC(9) Humongous regions: 64->57
[0.632s][info   ][gc,metaspace] GC(9) Metaspace: 152K(384K)->152K(384K) NonClass: 146K(256K)->146K(256K) Class: 5K(128K)->5K(128K)
[0.632s][info   ][gc          ] GC(9) Pause Young (Concurrent Start) (G1 Humongous Allocation) 160M->139M(273M) 11.252ms
[0.632s][info   ][gc,cpu      ] GC(9) User=0.02s Sys=0.00s Real=0.01s
[0.632s][info   ][gc          ] GC(10) Concurrent Mark Cycle
[0.632s][info   ][gc,marking  ] GC(10) Concurrent Clear Claimed Marks
[0.632s][info   ][gc,marking  ] GC(10) Concurrent Clear Claimed Marks 0.012ms
[0.632s][info   ][gc,marking  ] GC(10) Concurrent Scan Root Regions
[0.632s][info   ][gc,marking  ] GC(10) Concurrent Scan Root Regions 0.098ms
[0.632s][info   ][gc,marking  ] GC(10) Concurrent Mark
[0.632s][info   ][gc,marking  ] GC(10) Concurrent Mark From Roots
[0.632s][info   ][gc,task     ] GC(10) Using 2 workers of 2 for marking
[0.644s][info   ][gc,marking  ] GC(10) Concurrent Mark From Roots 11.393ms
[0.644s][info   ][gc,marking  ] GC(10) Concurrent Preclean
[0.644s][info   ][gc,marking  ] GC(10) Concurrent Preclean 0.075ms
[0.647s][info   ][gc,start    ] GC(10) Pause Remark
[0.650s][info   ][gc          ] GC(10) Pause Remark 143M->143M(273M) 3.124ms
[0.650s][info   ][gc,cpu      ] GC(10) User=0.00s Sys=0.01s Real=0.00s
[0.659s][info   ][gc,marking  ] GC(10) Concurrent Mark 27.020ms
[0.659s][info   ][gc,marking  ] GC(10) Concurrent Rebuild Remembered Sets
[0.660s][info   ][gc,marking  ] GC(10) Concurrent Rebuild Remembered Sets 0.818ms
[0.660s][info   ][gc,start    ] GC(10) Pause Cleanup
[0.660s][info   ][gc          ] GC(10) Pause Cleanup 143M->143M(273M) 0.118ms
[0.660s][info   ][gc,cpu      ] GC(10) User=0.00s Sys=0.00s Real=0.00s
[0.661s][info   ][gc,marking  ] GC(10) Concurrent Cleanup for Next Mark
[0.663s][info   ][gc,marking  ] GC(10) Concurrent Cleanup for Next Mark 2.031ms
[0.663s][info   ][gc          ] GC(10) Concurrent Mark Cycle 30.474ms
[0.663s][info   ][gc,start    ] GC(11) Pause Young (Concurrent Start) (G1 Humongous Allocation)
[0.663s][info   ][gc,task     ] GC(11) Using 6 workers of 6 for evacuation
[0.665s][info   ][gc,phases   ] GC(11)   Pre Evacuate Collection Set: 0.1ms
[0.665s][info   ][gc,phases   ] GC(11)   Merge Heap Roots: 0.0ms
[0.665s][info   ][gc,phases   ] GC(11)   Evacuate Collection Set: 1.7ms
[0.665s][info   ][gc,phases   ] GC(11)   Post Evacuate Collection Set: 0.2ms
[0.665s][info   ][gc,phases   ] GC(11)   Other: 0.1ms
[0.665s][info   ][gc,heap     ] GC(11) Eden regions: 8->0(52)
[0.665s][info   ][gc,heap     ] GC(11) Survivor regions: 9->4(8)
[0.665s][info   ][gc,heap     ] GC(11) Old regions: 73->81
[0.665s][info   ][gc,heap     ] GC(11) Archive regions: 2->2
[0.665s][info   ][gc,heap     ] GC(11) Humongous regions: 62->60
[0.665s][info   ][gc,metaspace] GC(11) Metaspace: 152K(384K)->152K(384K) NonClass: 146K(256K)->146K(256K) Class: 5K(128K)->5K(128K)
[0.665s][info   ][gc          ] GC(11) Pause Young (Concurrent Start) (G1 Humongous Allocation) 151M->145M(273M) 2.345ms
[0.665s][info   ][gc,cpu      ] GC(11) User=0.00s Sys=0.00s Real=0.01s
[0.665s][info   ][gc          ] GC(12) Concurrent Mark Cycle
[0.665s][info   ][gc,marking  ] GC(12) Concurrent Clear Claimed Marks
[0.665s][info   ][gc,marking  ] GC(12) Concurrent Clear Claimed Marks 0.006ms
[0.665s][info   ][gc,marking  ] GC(12) Concurrent Scan Root Regions
[0.665s][info   ][gc,marking  ] GC(12) Concurrent Scan Root Regions 0.044ms
[0.665s][info   ][gc,marking  ] GC(12) Concurrent Mark
[0.668s][info   ][gc,marking  ] GC(12) Concurrent Mark From Roots
[0.668s][info   ][gc,task     ] GC(12) Using 2 workers of 2 for marking
[0.681s][info   ][gc,marking  ] GC(12) Concurrent Mark From Roots 13.242ms
[0.681s][info   ][gc,marking  ] GC(12) Concurrent Preclean
[0.681s][info   ][gc,marking  ] GC(12) Concurrent Preclean 0.086ms
[0.684s][info   ][gc,start    ] GC(12) Pause Remark
[0.686s][info   ][gc          ] GC(12) Pause Remark 167M->167M(282M) 1.985ms
[0.686s][info   ][gc,cpu      ] GC(12) User=0.00s Sys=0.00s Real=0.01s
[0.692s][info   ][gc,marking  ] GC(12) Concurrent Mark 27.095ms
[0.693s][info   ][gc,marking  ] GC(12) Concurrent Rebuild Remembered Sets
[0.694s][info   ][gc,marking  ] GC(12) Concurrent Rebuild Remembered Sets 1.547ms
[0.694s][info   ][gc,start    ] GC(12) Pause Cleanup
[0.694s][info   ][gc          ] GC(12) Pause Cleanup 167M->167M(282M) 0.124ms
[0.694s][info   ][gc,cpu      ] GC(12) User=0.00s Sys=0.00s Real=0.00s
[0.694s][info   ][gc,marking  ] GC(12) Concurrent Cleanup for Next Mark
[0.695s][info   ][gc,marking  ] GC(12) Concurrent Cleanup for Next Mark 0.580ms
[0.695s][info   ][gc          ] GC(12) Concurrent Mark Cycle 29.760ms
[0.745s][info   ][gc,start    ] GC(13) Pause Young (Prepare Mixed) (G1 Evacuation Pause)
[0.745s][info   ][gc,task     ] GC(13) Using 6 workers of 6 for evacuation
[0.751s][info   ][gc,phases   ] GC(13)   Pre Evacuate Collection Set: 0.2ms
[0.751s][info   ][gc,phases   ] GC(13)   Merge Heap Roots: 0.1ms
[0.751s][info   ][gc,phases   ] GC(13)   Evacuate Collection Set: 5.1ms
[0.751s][info   ][gc,phases   ] GC(13)   Post Evacuate Collection Set: 0.4ms
[0.751s][info   ][gc,phases   ] GC(13)   Other: 0.2ms
[0.751s][info   ][gc,heap     ] GC(13) Eden regions: 52->0(7)
[0.751s][info   ][gc,heap     ] GC(13) Survivor regions: 4->7(7)
[0.751s][info   ][gc,heap     ] GC(13) Old regions: 81->102
[0.751s][info   ][gc,heap     ] GC(13) Archive regions: 2->2
[0.752s][info   ][gc,heap     ] GC(13) Humongous regions: 85->67
[0.752s][info   ][gc,metaspace] GC(13) Metaspace: 152K(384K)->152K(384K) NonClass: 146K(256K)->146K(256K) Class: 5K(128K)->5K(128K)
[0.752s][info   ][gc          ] GC(13) Pause Young (Prepare Mixed) (G1 Evacuation Pause) 222M->176M(282M) 6.173ms
[0.752s][info   ][gc,cpu      ] GC(13) User=0.01s Sys=0.00s Real=0.00s
[0.757s][info   ][gc,start    ] GC(14) Pause Young (Mixed) (G1 Evacuation Pause)
[0.757s][info   ][gc,task     ] GC(14) Using 6 workers of 6 for evacuation
[0.775s][info   ][gc,phases   ] GC(14)   Pre Evacuate Collection Set: 0.6ms
[0.775s][info   ][gc,phases   ] GC(14)   Merge Heap Roots: 0.2ms
[0.775s][info   ][gc,phases   ] GC(14)   Evacuate Collection Set: 16.9ms
[0.775s][info   ][gc,phases   ] GC(14)   Post Evacuate Collection Set: 0.3ms
[0.775s][info   ][gc,phases   ] GC(14)   Other: 0.5ms
[0.775s][info   ][gc,heap     ] GC(14) Eden regions: 7->0(43)
[0.775s][info   ][gc,heap     ] GC(14) Survivor regions: 7->2(2)
[0.775s][info   ][gc,heap     ] GC(14) Old regions: 102->99
[0.775s][info   ][gc,heap     ] GC(14) Archive regions: 2->2
[0.775s][info   ][gc,heap     ] GC(14) Humongous regions: 68->68
[0.775s][info   ][gc,metaspace] GC(14) Metaspace: 152K(384K)->152K(384K) NonClass: 146K(256K)->146K(256K) Class: 5K(128K)->5K(128K)
[0.775s][info   ][gc          ] GC(14) Pause Young (Mixed) (G1 Evacuation Pause) 184M->169M(282M) 18.592ms
[0.775s][info   ][gc,cpu      ] GC(14) User=0.01s Sys=0.01s Real=0.02s
[0.776s][info   ][gc,start    ] GC(15) Pause Young (Concurrent Start) (G1 Humongous Allocation)
[0.776s][info   ][gc,task     ] GC(15) Using 6 workers of 6 for evacuation
[0.785s][info   ][gc,phases   ] GC(15)   Pre Evacuate Collection Set: 0.1ms
[0.785s][info   ][gc,phases   ] GC(15)   Merge Heap Roots: 0.0ms
[0.785s][info   ][gc,phases   ] GC(15)   Evacuate Collection Set: 0.4ms
[0.785s][info   ][gc,phases   ] GC(15)   Post Evacuate Collection Set: 7.8ms
[0.785s][info   ][gc,phases   ] GC(15)   Other: 0.2ms
[0.785s][info   ][gc,heap     ] GC(15) Eden regions: 1->0(303)
[0.785s][info   ][gc,heap     ] GC(15) Survivor regions: 2->2(6)
[0.785s][info   ][gc,heap     ] GC(15) Old regions: 99->99
[0.785s][info   ][gc,heap     ] GC(15) Archive regions: 2->2
[0.785s][info   ][gc,heap     ] GC(15) Humongous regions: 68->68
[0.785s][info   ][gc,metaspace] GC(15) Metaspace: 152K(384K)->152K(384K) NonClass: 146K(256K)->146K(256K) Class: 5K(128K)->5K(128K)
[0.785s][info   ][gc          ] GC(15) Pause Young (Concurrent Start) (G1 Humongous Allocation) 170M->169M(846M) 8.667ms
[0.785s][info   ][gc,cpu      ] GC(15) User=0.01s Sys=0.00s Real=0.01s
[0.785s][info   ][gc          ] GC(16) Concurrent Undo Cycle
[0.785s][info   ][gc,marking  ] GC(16) Concurrent Cleanup for Next Mark
[0.798s][info   ][gc,marking  ] GC(16) Concurrent Cleanup for Next Mark 13.004ms
[0.798s][info   ][gc          ] GC(16) PkQ4� 13.132ms
执行结束!共生成对象次数:3334
[1.142s][info   ][gc,heap,exit] Heap
[1.142s][info   ][gc,heap,exit]  garbage-first heap   total 866304K, used 552372K [0x0000000780000000, 0x0000000800000000)
[1.142s][info   ][gc,heap,exit]   region size 1024K, 237 young (242688K), 2 survivors (2048K)
[1.142s][info   ][gc,heap,exit]  Metaspace       used 250K, committed 448K, reserved 1056768K
[1.142s][info   ][gc,heap,exit]   class space    used 8K, committed 128K, reserved 1048576K
(base) 
```