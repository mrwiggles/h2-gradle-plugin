package org.jamescarr.gradle
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

public class H2PluginTest {
	private Project project
	private H2Plugin plugin

	@Before
	public void beforeEach() {
        project = ProjectBuilder.builder()
			//.withProjectDir(testDir)
			.build()
        project.apply plugin: 'h2'
		project.convention.add "h2", {
			databaseName {
				scripts = ['a/b/c.sql']
			}
		}
	}

	@Test
	public void "verify h2start details"(){
		Task task = project.tasks["h2start"]

		assert task.name == 'h2start'
		assert task.group == 'h2'
		assert task.description  == 'Starts an embedded h2 database.'
	}

    @Test
    public void "verify h2stop details"(){
        Task task = project.tasks["h2stop"]

        assert task.name == 'h2stop'
        assert task.group == 'h2'
        assert task.description  == 'Stops an embedded h2 database.'
    }

	@Test
	@Ignore("Under Construction")
	public void "should have scripts available on task"(){
		println project.getState()
		
		Task task = project.tasks[H2Plugin.H2_START_TASK_NAME]
		
		assert task.scripts == ['databaseName':['a/b/c.sql']]
	}
	

}
