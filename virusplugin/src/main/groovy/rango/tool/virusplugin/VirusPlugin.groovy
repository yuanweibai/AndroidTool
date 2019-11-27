package rango.tool.virusplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class VirusPlugin implements Plugin<Project> {
    void apply(Project project) {
        def classTransform = new VirusTransform(project)
        project.android.registerTransform(classTransform)
    }
}
