package com.enterprisedb.efm;

/**
 * @author: wk
 * @Date: 2020/11/6 16:38
 * @Description
 */
public class Version {
    private static final String NAME = "Failover Manager";

    public static final int MAJOR = 2;

    public static final int MINOR = 1;

    public static final String VERSION = "2.1.2";

    private static final String JENKINS = ", Jenkins #{{{JENKINS_BUILD_NUMBER}}}";

    private static final String GIT = ", ( {{{GIT_DESCRIBE}}} )";

    public static String getVersion() {
        StringBuilder sb = new StringBuilder();
        sb.append("Failover Manager").append(", version ").append("2.1.2");
        if (!", Jenkins #{{{JENKINS_BUILD_NUMBER}}}".contains("JENKINS_BUILD_NUMBER"))
            sb.append(", Jenkins #{{{JENKINS_BUILD_NUMBER}}}");
        if (!", ( {{{GIT_DESCRIBE}}} )".contains("GIT_DESCRIBE"))
            sb.append(", ( {{{GIT_DESCRIBE}}} )");
        return sb.toString();
    }
}
