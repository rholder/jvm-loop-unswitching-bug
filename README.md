##Overview
This project rolls up a repeatable test case that non-deterministically causes
the JVM to produce a segmentation fault of the following form:

    #
    # A fatal error has been detected by the Java Runtime Environment:
    #
    #  SIGSEGV (0xb) at pc=0x00007fcd15089da8, pid=26646, tid=140518304237312
    #
    # JRE version: 7.0_21-b02
    # Java VM: OpenJDK 64-Bit Server VM (23.7-b01 mixed mode linux-amd64 compressed oops)
    # Problematic frame:
    # J  org.apache.http.impl.cookie.BestMatchSpec.formatCookies(Ljava/util/List;)Ljava/util/List;
    #
    # Failed to write core dump. Core dumps have been disabled. To enable core dumping, try "ulimit -c unlimited" before starting Java again
    #
    # If you would like to submit a bug report, please include
    # instructions on how to reproduce the bug and visit:
    #   https://bugs.launchpad.net/ubuntu/+source/openjdk-7/
    #

This appears to exhibit the same behavior as the bug reported [here](https://code.google.com/p/crawler4j/issues/detail?id=136).
Adding the `-XX:-LoopUnswitching` JVM argument fixes this issue, as referenced
in a similar bug reported in [here](https://issues.apache.org/jira/browse/HTTPCLIENT-1173).

Upstream problem has been reported: [java 7 branch](http://bugs.java.com/view_bug.do?bug_id=8025398) and [java 8 branch](http://bugs.java.com/view_bug.do?bug_id=8021898)

##How to run
```
git clone https://github.com/rholder/jvm-loop-unswitching-bug.git
cd jvm-loop-unswitching-bug
./gradlew clean test --info
```
You may need to repeat the test command a few times until the segmentation fault
appears.

##Failing JVM's Tested
* java full version "1.7.0_51-b13"
  * Java(TM) SE Runtime Environment (build 1.7.0_51-b13)
  * Java HotSpot(TM) 64-Bit Server VM (build 24.51-b03, mixed mode)
* java full version "1.7.0_21-b02"
  * OpenJDK Runtime Environment (IcedTea 2.3.9) (7u21-2.3.9-1ubuntu1)
  * OpenJDK 64-Bit Server VM (build 23.7-b01, mixed mode)
* java full version "1.7.0_17-b02"
  * Java(TM) SE Runtime Environment (build 1.7.0_17-b02)
  * Java HotSpot(TM) 64-Bit Server VM (build 23.7-b01, mixed mode)
* java full version "1.7.0_15-b20"
  * OpenJDK Runtime Environment (IcedTea7 2.3.7) (7u15-2.3.7-0ubuntu1~12.04.1)
  * OpenJDK 64-Bit Server VM (build 23.7-b01, mixed mode)
* java full version "1.6.0_24-b24"
  * OpenJDK Runtime Environment (IcedTea6 1.11.5) (amazon-53.1.11.5.47.amzn1-x86_64)
  * OpenJDK 64-Bit Server VM (build 20.0-b12, mixed mode)

##Succeeding JVM's Tested
* java version "1.7.0_60-ea" (Early Access)
  * Java(TM) SE Runtime Environment (build 1.7.0_60-ea-b04)
  * Java HotSpot(TM) 64-Bit Server VM (build 24.60-b07, mixed mode)
* java version "1.8.0" (Early Access)
  * Java(TM) SE Runtime Environment (build 1.8.0-b129)
  * Java HotSpot(TM) 64-Bit Server VM (build 25.0-b69, mixed mode)

##Additional Notes
Current testing indicates that both single and multi-core machines cause this
bug to manifest itself.

##Contributors
* Ray Holder (rholder)
* Chad Bayer (chadbay)
