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

##Tested JVM's
* java version "1.7.0_21"
  * OpenJDK Runtime Environment (IcedTea 2.3.9) (7u21-2.3.9-1ubuntu1)
  * OpenJDK 64-Bit Server VM (build 23.7-b01, mixed mode)

##Additional Notes
Current testing indicates that on a single-core machine this bug does not
manifest itself.

##Contributors
* Ray Holder (rholder)
* Chad Bayer (chadbay)
