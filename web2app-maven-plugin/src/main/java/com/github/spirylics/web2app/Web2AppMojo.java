package com.github.spirylics.web2app;


import org.apache.commons.io.FileUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public abstract class Web2AppMojo extends AbstractMojo {

    /**
     * Directory where will be installed node & co
     */
    @Parameter(defaultValue = "${project.basedir}/frontend", readonly = true, required = true)
    File frontendDirectory;

    /**
     * Directory where will be installed frontend dependencies
     */
    @Parameter(defaultValue = "${project.basedir}/working", readonly = true, required = true)
    File frontendWorkingDirectory;

    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required = true)
    File buildDirectory;

    @Parameter(defaultValue = "${project.basedir}/working/node_modules/cordova/bin/cordova", readonly = true, required = true)
    File cordovaExec;

    @Parameter(defaultValue = "${project.build.directory}/${project.name}-${project.version}", readonly = true, required = true)
    File appDirectory;

    @Parameter(readonly = true, required = true)
    Dependency dependency;

    @Parameter(defaultValue = "**", readonly = true, required = true)
    String dependencyIncludes;

    @Parameter(defaultValue = "META-INF/,WEB-INF/", readonly = true, required = true)
    String dependencyExcludes;

    @Parameter(defaultValue = "${app.group}", readonly = true, required = true)
    String appGroup;

    @Parameter(defaultValue = "${app.name}", readonly = true, required = true)
    String appName;

    @Parameter(defaultValue = "${app.version}", readonly = true, required = true)
    String appVersion;

    @Parameter(defaultValue = "${app.version.code}", readonly = true, required = true)
    String appVersionCode;

    @Parameter(defaultValue = "${app.description}", readonly = true, required = true)
    String appDescription;

    @Parameter(defaultValue = "${app.author.email}", readonly = true, required = true)
    String appAuthorEmail;

    @Parameter(defaultValue = "${app.author.site}", readonly = true, required = true)
    String appAuthorSite;

    @Parameter(defaultValue = "${project.basedir}/config.xml", readonly = true, required = true)
    File appConfig;

    @Parameter(defaultValue = "index.html", readonly = true, required = true)
    String appContent;

    @Parameter(readonly = true, required = true)
    List<String> platforms = Arrays.asList("browser");

    @Parameter(readonly = true, required = true)
    List<String> plugins = Arrays.asList();

    @Parameter(defaultValue = "", readonly = true, required = true)
    String icon;

    @Parameter(defaultValue = "", readonly = true, required = true)
    String splashscreen;

    @Parameter(readonly = true, required = false)
    String themeColor = null;

    /**
     * Maven project
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject mavenProject;

    /**
     * Maven session
     */
    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    protected MavenSession mavenSession;

    @Component
    protected BuildPluginManager pluginManager;

    protected File getPlatformsDir() {
        return new File(appDirectory, "platforms");
    }

    protected File getPlatformDir(String name) {
        return new File(getPlatformsDir(), name);
    }

    protected File getWwwDir() {
        return new File(appDirectory, "www");
    }

    protected File getContentFile() {
        return new File(getWwwDir(), appContent);
    }

    protected void appendScript(File htmlFile, String scriptSrc) throws IOException {
        String content = FileUtils.readFileToString(htmlFile);
        if (!content.contains(scriptSrc)) {
            content.replaceFirst(
                    "</head>",
                    String.format("\t<script type=\"text/javascript\" src=\"%s\"></script>\n</head>", scriptSrc));
            Files.write(htmlFile.toPath(), content.getBytes());
        }
    }
}
